package itpkg

import (
	"crypto/tls"
	"fmt"
	"io/ioutil"
	"log"
	"os"
	"strconv"
	"time"

	"github.com/garyburd/redigo/redis"
	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"
	"gopkg.in/gomail.v1"
	"gopkg.in/yaml.v2"
)

type Configuration struct {
	secret []byte
	env    string

	Secret  string
	Session struct {
		Store string
	}
	Cache struct {
		Store string
	}
	Http struct {
		Host   string
		Port   int
		Cookie string
		Expire int
	}
	Database struct {
		Driver   string
		Host     string
		Port     int
		User     string
		Password string
		Name     string
		Extra    string
	}
	Redis struct {
		Host string
		Port int
		Db   int
		Pool int
	}
	Google struct {
		Id     string
		Secret string
	}
	Smtp struct {
		From     string
		Host     string
		Port     int
		Ssl      bool
		Username string
		Password string
		Bcc      string
	}
}

func (p *Configuration) IsProduction() bool {
	return p.env == "production"
}

func (p *Configuration) OpenMailer() *gomail.Mailer {
	if p.Smtp.Ssl {
		return gomail.NewMailer(
			p.Smtp.Host,
			p.Smtp.Username,
			p.Smtp.Password,
			p.Smtp.Port,
			gomail.SetTLSConfig(&tls.Config{InsecureSkipVerify: true}))
	}
	return gomail.NewMailer(
		p.Smtp.Host,
		p.Smtp.Username,
		p.Smtp.Password,
		p.Smtp.Port)
}

func (p *Configuration) OpenRouter() (*gin.Engine, error) {

	if p.IsProduction() {
		gin.SetMode(gin.ReleaseMode)
	}
	rt := gin.Default()

	if !p.IsProduction() {
		rt.Static("/assets", "public")
	}

	return rt, nil
}

func (p *Configuration) OpenDb() (*gorm.DB, error) {

	db, err := gorm.Open(p.Database.Driver, p.DbUrl())
	if err != nil {
		return nil, err
	}
	db.LogMode(!p.IsProduction())
	if err = db.DB().Ping(); err != nil {
		return nil, err
	}
	return &db, nil
}

func (p *Configuration) DbUrl() string {
	return fmt.Sprintf(
		"%s://%s:%s@%s:%d/%s?%s",
		p.Database.Driver, p.Database.User, p.Database.Password, p.Database.Host,
		p.Database.Port, p.Database.Name, p.Database.Extra)
}

func (p *Configuration) DbCreate() (string, []string) {
	d := p.Database.Driver
	switch d {
	case "postgres":
		return "psql", []string{
			"-h", p.Database.Host,
			"-p", strconv.Itoa(p.Database.Port),
			"-U", p.Database.User,
			"-c", fmt.Sprintf("CREATE DATABASE %s", p.Database.Name)}
	default:
		return "echo", []string{"Unknown database driver " + d}
	}
}

func (p *Configuration) DbDrop() (string, []string) {
	d := p.Database.Driver
	switch d {
	case "postgres":
		return "psql", []string{
			"-h", p.Database.Host,
			"-p", strconv.Itoa(p.Database.Port),
			"-U", p.Database.User,
			"-c", fmt.Sprintf("DROP DATABASE %s", p.Database.Name)}
	default:
		return "echo", []string{"Unknown database driver " + d}
	}
}

func (p *Configuration) DbShell() (string, []string) {
	d := p.Database.Driver
	switch d {
	case "postgres":
		return "psql", []string{
			"-h", p.Database.Host,
			"-p", strconv.Itoa(p.Database.Port),
			"-d", p.Database.Name,
			"-U", p.Database.User}
	default:
		return "echo", []string{"Unknown database driver " + d}
	}
}

func (p *Configuration) OpenRedis() *redis.Pool {
	return &redis.Pool{
		MaxIdle:     3,
		IdleTimeout: 4 * 60 * time.Second,
		Dial: func() (redis.Conn, error) {
			c, err := redis.Dial("tcp", p.RedisUrl())
			if err != nil {
				return nil, err
			}
			if _, err = c.Do("SELECT", p.Redis.Db); err != nil {
				return nil, err
			}
			return c, err
		},
		TestOnBorrow: func(c redis.Conn, t time.Time) error {
			_, err := c.Do("PING")
			return err
		},
	}
}

func (p *Configuration) RedisUrl() string {
	return fmt.Sprintf("%s:%d", p.Redis.Host, p.Redis.Port)
}

func (p *Configuration) RedisShell() (string, []string) {
	return "telnet", []string{p.Redis.Host, strconv.Itoa(p.Redis.Port)}
}

//----------------------------------------------------------------------------

func LoadConfig(cfg *Configuration, file string) error {
	_, err := os.Stat(file)

	if err == nil {
		var yml []byte
		yml, err = ioutil.ReadFile(file)
		if err != nil {
			return err
		}
		log.Printf("Load from config file: %s", file)
		if err = yaml.Unmarshal(yml, cfg); err != nil {
			return err
		}
		cfg.secret, err = Base64Decode([]byte(cfg.Secret))

	} else {

		cfg.secret, err = RandomBytes(512)
		if err != nil {
			return err
		}

		cfg.Secret = string(Base64Encode(cfg.secret))

		cfg.Http.Host = "CHANGE ME"
		cfg.Http.Port = 3000
		cfg.Http.Cookie = RandomStr(8)
		cfg.Http.Expire = 60 * 30

		cfg.Cache.Store = "redis"   // can be memory or redis
		cfg.Session.Store = "redis" // can be cookie or redis

		cfg.Database.Driver = "postgres"
		cfg.Database.Host = "localhost"
		cfg.Database.Port = 5432
		cfg.Database.User = "postgres"
		cfg.Database.Password = ""
		cfg.Database.Name = fmt.Sprintf("itpkg_%s", cfg.env)
		cfg.Database.Extra = "sslmode=disable"

		cfg.Redis.Host = "localhost"
		cfg.Redis.Port = 6379
		cfg.Redis.Db = 0
		cfg.Redis.Pool = 12

		cfg.Google.Id = "CHANGE ME"
		cfg.Google.Secret = "CHANGE ME"

		cfg.Smtp.Host = "CHANGE ME"
		cfg.Smtp.Port = 25
		cfg.Smtp.Username = "CHANGE ME"
		cfg.Smtp.Password = "CHANGE ME"

		var data []byte
		data, err = yaml.Marshal(cfg)
		if err != nil {
			return err
		}
		log.Printf("Generate config file: %s", file)
		err = ioutil.WriteFile(file, data, 0600)
	}
	return err
}
