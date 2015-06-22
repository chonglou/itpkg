package forum

import (
	"github.com/jinzhu/gorm"
)

type ForumDao struct {
	Db *gorm.DB `inject:""`
}
