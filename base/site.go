package itpkg

import (
	"encoding/json"
	"fmt"
	"github.com/chonglou/gin-contrib/cache"
	"github.com/chonglou/sitemap"
	"github.com/gin-gonic/gin"
	"github.com/gorilla/feeds"
	"github.com/jinzhu/gorm"
	"io/ioutil"
	"net/http"
	"os"
	"time"
)

type SiteEngine struct {
	EngineSetup
	dao *SiteDao
}

func (p *SiteEngine) Map() {
	Logger.Debug("Init SiteDao")
	aes := Aes{}
	if err := aes.Init(p.cfg.secret[20:52]); err != nil {
		Logger.Fatalf("Error on init aes: %v", err)
	}
	p.dao = &SiteDao{db: p.cfg.db, aes: &aes}
	p.Use("siteDao", p.dao)

	p.Use("t", &LocaleDao{db: p.cfg.db})
}

func (p *SiteEngine) Mount() {
	r := p.cfg.router

	r.GET("/sitemap.xml", cache.CachePage(p.cfg.cache, time.Hour*24, func(c *gin.Context) {
		si := sitemap.New()
		//todo add links from redis
		c.XML(http.StatusOK, si)
	}))

	r.GET("/rss.atom", cache.CachePage(p.cfg.cache, time.Hour*3, func(c *gin.Context) {
		lang := LANG(c)
		feed := &feeds.Feed{
			Title:       p.T(lang, "site.title"),
			Link:        &feeds.Link{Href: fmt.Sprintf("https://%s", p.cfg.Http.Host)},
			Description: p.T(lang, "site.description"),
			Author:      &feeds.Author{p.T(lang, "site.author.name"), p.T(lang, "site.author.email")},
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

	r.GET("/index.json", cache.CachePage(p.cfg.cache, time.Hour*24, func(c *gin.Context) {
		lang := LANG(c)
		si := make(map[string]interface{}, 0)
		for _, k := range []string{"title", "keywords", "description", "copyright"} {
			si[k] = p.T(lang, "site."+k)
		}

		author := make(map[string]string, 0)
		for _, k := range []string{"name", "email"} {
			author[k] = p.T(lang, "site.author."+k)
		}
		si["author"] = author

		si["locale"] = lang

		ei := make([]map[string]string, 0)
		for _, e := range engines {
			n, v, d := e.Info()
			in := make(map[string]string, 0)
			in["name"] = n
			in["version"] = v
			in["description"] = d
			ei = append(ei, in)
		}
		si["engines"] = ei
		c.JSON(http.StatusOK, si)
	}))

}

func (p *SiteEngine) Migrate() {
	db := p.cfg.db
	db.AutoMigrate(&Setting{})
	db.AutoMigrate(&Locale{})
	db.Model(&Locale{}).AddUniqueIndex("idx_locales_key_lang", "key", "lang")

	if err := p.loadLocales("locales"); err != nil {
		Logger.Error("Error on load locales: %v", err)
	}
}

func (p *SiteEngine) Info() (name string, version string, desc string) {
	return "site", "v10150530", "Site framework"
}

func (p *SiteEngine) loadLocales(path string) error {
	Logger.Info("Loading i18n from " + path)
	files, err := ioutil.ReadDir(path)
	if err != nil {
		return err
	}

	tx := p.cfg.db.Begin()
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
		Logger.Info("Find locale file %s(%d)", fn, len(ss))
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

type Model struct {
	ID        uint `gorm:"primary_key"`
	UpdatedAt time.Time
	CreatedAt time.Time
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
	db  *gorm.DB
	aes *Aes
}

func (p *SiteDao) Set(key string, val interface{}, enc bool) error {
	dt, err := Obj2bits(val)
	if err != nil {
		return err
	}
	var iv []byte
	if enc {
		dt, iv = p.aes.Encrypt(dt)
	}

	st := Setting{ID: key}
	var cn int
	p.db.Model(st).Count(&cn)
	if cn == 0 {
		st.Val = dt
		st.Iv = iv
		p.db.Create(&st)
	} else {
		p.db.Model(&st).Updates(Setting{Val: dt, Iv: iv})
	}
	return nil
}

func (p *SiteDao) Get(key string, val interface{}, enc bool) error {
	st := Setting{}
	p.db.Where("id = ?", key).First(&st)
	if st.Val != nil {
		var dt []byte

		if enc {
			dt = p.aes.Decrypt(st.Val, st.Iv)
		} else {
			dt = st.Val
		}
		return Bits2obj(dt, val)
	}
	return nil
}

type LocaleDao struct {
	db *gorm.DB
}

func (p *LocaleDao) Get(lang, key string) string {
	l := Locale{Lang: lang, Key: key}
	p.db.Where("lang = ? AND key = ?", lang, key).First(&l)
	return l.Val
}

func (p *LocaleDao) Set(lang, key, val string) {
	l := Locale{Lang: lang, Key: key}
	p.db.Where("lang = ? AND key = ?", lang, key).First(&l)
	if l.Val == "" {
		p.db.Create(&Locale{Key: key, Lang: lang, Val: val})
	} else {
		p.db.Model(&l).Updates(Locale{Val: val})
	}

}

//---------------------------------------
func init() {
	Register(&SiteEngine{})
}
