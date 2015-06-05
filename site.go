package itpkg

import (
	"fmt"
	"github.com/gorilla/pat"
	"github.com/jinzhu/gorm"
	"net/http"
	"time"
)

type SiteEngine struct {
	cfg *Config
}

func (p *SiteEngine) Map() {
	log.Info("Init SiteDao")
	aes := Aes{}
	if err := aes.Init(p.cfg.secret[20:52]); err != nil {
		log.Fatalf("Error on init aes: %v", err)
	}
	p.cfg.Use("siteDao", &SiteDao{db: p.cfg.db, aes: &aes})
}

func (p *SiteEngine) Mount(r *pat.Router) {

	r.Get("/index.json", func(wrt http.ResponseWriter, req *http.Request) {
		dao := p.cfg.Get("siteDao").(*SiteDao)
		lang := "zh-CN" //Lang(req)
		si := make(map[string]interface{}, 0)
		for _, k := range []string{"title", "author", "keywords", "description", "copyright"} {
			var v string
			dao.GetSiteInfo(k, lang, &v)
			si[k] = v
		}
		si["locale"] = lang
		JSON(wrt, si)
	})

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
