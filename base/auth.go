package itpkg

import (
	"errors"
	"fmt"
	"net/http"
	"strings"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"
	"github.com/op/go-logging"
	"github.com/pborman/uuid"
	"gopkg.in/bluesuncorp/validator.v5"
)

type AuthEngine struct {
	Cfg      *Configuration      `inject:""`
	AuthDao  *AuthDao            `inject:""`
	Db       *gorm.DB            `inject:""`
	SiteDao  *SiteDao            `inject:""`
	I18n     *LocaleDao          `inject:""`
	Router   *gin.Engine         `inject:""`
	Logger   *logging.Logger     `inject:""`
	Mailer   *Mailer             `inject:""`
	Token    *Token              `inject:""`
	Validate *validator.Validate `inject:""`
}

func (p *AuthEngine) Mount() {

	p.Router.POST("/users/sign_up", func(c *gin.Context) {
		lang := LANG(c)

		var fm UserRegisterFm
		c.Bind(&fm)
		p.Logger.Debug("========== Form: %v", fm)
		err := p.Validate.Struct(fm)
		res := NewResponse(true)
		if err == nil {
			if fm.Password != fm.RePassword {
				res.Invalid(errors.New(p.I18n.T(lang, "auth.error.passwords_not_match")))
			} else {
				user, err := p.AuthDao.AddEmailUser(fm.Email, fm.Name, fm.Password)
				if err == nil {
					p.AuthDao.Log(user.ID, p.I18n.T(lang, "auth.log.sign_up"), "")
					go p.mail(lang, user.Email, "confirm")
					res.Message(p.I18n.T(lang, "auth.message.sign_up.success"))
				} else {
					res.Invalid(err)
				}
			}

		} else {
			res.Invalid(err)
		}
		c.JSON(http.StatusOK, res)
	})

	p.Router.POST("/users/login", func(c *gin.Context) {})

	p.Router.POST("/users/unlock", func(c *gin.Context) {
		res := NewResponse(true)
		c.JSON(http.StatusOK, res)
	})
	p.Router.GET("/users/unlock", func(c *gin.Context) {})

	p.Router.POST("/users/password/1", func(c *gin.Context) {})
	p.Router.GET("/users/password/1", func(c *gin.Context) {})
	p.Router.POST("/users/password/2", func(c *gin.Context) {})

	p.Router.POST("/users/confirm", func(c *gin.Context) {})
	p.Router.GET("/users/confirm", func(c *gin.Context) {})

	p.Router.GET("/users/logout", func(c *gin.Context) {})
}

func (p *AuthEngine) Migrate() {
	p.Db.AutoMigrate(&Contact{})
	p.Db.AutoMigrate(&User{})
	p.Db.Model(&User{}).AddUniqueIndex("idx_users_login", "token", "provider")
	p.Db.AutoMigrate(&Log{})
	p.Db.AutoMigrate(&Role{})
	p.Db.Model(&Role{}).AddUniqueIndex("idx_roles_", "user_id", "name", "resource_type", "resource_id")
}

func (p *AuthEngine) Info() (name string, version string, desc string) {
	return "auth", "v10250530", "auth engine"
}

func (p *AuthEngine) mail(lang, email string, act string) {
	switch {
	case act == "password" || act == "confirm" || act == "unlock":
		tk, err := p.Token.New(&emailToken{Email: email, Action: act}, time.Hour*24)
		if err != nil {
			p.Logger.Error("Error on generate user token: %s", act)
		} else {
			url := p.Cfg.Url(lang, "/users/"+act)
			url += "&token=" + tk
			p.Logger.Debug("Url: %s", url)
			p.Mailer.Html(
				p.Cfg.From("no-reply"),
				p.Cfg.Smtp.Bcc,
				[]string{email},
				p.I18n.T(lang, "auth.mailer."+act+".subject"),
				p.I18n.T(lang, "auth.mailer."+act+".body", email, url, url))
		}

	default:
		p.Logger.Error("Unknown user email action: %s", act)
	}
}

type emailToken struct {
	Email  string `json:"email"`
	Action string `json:"action"`
}

type UserToken struct {
	ID     uint   `json:"id"`
	Email  string `json:"email"`
	Name   string `json:"name"`
	Logo   string `json:"logo"`
	Action string `json:"action"`
}

func (p *UserToken) Avatar() string {
	if p.Logo == "" {
		return p.Logo
	}
	return fmt.Sprintf(
		"http://www.gravatar.com/avatar/%v",
		Md5([]byte(strings.ToLower(strings.Trim(p.Logo, " ")))))
}

//-----------------------form---------------------------------------
type UserRegisterFm struct {
	Name       string `validate:"required" form:"name" binding:"required"`
	Email      string `validate:"email,required" form:"email" binding:"required"`
	Password   string `validate:"required,min=6,max=64" form:"password" binding:"required"`
	RePassword string `validate:"required" form:"re_password" binding:"required"`
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
	Token     string `sql:"size:255;index;not null"`
	Provider  string `sql:"size:16;not null;default:'email';index"`
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
	UserID       uint   `sql:"index"`
	Name         string `sql:"size:255;index;not null"`
	ResourceType string `sql:"size:255;index"`
	ResourceID   uint   `sql:"index"`
	DateZone
}

//-----------------------dao---------------------------------------

type AuthDao struct {
	Db     *gorm.DB        `inject:""`
	Hmac   *Hmac           `inject:""`
	Logger *logging.Logger `inject:""`
}

func (p *AuthDao) AddEmailUser(email, name, password string) (*User, error) {
	var c int
	p.Db.Model(User{}).Where("email = ? AND provider = ?", email, "email").Count(&c)
	if c > 0 {
		return nil, errors.New("email already exist")
	}
	u := User{
		Provider: "local",
		Name:     name,
		Email:    email,
		Password: p.Hmac.Sum([]byte(password)),
		Token:    uuid.New()}
	p.Db.Create(&u)
	return &u, nil
}

func (p *AuthDao) ConfirmUser(id uint) {
	now := time.Now()
	p.Db.Model(User{}).Where("id = ?", id).Updates(User{Confirmed: &now})
}

func (p *AuthDao) UserById(id uint, user *User) bool {
	return !p.Db.First(user, id).RecordNotFound()
}

func (p *AuthDao) Log(user uint, message string, flag string) {
	p.Db.Create(&Log{UserID: user, Message: message, Type: flag})
}

func (p *AuthDao) Check(user uint, role string, args ...interface{}) bool {
	rty, rid, _, _ := p.resource(args...)
	r := Role{}

	if p.Db.Where(
		"user_id = ? AND name = ? AND resource_type = ? AND resource_id = ?",
		user, role, rty, rid).First(&r).RecordNotFound() {
		return false
	}

	now := time.Now()
	return r.StartUp.Before(now) && r.ShutDown.After(now)
}

func (p *AuthDao) Deny(user uint, role string, args ...interface{}) {
	rty, rid, _, _ := p.resource(args...)
	p.Db.Where(
		"user_id = ? AND name = ? AND resource_type = ? AND resource_id = ?",
		user, role, rty, rid).Delete(Role{})
}

func (p *AuthDao) Allow(user uint, role string, args ...interface{}) {
	rty, rid, begin, end := p.resource(args...)

	r := Role{}
	if p.Db.Model(Role{}).Where(
		"user_id = ? AND name = ? AND resource_type = ? AND resource_id = ?",
		user, role, rty, rid).First(&r).RecordNotFound() {

		r.UserID = user
		r.Name = role
		r.ResourceType = rty
		r.ResourceID = rid
		r.StartUp = begin
		r.ShutDown = end
		p.Db.Create(&r)
	} else {
		p.Db.Model(&r).Updates(map[string]interface{}{"start_up": begin, "shut_down": end})
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
		p.Logger.Warning("Ingnore role args: %v", args)
	}
	return rty, rid, begin, end
}

//-----------------------init---------------------------------------
func init() {
	Register(&AuthEngine{})
}
