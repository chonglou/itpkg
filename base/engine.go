package itpkg

import (
	"fmt"

	"github.com/chonglou/gin-contrib/cache"
	"github.com/garyburd/redigo/redis"
	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"
	"github.com/op/go-logging"
)

type Engine interface {
	Mount()
	Migrate()
	Info() (name string, version string, desc string)
}

type BaseEngine struct {
	LocaleDao *LocaleDao       `inject:""`
	Logger    *logging.Logger  `inject:""`
	Router    *gin.Engine      `inject:""`
	Redis     *redis.Pool      `inject:""`
	Db        *gorm.DB         `inject:""`
	Cache     cache.CacheStore `inject:""`
}

func (p *BaseEngine) T(lang, key string, args ...interface{}) string {
	v := p.LocaleDao.Get(lang, key)
	if v == "" {
		return fmt.Sprintf("Translation [%s] not found", key)
	}
	return fmt.Sprintf(v, args...)
}
