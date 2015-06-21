package itpkg

import (
	"fmt"
	"log"

	"github.com/facebookgo/inject"
	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"
	"github.com/op/go-logging"
)

var beans inject.Graph

func Register(objects ...interface{}) {
	items := make([]*inject.Object, len(objects))
	for i, o := range objects {
		items[i] = &inject.Object{Value: o}
	}
	if err := beans.Provide(items...); err != nil {
		log.Fatalf("error on register: %v", err)
	}
}

func New(env string) *Application {
	var err error
	cfg := Configuration{env: env}
	if err = LoadConfig(&cfg, fmt.Sprintf("config/%s.yml", env)); err != nil {
		log.Fatalf("error on load config: %v", err)
	}
	return &Application{engines: make([]Engine, 0), Cfg: &cfg}
}

func Load(env string, web bool) *Application {
	var err error
	logger := logging.MustGetLogger("itpkg")
	app := Application{engines: make([]Engine, 0)}
	cfg := Configuration{env: env}
	if err = LoadConfig(&cfg, fmt.Sprintf("config/%s.yml", env)); err != nil {
		log.Fatalf("error on load config: %v", err)
	}

	var db *gorm.DB
	if db, err = cfg.OpenDb(); err != nil {
		log.Fatalf("error on open database: %v", err)
	}

	Register(logger, &app, &cfg, cfg.OpenRedis(), db)

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
