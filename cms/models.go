package cms

import (
	. "github.com/chonglou/itpkg/base"
)

type Article struct {
	Model
	Lang     string `sql:"size:5;index;not null;default:'en'"`
	Title    string `sql:"size:255;index;not null"`
	Body     string `sql:"type:TEXT;not null"`
	Comments []Comment
}

func (p *Article) TableName() string {
	return "cms_articles"
}

type Tag struct {
	ID       uint
	Name     string    `sql:"size:32;unique_index;not null"`
	Articles []Article `gorm:"many2many:cms_tags_articles;"`
}

func (p *Tag) TableName() string {
	return "cms_tags"
}

type Comment struct {
	Model
	ArticleID uint   `sql:"index"`
	Content   string `sql:"type:TEXT;not null"`
}

func (p *Comment) TableName() string {
	return "cms_comments"
}
