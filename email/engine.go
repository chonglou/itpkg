package email

import (
	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"

	"github.com/chonglou/gin-contrib/rest"
	itpkg "github.com/chonglou/itpkg/base"
)

type EmailEngine struct {
	Db     *gorm.DB    `inject:""`
	Dao    *EmailDao   `inject:""`
	Router *gin.Engine `inject:""`
}

func (p *EmailEngine) Mount() {
	g := p.Router.Group("/email")
	rest.CRUD(g, "/box", &BoxCtrl{})
	rest.CRUD(g, "/message", &MessageCtrl{})
	rest.CRUD(g, "/user", &UserCtrl{})
}

func (p *EmailEngine) Migrate() {
	db := p.Db
	db.AutoMigrate(&User{})
	db.AutoMigrate(&Message{})
	db.AutoMigrate(&Box{})

}

func (p *EmailEngine) Info() (name string, version string, desc string) {
	return "Email", "v102500629", ""
}

func init() {
	itpkg.Register(&EmailEngine{})
}
