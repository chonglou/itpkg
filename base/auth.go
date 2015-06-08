package itpkg

import (
	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"
	"net/http"
	"time"
)

type AuthEngine struct {
	EngineSetup
}

func (p *AuthEngine) Map() {
	p.Use("authDao", &AuthDao{db: p.cfg.db, hmac: &Hmac{key: p.cfg.secret[120:152]}})
}

func (p *AuthEngine) Mount() {
	r := p.cfg.router

	r.POST("/users/register", func(c *gin.Context) {
		var fm UserRegisterFm
		c.Bind(&fm)
		err := p.cfg.validate.Struct(fm)
		res := NewResponse(true)
		if err == nil {
			//todo
		} else {
			res.Error(err.Error())
		}
		c.JSON(http.StatusOK, res)
	})

	r.POST("/users/login", func(c *gin.Context) {})

	r.POST("/users/unlock", func(c *gin.Context) {})
	r.GET("/users/unlock", func(c *gin.Context) {})

	r.POST("/users/password/1", func(c *gin.Context) {})
	r.GET("/users/password/1", func(c *gin.Context) {})
	r.POST("/users/password/2", func(c *gin.Context) {})

	r.POST("/users/confirm", func(c *gin.Context) {})
	r.GET("/users/confirm", func(c *gin.Context) {})

	r.GET("/users/logout", func(c *gin.Context) {})
}

func (p *AuthEngine) Migrate() {
	p.cfg.db.AutoMigrate(&Contact{})
	p.cfg.db.AutoMigrate(&User{})
	p.cfg.db.Model(&User{}).AddUniqueIndex("idx_user_login", "token", "provider")
	p.cfg.db.AutoMigrate(&Log{})
	p.cfg.db.AutoMigrate(&Role{})
	p.cfg.db.Model(&Role{}).AddUniqueIndex("idx_role_", "user_id", "name", "resource_type", "resource_id")
}

func (p *AuthEngine) Info() (name string, version string, desc string) {
	return "auth", "v10250530", ""
}

//-----------------------form---------------------------------------
type UserRegisterFm struct {
	Name       string `validate:"required" json:"name"`
	Email      string `validate:"email,required" json:"email"`
	Password   string `validate:"required,min=6,max=64" json:"password"`
	RePassword string `validate:"required" json:"re_password"`
}

type UserLoginFm struct {
	Email      string `validate:"email,required" json:"email"`
	Password   string `validate:"required" json:"password"`
	RememberMe bool   `validate:"-" json:"remember_me"`
}

type UserEmailFm struct {
	Email string `validate:"email,required" json:"email"`
}

type UserPasswordFm struct {
	Password   string `validate:"required,min=6,max=64" json:"password"`
	RePassword string `validate:"required" json:"re_password"`
}

//-----------------------model---------------------------------------
type User struct {
	Model
	Name      string `sql:"not null;size:64;index"`
	Email     string `sql:"size:128;index"`
	Token     string `sql:"size:255;index"`
	Provider  string `sql:"size:16;not null;default:'local';index"`
	Password  []byte `sql:"size:64"`
	Confirmed *time.Time
	Locked    *time.Time

	Contact Contact
	Logs    []Log
	Roles   []Role
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
	ID        uint
	UserID    uint   `sql:"not null;index"`
	Message   string `sql:"size:255"`
	Type      string `sql:"size:8;default:'info';index"`
	CreatedAt time.Time
}

type Role struct {
	Model
	UserID       uint       `sql:"index"`
	Name         string     `sql:"size:255;index;not null"`
	ResourceType string     `sql:"size:255;index"`
	ResourceID   uint       `sql:"index"`
	StartUp      *time.Time `sql:"type:DATE;default:CURRENT_DATE"`
	ShutDown     *time.Time `sql:"type:DATE;default:'9999-12-31'"`
}

//-----------------------dao---------------------------------------

type AuthDao struct {
	db   *gorm.DB
	hmac *Hmac
}

func (p *AuthDao) UserById(id uint, user *User) bool {
	return !p.db.First(user, id).RecordNotFound()
}

func (p *AuthDao) Log(user uint, message string, flag string) {
	p.db.Create(&Log{UserID: user, Message: message, Type: flag})
}

func (p *AuthDao) Check(user uint, role string, args ...interface{}) bool {
	rty, rid, _, _ := p.resource(args...)
	r := Role{}

	if p.db.Where(
		"user_id = ? AND name = ? AND resource_type = ? AND resource_id = ?",
		user, role, rty, rid).First(&r).RecordNotFound() {
		return false
	}

	now := time.Now()
	return r.StartUp.Before(now) && r.ShutDown.After(now)
}

func (p *AuthDao) Deny(user uint, role string, args ...interface{}) {
	rty, rid, _, _ := p.resource(args...)
	p.db.Where(
		"user_id = ? AND name = ? AND resource_type = ? AND resource_id = ?",
		user, role, rty, rid).Delete(Role{})
}

func (p *AuthDao) Allow(user uint, role string, args ...interface{}) {
	rty, rid, begin, end := p.resource(args...)

	r := Role{}
	if p.db.Model(Role{}).Where(
		"user_id = ? AND name = ? AND resource_type = ? AND resource_id = ?",
		user, role, rty, rid).First(&r).RecordNotFound() {

		r.UserID = user
		r.Name = role
		r.ResourceType = rty
		r.ResourceID = rid
		r.StartUp = begin
		r.ShutDown = end
		p.db.Create(&r)
	} else {
		p.db.Model(&r).Updates(map[string]interface{}{"start_up": begin, "shut_down": end})
	}

}

func (p *AuthDao) resource(args ...interface{}) (string, uint, *time.Time, *time.Time) {
	var rid uint
	var rty string
	var begin *time.Time
	var end *time.Time
	switch len(args) {
	case 0:
	case 1:
		rty = args[0].(string)
	case 2:
		rty = args[1].(string)
		rid = args[2].(uint)
	case 3:
		rty = args[1].(string)
		rid = args[2].(uint)
		begin = args[2].(*time.Time)
	case 4:
		rty = args[1].(string)
		rid = args[2].(uint)
		begin = args[2].(*time.Time)
	default:
		Logger.Warning("Ingnore role args: %v", args)
	}
	return rty, rid, begin, end
}

//-----------------------init---------------------------------------
func init() {
	Register(&AuthEngine{})
}
