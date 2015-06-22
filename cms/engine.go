package cms

import (
	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"

	"github.com/chonglou/gin-contrib/rest"
	. "github.com/chonglou/itpkg/base"
)

type CmsEngine struct {
	Db     *gorm.DB    `inject:""`
	Dao    *CmsDao     `inject:""`
	Router *gin.Engine `inject:""`
}

func (p *CmsEngine) Mount() {
	g := p.Router.Group("/cms")
	rest.CRUD(g, "/article", &ArticleCtrl{})
	rest.CRUD(g, "/tag", &TagCtrl{})
	rest.CRUD(g, "/comment", &CommentCtrl{})
}

func (p *CmsEngine) Migrate() {
	db := p.Db
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
