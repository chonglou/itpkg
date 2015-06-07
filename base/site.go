package itpkg

import (
	//"expvar"
	"fmt"
	//"github.com/chonglou/sitemap"
	//"github.com/gorilla/feeds"
	"github.com/jinzhu/gorm"
	//"net/http"
	//"strings"
	"time"
)

type SiteEngine struct {
	cfg *Config
}

func (p *SiteEngine) Map() {
	Logger.Debug("Init SiteDao")
	aes := Aes{}
	if err := aes.Init(p.cfg.secret[20:52]); err != nil {
		Logger.Fatalf("Error on init aes: %v", err)
	}
	p.cfg.Use("siteDao", &SiteDao{db: p.cfg.db, aes: &aes})
}

func (p *SiteEngine) Mount() {
	/*
		r.Get("/sitemap.xml", func(wrt http.ResponseWriter, req *http.Request) {
			si := sitemap.New()
			//todo add links from redis
			XML(wrt, si)
		})

		r.Get("/rss.atom", func(wrt http.ResponseWriter, req *http.Request) {
			dao := p.cfg.Get("siteDao").(*SiteDao)
			lang := LANG(req)
			var title, description, author string
			dao.GetSiteInfo("title", lang, &title)
			dao.GetSiteInfo("description", lang, &description)
			dao.GetSiteInfo("author", lang, &author)
			feed := &feeds.Feed{
				Title:       title,
				Link:        &feeds.Link{Href: fmt.Sprintf("https://%s", p.cfg.Http.Host)},
				Description: description,
				Author:      &feeds.Author{strings.Split(author, "@")[0], author},
				Created:     time.Now(),
			}
			feed.Items = []*feeds.Item{
			//todo read from redis
			}

			atom := feeds.Atom{feed}
			XML(wrt, atom.FeedXml())
		})

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

		r.Get("/index.json", func(wrt http.ResponseWriter, req *http.Request) {
			dao := p.cfg.Get("siteDao").(*SiteDao)
			lang := LANG(req)
			si := make(map[string]interface{}, 0)
			for _, k := range []string{"title", "author", "keywords", "description", "copyright"} {
				var v string
				dao.GetSiteInfo(k, lang, &v)
				si[k] = v
			}
			si["locale"] = lang
			JSON(wrt, si)
		})
	*/

}

func (p *SiteEngine) Migrate() {
	p.cfg.db.AutoMigrate(&Setting{})
}

func (p *SiteEngine) Info() (name string, version string, desc string) {
	return "base", "v10150530", "Site framework"
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

type SiteDao struct {
	db  *gorm.DB
	aes *Aes
}

func (p *SiteDao) siteInfo(key string, lang string) string {
	return fmt.Sprintf("site://%s/%s", key, lang)
}

func (p *SiteDao) SetSiteInfo(key string, lang string, val interface{}) {
	p.Set(p.siteInfo(key, lang), val, false)
}

func (p *SiteDao) GetSiteInfo(key string, lang string, val interface{}) {
	p.Get(p.siteInfo(key, lang), val, false)
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

func init() {
	Register("site")
}
