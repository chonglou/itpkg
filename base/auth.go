package itpkg

import (
	"github.com/jinzhu/gorm"
	"net/http"
	"regexp"
	"time"
)

var rxpUserEmail = regexp.MustCompile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")
var rxpUserName = regexp.MustCompile("\\w{1,64}")
var rxpUserPassword = regexp.MustCompile(".{6,128}")

//
// func CurrentUser(ss sessions.Session, dao *AuthDao, user *User) bool {
// 	uid := ss.Get("uid")
// 	if uid == nil {
// 		return false
// 	}
//
// 	return dao.UserById(uid.(uint), user)
// }

type AuthEngine struct {
	cfg *Config
}

func (p *AuthEngine) Map() {
	p.cfg.Use("authDao", &AuthDao{db: p.cfg.db, hmac: &Hmac{key: p.cfg.secret[120:152]}})
}

func (p *AuthEngine) Mount() {
	// p.app.Post(
	// 	"/users/register",
	// 	binding.Bind(UserRegisterFm{}),
	// 	func(fm UserRegisterFm, r render.Render, dao *AuthDao, mailer *Mailer) {
	// 		//go mailer.Text([]string{"2682287010@qq.com"}, "test", "body")
	// 		r.JSON(200, map[string]interface{}{"fm": fm})
	// 	})
	// p.app.Post("/users/login", func() {})
	// p.app.Get("/users/unlock", func() {})
	// p.app.Post("/users/password/1", func() {})
	// p.app.Post("/users/password/2", func() {})
	// p.app.Post("/users/confirm", func() {})
	// p.app.Get("/users/logout", func(req *http.Request, r render.Render, ss sessions.Session, dao *AuthDao) {
	// 	// u := User{}
	// 	// if CurrentUser(ss, dao, &u) {
	// 	// 	ss.Clear()
	// 	// 	dao.Log(u.ID, T(Lang(req), "auth.log.logout"), "")
	// 	// 	r.JSON(200, NewMessage(false))
	// 	// } else {
	// 	// 	r.JSON(200, NewMessage(false))
	// 	// }
	// })
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

type UserRegisterFm struct {
	Name       string
	Email      string
	Password   string
	RePassword string
}

func (p UserRegisterFm) Validate(req *http.Request) []error {
	return nil
}

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

func init() {
	Register("auth")
}
