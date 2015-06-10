package shop

import (
	"github.com/jinzhu/gorm"
)

type ShopDao struct {
	db *gorm.DB
}
