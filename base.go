package itpkg

import (
	"github.com/jinzhu/gorm"
	"time"
)

type BaseEngine struct {
	db *gorm.DB
}

func (p *BaseEngine) Mount() {

}

func (p *BaseEngine) Migrate() {
	p.db.AutoMigrate(&Setting{})
}

func (p *BaseEngine) Info() (name string, version string, desc string) {
	return "base", "v10150530", "Base framework"
}

type Model struct {
	ID      uint      `gorm:"primary_key"`
	Created time.Time `sql:"not null;DEFAULT:current_timestamp"`
}

type Setting struct {
	Key string `gorm:"column:id;primary_key"`
	Val []byte `sql:"not null"`
	Iv  []byte `sql:"size:32"`
}
