package teamwork

import (
	"github.com/chonglou/gin-contrib/rest"
	. "github.com/chonglou/itpkg/base"
	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"
)

type TeamworkEngine struct {
	EngineSetup
	dao *TeamworkDao
}

func (p *TeamworkEngine) Map() {
	dao := &TeamworkDao{db: p.Get("db").(*gorm.DB)}
	p.Use("teamworkDao", dao)
	p.dao = dao
}

func (p *TeamworkEngine) Mount() {
	g := p.Get("router").(*gin.Engine).Group("/tw")
	rest.CRUD(g, "/project", &ProjectCtrl{})
	rest.CRUD(g, "/task", &TaskCtrl{})
	rest.CRUD(g, "/tag", &TagCtrl{})
	rest.CRUD(g, "/comment", &CommentCtrl{})
}

func (p *TeamworkEngine) Migrate() {
	db := p.Get("db").(*gorm.DB)
	db.AutoMigrate(&Project{})
	db.Model(&Project{}).AddUniqueIndex("idx_tw_projects_uid_ver", "uid", "ver")
	db.AutoMigrate(&Task{})
	db.Model(&Task{}).AddUniqueIndex("idx_tw_tasks_uid_ver", "uid", "ver")
	db.AutoMigrate(&Tag{})
	db.Model(&Tag{}).AddUniqueIndex("idx_tw_tags_project_name", "project_uid", "name")
	db.AutoMigrate(&Comment{})
	db.Model(&Comment{}).AddUniqueIndex("idx_tw_comments_uid_ver", "uid", "ver")

}

func (p *TeamworkEngine) Info() (name string, version string, desc string) {
	return "teamwork", "v102500609", ""
}

func init() {
	Register(&TeamworkEngine{})
}
