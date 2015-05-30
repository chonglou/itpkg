package itpkg

import (
	"github.com/jinzhu/gorm"
	"time"
)

type AuthEngine struct {
	db *gorm.DB
}


func (p *AuthEngine) Map() {

}

func (p *AuthEngine) Mount() {

}

func (p *AuthEngine) Migrate() {
	p.db.AutoMigrate(&Contact{})
	p.db.AutoMigrate(&User{})
	p.db.AutoMigrate(&Log{})
}

func (p *AuthEngine) Info() (name string, version string, desc string){
	return "auth", "v10250530", ""
}

type User struct {
	Model
	Name      string `sql:"not null;size:64;index"`
	Email     string `sql:"size:128;index"`
	Token     string `sql:"size:64;index"`
	Provider  string `sql:"size:16;not null;default:'local';index"`
	Password  []byte `sql:"size:64"`
	Confirmed time.Time
	Locked    time.Time
	Updated   time.Time
	Contact   Contact
	Logs      []Log
}

type Contact struct {
	Model
	Qq       string
	Skype    string
	WeChat   string
	LinkedIn string
	FaceBook string
	Email    string
	Logo     string
	Phone    string
	Tel      string
	Fax      string
	Address  string
	Details  string `sql:"type:TEXT"`
}

type Log struct {
	ID uint
	UserID  int    `sql:"not null;index"`
	Message string `sql:"size:255"`
	CreatedAt time.Time
}
