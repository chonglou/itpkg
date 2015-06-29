package email

import (
	"github.com/jinzhu/gorm"
)

type EmailDao struct {
	Db *gorm.DB `inject:""`
}
