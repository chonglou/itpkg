package cms

import (
	"github.com/jinzhu/gorm"
)

type CmsDao struct {
	Db *gorm.DB `inject:""`
}
