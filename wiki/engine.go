package wiki

import (
	. "github.com/chonglou/itpkg/base"
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
}

func (p *WikiEngine) Migrate() {
	p.Get("db").(*gorm.DB).AutoMigrate(&Wiki{})
}
func (p *WikiEngine) Info() (name string, version string, desc string) {
	return "wiki", "v102500607", ""
}

type Wiki struct {
	Model
	Name  string `sql:"not null;size:255;unique_index"`
	Title string `sql:"size:255;index;not null"`
	Body  string `sql:"type:TEXT;not null"`
}

type WikiDao struct {
	db *gorm.DB
}

func init() {
	Register(&WikiEngine{})
}
