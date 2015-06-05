package itpkg

import (
	"errors"
	"fmt"
	"github.com/garyburd/redigo/redis"
	"github.com/gorilla/sessions"
	"github.com/jinzhu/gorm"
	"gopkg.in/boj/redistore.v1"
	"gopkg.in/yaml.v2"
	"io/ioutil"
	"os"
	"strconv"
	"time"
)

type Config struct {
	db      *gorm.DB
	redis   *redis.Pool
	mailer  *Mailer
	session sessions.Store
	env     string
	secret  []byte
	beans   map[string]interface{}

	Secret  string
	Session struct {
		Store string
		Pool  int
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
		Ssl      string
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

func (p *Config) Use(name string, val interface{}) {
	p.beans[name] = val
}

func (p *Config) Get(name string) interface{} {
	return p.beans[name]
}

func (p *Config) OpenMailer() {
	m := Mailer{}
	m.Auth(
		p.Smtp.From,
		p.Smtp.Host,
		p.Smtp.Port,
		p.Smtp.Ssl,
		p.Smtp.Username,
		p.Smtp.Password,
		p.Smtp.Bcc)
	p.mailer = &m
}

func (p *Config) IsProduction() bool {
	return p.env == "production"
}

func (p *Config) OpenDb() error {

	db, err := gorm.Open(p.Database.Driver, p.DbUrl())
	if err != nil {
		return err
	}
	db.LogMode(!p.IsProduction())
	if err = db.DB().Ping(); err != nil {
		return err
	}
	p.db = &db
	return nil
}

func (p *Config) DbUrl() string {
	return fmt.Sprintf(
		"%s://%s:%s@%s:%d/%s?sslmode=%s",
		p.Database.Driver, p.Database.User, p.Database.Password, p.Database.Host,
		p.Database.Port, p.Database.Name, p.Database.Ssl)
}

func (p *Config) DbShell() (string, []string) {
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

func (p *Config) Token() Token {
	return Token{key: p.secret[220:252]}
}

func (p *Config) OpenSession() error {
	key, iv := p.secret[100:164], p.secret[170:202]
	switch p.Session.Store {
	case "redis":
		//s, e := redistore.NewRediStore(p.Session.Pool, "tcp", p.RedisUrl(), "", key, iv)
		s, e := redistore.NewRediStoreWithPool(p.redis, key, iv)
		if e != nil {
			return e
		}
		p.session = s
	case "cookie":
		p.session = sessions.NewCookieStore(key, iv)
	default:
		return errors.New(fmt.Sprintf("Unknown session store: %s", p.Session.Store))
	}
	return nil
}

func (p *Config) OpenRedis() {
	p.redis = &redis.Pool{
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

func (p *Config) RedisUrl() string {
	return fmt.Sprintf("%s:%d", p.Redis.Host, p.Redis.Port)
}

func (p *Config) RedisShell() (string, []string) {
	return "telnet", []string{p.Redis.Host, strconv.Itoa(p.Redis.Port)}
}

func loadConfig(cfg *Config, file string) error {
	_, err := os.Stat(file)

	if err == nil {
		var yml []byte
		yml, err = ioutil.ReadFile(file)
		if err != nil {
			return err
		}
		log.Info("Load from config file: %s", file)
		if err = yaml.Unmarshal(yml, cfg); err != nil {
			return err
		}
		cfg.secret, err = Base64Decode([]byte(cfg.Secret))

	} else {

		cfg.secret = RandomBytes(512)
		cfg.Secret = string(Base64Encode(cfg.secret))

		cfg.Http.Host = "CHANGE ME"
		cfg.Http.Port = 3000
		cfg.Http.Cookie = RandomStr(8)
		cfg.Http.Expire = 60 * 30

		cfg.Session.Pool = 6
		cfg.Session.Store = "redis" // can be cookie or redis

		cfg.Database.Driver = "postgres"
		cfg.Database.Host = "localhost"
		cfg.Database.Port = 5432
		cfg.Database.User = "postgres"
		cfg.Database.Password = ""
		cfg.Database.Name = "itpkg"
		cfg.Database.Ssl = "disable"

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

		if err = os.MkdirAll("config", 0700); err != nil {
			return err
		}

		var data []byte
		data, err = yaml.Marshal(cfg)
		if err != nil {
			return err
		}
		log.Info("Generate config file: %s", file)
		err = ioutil.WriteFile(file, data, 0600)
	}
	return err
}
