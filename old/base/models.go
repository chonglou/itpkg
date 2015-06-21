package itpkg

import (
	"time"
)

type Model struct {
	ID        uint `gorm:"primary_key"`
	UpdatedAt time.Time
	CreatedAt time.Time
}

type VModel struct {
	Uid       string `sql:"index;site:36;not null"`
	Ver       uint   `sql:"default:0;not null"`
	CreatedAt time.Time
}

type DateZone struct {
	StartUp  *time.Time `sql:"type:DATE;default:CURRENT_DATE"`
	ShutDown *time.Time `sql:"type:DATE;default:'9999-12-31'"`
}
