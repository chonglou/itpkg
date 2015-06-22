package forum

import (
	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"

	"github.com/chonglou/gin-contrib/rest"
	. "github.com/chonglou/itpkg/base"
)

type ForumEngine struct {
	Db     *gorm.DB    `inject:""`
	Dao    *ForumDao   `inject:""`
	Router *gin.Engine `inject:""`
}

func (p *ForumEngine) Mount() {
	g := p.Router.Group("/forum")

	rest.CRUD(g, "/article", &ArticleCtrl{})
	rest.CRUD(g, "/board", &BoardCtrl{})
	rest.CRUD(g, "/comment", &CommentCtrl{})
}

func (p *ForumEngine) Migrate() {
	db := p.Db
	db.AutoMigrate(&Article{})
	db.AutoMigrate(&Board{})
	db.Model(&Board{}).AddUniqueIndex("idx_forum_boards_lang_name", "lang", "name")
	db.AutoMigrate(&Comment{})

}

func (p *ForumEngine) Info() (name string, version string, desc string) {
	return "forum", "v102500609", ""
}

func init() {
	Register(&ForumEngine{})
}
