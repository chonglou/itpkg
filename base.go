package itpkg

import (
	"fmt"
	"github.com/go-martini/martini"
	"github.com/jinzhu/gorm"
	"github.com/martini-contrib/render"
	"net/http"
	"time"
)

type BaseEngine struct {
	cfg *Config
	db  *gorm.DB
	app *martini.ClassicMartini
}

func (p *BaseEngine) Map() {
	aes := Aes{}
	if err := aes.Init(p.cfg.secret[20:52]); err != nil {
		log.Fatalf("Error on init aes: %v", err)
	}
	p.app.Map(&BaseDao{db: p.db, aes: &aes})
}

func (p *BaseEngine) Mount() {
	p.app.Get("/index.json", func(r render.Render, dao *BaseDao, req *http.Request) {
		lang := Lang(req)
		si := make(map[string]interface{}, 0)
		for _, k := range []string{"title", "author", "keywords", "description", "copyright"} {
			var v string
			dao.GetSiteInfo(k, lang, &v)
			si[k] = v
		}
		si["locale"] = lang
		r.JSON(200, si)
	})
}

func (p *BaseEngine) Migrate() {
	p.db.AutoMigrate(&Setting{})
}

func (p *BaseEngine) Info() (name string, version string, desc string) {
	return "base", "v10150530", "Base framework"
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

type BaseDao struct {
	db  *gorm.DB
	aes *Aes
}

func (p *BaseDao) siteInfo(key string, lang string) string {
	return fmt.Sprintf("site://%s/%s", key, lang)
}

func (p *BaseDao) SetSiteInfo(key string, lang string, val interface{}) {
	p.Set(p.siteInfo(key, lang), val, false)
}

func (p *BaseDao) GetSiteInfo(key string, lang string, val interface{}) {
	p.Get(p.siteInfo(key, lang), val, false)
}

func (p *BaseDao) Set(key string, val interface{}, enc bool) error {
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

func (p *BaseDao) Get(key string, val interface{}, enc bool) error {
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
