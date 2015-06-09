package forum

import (
	"github.com/chonglou/gin-contrib/rest"
	. "github.com/chonglou/itpkg/base"
	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"
)

type ForumEngine struct {
	EngineSetup
	dao *ForumDao
}

func (p *ForumEngine) Map() {
	dao := &ForumDao{db: p.Get("db").(*gorm.DB)}
	p.Use("forumDao", dao)
	p.dao = dao
}

func (p *ForumEngine) Mount() {
	g := p.Get("router").(*gin.Engine).Group("/forum")

	rest.CRUD(g, "/article", &ArticleCtrl{})
	rest.CRUD(g, "/board", &BoardCtrl{})
	rest.CRUD(g, "/comment", &CommentCtrl{})
}

func (p *ForumEngine) Migrate() {
	db := p.Get("db").(*gorm.DB)
	db.AutoMigrate(&Article{})
	db.AutoMigrate(&Board{})
	db.Model(&Board{}).AddUniqueIndex("idx_boards_lang_name", "lang", "name")
	db.AutoMigrate(&Comment{})

}

func (p *ForumEngine) Info() (name string, version string, desc string) {
	return "forum", "v102500609", ""
}

func init() {
	Register(&ForumEngine{})
}
