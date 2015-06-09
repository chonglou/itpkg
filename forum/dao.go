package forum

import (
	"github.com/jinzhu/gorm"
)

type ForumDao struct {
	db *gorm.DB
}
