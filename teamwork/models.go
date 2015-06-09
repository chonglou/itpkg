package teamwork

import (
	. "github.com/chonglou/itpkg/base"
)

type Project struct {
	VModel
	Name   string `sql:"size:255;index;not null"`
	Detail string `sql:"type:TEXT;not null"`
}

func (p *Project) TableName() string {
	return "tw_projects"
}

type Tag struct {
	Name       string `sql:"size:;index;not null"`
	ProjectUid string `sql:"size:36;index;not null"`
}

func (p *Tag) TableName() string {
	return "tw_tags"
}

type Task struct {
	VModel
	Priority   string `sql:"size:8;index;not null"`
	ProjectUid string `sql:"size:36;index;not null"`
	Name       string `sql:"size:255;index;not null"`
	Detail     string `sql:"type:TEXT;not null"`
	DateZone
}

func (p *Task) TableName() string {
	return "tw_tasks"
}

type Comment struct {
	VModel
	TaskUid string `sql:"size:36;index;not null"`
	Content string `sql:"type:TEXT;not null"`
}

func (p *Comment) TableName() string {
	return "tw_comments"
}
