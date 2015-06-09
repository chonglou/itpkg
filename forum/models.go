package forum

import (
	. "github.com/chonglou/itpkg/base"
)

type Article struct {
	Model
	AuthorID uint   `sql:"index;not null"`
	Title    string `sql:"size:255;index;not null"`
	Body     string `sql:"type:TEXT;not null"`
	Comments []Comment
}

func (p *Article) TableName() string {
	return "forum_articles"
}

type Board struct {
	ID       uint
	Lang     string    `sql:"size:5;not null;default:'en'"`
	Name     string    `sql:"size:32;not null"`
	Articles []Article `gorm:"many2many:forum_boards_articles;"`
}

func (p *Board) TableName() string {
	return "forum_boards"
}

type Comment struct {
	Model
	AuthorID  uint   `sql:"index;not null"`
	ArticleID uint   `sql:"index;not null"`
	Content   string `sql:"type:TEXT;not null"`
}

func (p *Comment) TableName() string {
	return "forum_comments"
}
