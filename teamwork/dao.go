package teamwork

import (
	"github.com/jinzhu/gorm"
)

type TeamworkDao struct {
	Db *gorm.DB `inject:""`
}
