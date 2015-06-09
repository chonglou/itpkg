package cms

import (
	"github.com/chonglou/gin-contrib/rest"
	. "github.com/chonglou/itpkg/base"
	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"
)

type CmsEngine struct {
	EngineSetup
	dao *CmsDao
}

func (p *CmsEngine) Map() {
	dao := &CmsDao{db: p.Get("db").(*gorm.DB)}
	p.Use("cmsDao", dao)
	p.dao = dao
}

func (p *CmsEngine) Mount() {
	g := p.Get("router").(*gin.Engine).Group("/cms")
	rest.CRUD(g, "/article", &ArticleCtrl{})
	rest.CRUD(g, "/tag", &TagCtrl{})
	rest.CRUD(g, "/comment", &CommentCtrl{})
}

func (p *CmsEngine) Migrate() {
	db := p.Get("db").(*gorm.DB)
	db.AutoMigrate(&Article{})
	db.AutoMigrate(&Tag{})
	db.AutoMigrate(&Comment{})

}

func (p *CmsEngine) Info() (name string, version string, desc string) {
	return "cms", "v102500609", ""
}

func init() {
	Register(&CmsEngine{})
}
