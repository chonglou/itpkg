package shop

import (
	"github.com/jinzhu/gorm"
)

type ShopDao struct {
	Db *gorm.DB `inject:""`
}
