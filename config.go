package itpkg

import (
	"fmt"
	jwt_lib "github.com/dgrijalva/jwt-go"
	"github.com/gin-gonic/contrib/cache"
	"github.com/gin-gonic/contrib/jwt"
	"github.com/gin-gonic/contrib/sessions"
	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"
	"gopkg.in/yaml.v2"
	"io/ioutil"
	"os"
	"strconv"
	"time"
)

type Config struct {
	secret []byte
	Secret string
	Cache  struct {
		Store string
	}
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

func (p *Config) Mailer() *Mailer {
	m := Mailer{}
	m.Auth(
		p.Smtp.From,
		p.Smtp.Host,
		p.Smtp.Port,
		p.Smtp.Ssl,
		p.Smtp.Username,
		p.Smtp.Password,
		p.Smtp.Bcc)
	return &m
}

func (p *Config) Db() (*gorm.DB, error) {

	db, err := gorm.Open(p.Database.Driver, p.DbUrl())
	if err != nil {
		return nil, err
	}
	db.LogMode(!IsProduction())
	if err = db.DB().Ping(); err != nil {
		return nil, err
	}
	return &db, nil
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

func (p *Config) Token(user uint) (string, error) {
	token := jwt_lib.New(jwt_lib.GetSigningMethod("HS256"))
	// Set some claims
	token.Claims["ID"] = user
	token.Claims["exp"] = time.Now().Add(time.Hour * 1).Unix()

	return token.SignedString(p.authPassword())

}

func (p *Config) Auth() gin.HandlerFunc {
	return jwt.Auth(string(p.authPassword()))
}

func (p *Config) authPassword() []byte {
	return p.secret[220:252]
}

func (p *Config) CacheStore() cache.CacheStore {
	de := time.Second
	switch p.Cache.Store {
	case "memory":
		return cache.NewInMemoryStore(de)
	case "redis":
		return cache.NewRedisCache(p.RedisUrl(), "", de)
	default:
		log.Fatalf("Unknown cache store: %s", p.Cache.Store)
	}
	return nil
}

func (p *Config) SessionStore() sessions.Store {
	key, iv := p.secret[100:164], p.secret[170:202]
	switch p.Session.Store {
	case "redis":
		s, e := sessions.NewRedisStore(p.Session.Pool, "tcp", p.RedisUrl(), "", key, iv)
		if e != nil {
			log.Fatalf("Error on open redis session: %v", e)
		}
		return s
	case "cookie":
		return sessions.NewCookieStore(key, iv)
	default:
		log.Fatalf("Unknown session store: %s", p.Session.Store)
	}
	return nil
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

		cfg.Http.Host = "http://localhost"
		cfg.Http.Port = 3000
		cfg.Http.Cookie = RandomStr(8)
		cfg.Http.Expire = 60 * 30

		cfg.Cache.Store = "redis" // can be cookie or memory
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
