package wiki

import (
	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"

	"github.com/chonglou/gin-contrib/rest"
	. "github.com/chonglou/itpkg/base"
)

type WikiEngine struct {
	Db     *gorm.DB    `inject:""`
	Dao    *WikiDao    `inject:""`
	Router *gin.Engine `inject:""`
}

func (p *WikiEngine) Mount() {
	g := p.Router.Group("/wiki")
	rest.CRUD(g, "/item", &WikiCtrl{})
}

func (p *WikiEngine) Migrate() {
	db := p.Db

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
	Db *gorm.DB `inject:""`
}

func init() {
	Register(&WikiEngine{})
}
