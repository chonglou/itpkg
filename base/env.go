package itpkg

import (
	"crypto/aes"
	"crypto/cipher"
	"fmt"
	"log"
	"os"
	"time"

	"github.com/chonglou/gin-contrib/cache"
	"github.com/facebookgo/inject"
	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"
	"github.com/op/go-logging"
)

var beans inject.Graph

func RegisterWithName(objects map[string]interface{}) {
	items := make([]*inject.Object, 0)
	for k, v := range objects {
		items = append(items, &inject.Object{Value: v, Name: k})
	}
	if err := beans.Provide(items...); err != nil {
		log.Fatalf("error on register: %v", err)
	}
}

func Register(objects ...interface{}) {
	items := make([]*inject.Object, 0)
	for _, v := range objects {
		items = append(items, &inject.Object{Value: v})
	}
	if err := beans.Provide(items...); err != nil {
		log.Fatalf("error on register: %v", err)
	}
}

func configByEnv(env string) *Configuration {
	var err error
	if err = os.MkdirAll("config", 0700); err != nil {
		log.Fatalf("config directory not exists!")
	}

	cfg := Configuration{env: env}
	if err = LoadConfig(&cfg, fmt.Sprintf("config/%s.yml", env)); err != nil {
		log.Fatalf("error on load config: %v", err)
	}
	return &cfg
}

func New(env string) *Application {
	return &Application{engines: make([]Engine, 0), Cfg: configByEnv(env)}
}

func Load(env string, web bool) *Application {
	var err error
	logger := logging.MustGetLogger("itpkg")
	app := Application{engines: make([]Engine, 0)}
	cfg := configByEnv(env)

	var db *gorm.DB
	if db, err = cfg.OpenDb(); err != nil {
		log.Fatalf("error on open database: %v", err)
	}

	//aes
	var cip cipher.Block
	if cip, err = aes.NewCipher(cfg.secret[120:152]); err != nil {
		log.Fatalf("error on generate aes cipher: %v", err)
	}

	//redis
	redis := cfg.OpenRedis()

	//cache
	var cacheS cache.CacheStore
	switch cfg.Cache.Store {
	case "memory":
		cacheS = cache.NewInMemoryStore(time.Second)
	case "redis":
		cacheS = cache.NewRedisCacheWithPool(redis, time.Second)
	default:
		log.Fatalf("Unknown cache store: %s", cfg.Cache.Store)
	}
	logger.Info("Using cache by %s", cfg.Cache.Store)

	//register
	Register(
		logger,
		&app,
		cfg,
		redis,
		db,
		cfg.OpenMailer(),
		cacheS,
	)
	RegisterWithName(map[string]interface{}{
		"token key":   cfg.secret[100:116],
		"aes cipher":  cip,
		"production?": cfg.IsProduction(),
		"hmac key":    cfg.secret[160:192]})

	if web {
		var router *gin.Engine
		if router, err = cfg.OpenRouter(); err != nil {
			log.Fatalf("error on open router: %v", err)
		}
		Register(router)
	}

	if err = beans.Populate(); err != nil {
		log.Fatalf("error on init beans: %v", err)
	}
	return &app
}

func init() {
	//cache.PageCachePrefix = "cache://page/"
}
