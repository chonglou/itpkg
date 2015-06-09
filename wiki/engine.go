package wiki

import (
	"github.com/chonglou/gin-contrib/rest"
	. "github.com/chonglou/itpkg/base"
	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"
)

type WikiEngine struct {
	EngineSetup
	dao *WikiDao
}

func (p *WikiEngine) Map() {
	dao := &WikiDao{db: p.Get("db").(*gorm.DB)}

	p.Use("wikiDao", dao)
	p.dao = dao

}

func (p *WikiEngine) Mount() {
	g := p.Get("router").(*gin.Engine).Group("/wiki")
	rest.CRUD(g, "/item", &WikiCtrl{})
}

func (p *WikiEngine) Migrate() {
	db := p.Get("db").(*gorm.DB)

	db.AutoMigrate(&Wiki{})
	db.Model(&Wiki{}).AddUniqueIndex("idx_wikis_uid_ver", "uid", "ver")
}
func (p *WikiEngine) Info() (name string, version string, desc string) {
	return "wiki", "v102500607", ""
}

type Wiki struct {
	VModel
	Title string `sql:"size:255;index;not null"`
	Body  string `sql:"type:TEXT;not null"`
}

type WikiCtrl struct {
}

func (p *WikiCtrl) CreateHandler(*gin.Context) {}
func (p *WikiCtrl) ListHandler(*gin.Context)   {}
func (p *WikiCtrl) TakeHandler(*gin.Context)   {}
func (p *WikiCtrl) UpdateHandler(*gin.Context) {}
func (p *WikiCtrl) DeleteHandler(*gin.Context) {}

type WikiDao struct {
	db *gorm.DB
}

func init() {
	Register(&WikiEngine{})
}
