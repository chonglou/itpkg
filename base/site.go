package itpkg

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"os"
	"time"

	"github.com/chonglou/gin-contrib/cache"
	"github.com/chonglou/sitemap"
	"github.com/garyburd/redigo/redis"
	"github.com/gin-gonic/gin"
	"github.com/gorilla/feeds"
	"github.com/jinzhu/gorm"
	"github.com/op/go-logging"
)

type SiteEngine struct {
	Db      *gorm.DB        `inject:""`
	SiteDao *SiteDao        `inject:""`
	I18n    *LocaleDao      `inject:""`
	Cfg     *Configuration  `inject:""`
	App     *Application    `inject:""`
	Logger  *logging.Logger `inject:""`
	Router  *gin.Engine     `inject:""`
	Redis   *redis.Pool     `inject:""`

	Cache cache.CacheStore `inject:""`
}

func (p *SiteEngine) Mount() {
	r := p.Router

	r.GET("/sitemap.xml", cache.CachePage(p.Cache, time.Hour*24, func(c *gin.Context) {
		si := sitemap.New()
		//todo add links from redis
		c.XML(http.StatusOK, si)
	}))

	r.GET("/rss.atom", cache.CachePage(p.Cache, time.Hour*3, func(c *gin.Context) {
		lang := LANG(c)
		feed := &feeds.Feed{
			Title:       p.I18n.T(lang, "site.title"),
			Link:        &feeds.Link{Href: fmt.Sprintf("https://%s", p.Cfg.Http.Host)},
			Description: p.I18n.T(lang, "site.description"),
			Author:      &feeds.Author{p.I18n.T(lang, "site.author.name"), p.I18n.T(lang, "site.author.email")},
			Created:     time.Now(),
		}
		feed.Items = []*feeds.Item{
		//todo read from redis
		}

		atom := feeds.Atom{feed}
		c.XML(http.StatusOK, atom.FeedXml())
	}))

	/*
		r.Get("/debug/vars", func(w http.ResponseWriter, req *http.Request) {
			// todo need login
			w.Header().Set("Content-Type", "application/json; charset=utf-8")
			fmt.Fprintf(w, "{")
			first := true
			expvar.Do(func(kv expvar.KeyValue) {
				if !first {
					fmt.Fprintf(w, ",\n")
				}
				first = false
				fmt.Fprintf(w, "%q: %s", kv.Key, kv.Value)
			})
			fmt.Fprintf(w, "}\n")
		})
	*/

	r.GET("/index.json", cache.CachePage(p.Cache, time.Hour*24, func(c *gin.Context) {
		lang := LANG(c)
		si := make(map[string]interface{}, 0)
		for _, k := range []string{"title", "keywords", "description", "copyright"} {
			si[k] = p.I18n.T(lang, "site."+k)
		}

		author := make(map[string]string, 0)
		for _, k := range []string{"name", "email"} {
			author[k] = p.I18n.T(lang, "site.author."+k)
		}
		si["author"] = author

		si["locale"] = lang

		ei := make([]map[string]string, 0)

		LoopEngine(func(en Engine) error {
			n, v, d := en.Info()
			ei = append(ei, map[string]string{"name": n, "version": v, "description": d})
			return nil
		})
		si["engines"] = ei

		c.JSON(http.StatusOK, si)
	}))

}

func (p *SiteEngine) Migrate() {
	db := p.Db
	db.AutoMigrate(&Setting{})
	db.AutoMigrate(&Locale{})
	db.Model(&Locale{}).AddUniqueIndex("idx_locales_key_lang", "key", "lang")

	if err := p.loadLocales("locales"); err != nil {
		p.Logger.Error("Error on load locales: %v", err)
	}
}

func (p *SiteEngine) Info() (name string, version string, desc string) {
	return "site", "v10150621", "Site framework"
}

func (p *SiteEngine) loadLocales(path string) error {
	p.Logger.Info("Loading i18n from " + path)
	files, err := ioutil.ReadDir(path)
	if err != nil {
		return err
	}

	tx := p.Db.Begin()
	for _, f := range files {
		fn := f.Name()

		lang := fn[0:(len(fn) - 5)]

		ss := make(map[string]string, 0)
		fd, err := os.Open(path + "/" + fn)
		if err != nil {
			return err
		}
		defer fd.Close()

		err = json.NewDecoder(fd).Decode(&ss)
		if err != nil {
			return err
		}
		p.Logger.Info("Find locale file %s(%d)", fn, len(ss))
		for key, val := range ss {
			var c int
			tx.Model(Locale{}).Where("lang = ? AND key = ?", lang, key).Count(&c)
			if c > 0 {
				continue
			}
			tx.Create(&Locale{Key: key, Lang: lang, Val: val})
		}
	}

	tx.Commit()
	return nil
}

type Setting struct {
	ID  string `gorm:"primary_key"`
	Val []byte `sql:"not null"`
	Iv  []byte `sql:"size:32"`
}

type Locale struct {
	Key  string `sql:"not null;size:255;index"`
	Val  string `sql:"not null;type:TEXT"`
	Lang string `sql:"not null;size:5;index;default:'en'"`
}

type SiteDao struct {
	Db  *gorm.DB `inject:""`
	Aes *Aes     `inject:""`
}

func (p *SiteDao) Set(key string, val interface{}, enc bool) error {
	dt, err := Obj2bits(val)
	if err != nil {
		return err
	}
	var iv []byte
	if enc {
		dt, iv, err = p.Aes.Encrypt(dt)
		if err != nil {
			return err
		}
	}

	st := Setting{ID: key}
	var cn int
	p.Db.Model(st).Count(&cn)
	if cn == 0 {
		st.Val = dt
		st.Iv = iv
		p.Db.Create(&st)
	} else {
		p.Db.Model(&st).Updates(Setting{Val: dt, Iv: iv})
	}
	return nil
}

func (p *SiteDao) Get(key string, val interface{}, enc bool) error {
	st := Setting{}
	p.Db.Where("id = ?", key).First(&st)
	if st.Val != nil {
		var dt []byte

		if enc {
			dt = p.Aes.Decrypt(st.Val, st.Iv)
		} else {
			dt = st.Val
		}
		return Bits2obj(dt, val)
	}
	return nil
}

type LocaleDao struct {
	Db *gorm.DB `inject:""`
}

func (p *LocaleDao) T(lang, key string, args ...interface{}) string {
	v := p.Get(lang, key)
	if v == "" {
		return fmt.Sprintf("Translation [%s] not found", key)
	}
	return fmt.Sprintf(v, args...)
}

func (p *LocaleDao) Get(lang, key string) string {
	l := Locale{Lang: lang, Key: key}
	p.Db.Where("lang = ? AND key = ?", lang, key).First(&l)
	return l.Val
}

func (p *LocaleDao) Set(lang, key, val string) {
	l := Locale{Lang: lang, Key: key}
	p.Db.Where("lang = ? AND key = ?", lang, key).First(&l)
	if l.Val == "" {
		p.Db.Create(&Locale{Key: key, Lang: lang, Val: val})
	} else {
		p.Db.Model(&l).Updates(Locale{Val: val})
	}

}

//---------------------------------------
func init() {
	Register(&SiteEngine{})
}
