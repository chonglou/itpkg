package cms

import (
	"github.com/jinzhu/gorm"
)

type CmsDao struct {
	db *gorm.DB
}
