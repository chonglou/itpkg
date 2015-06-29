package email

import (
	itpkg "github.com/chonglou/itpkg/base"
)

type User struct {
	itpkg.Model
	Email    string `sql:"size:255;index;not null"`
	Password []byte `sql:"not null"`
	Iv       []byte `sql:"size:32;not null"`

	Boxes []Box
}

func (p *User) TableName() string {
	return "email_users"
}

type Box struct {
	itpkg.Model
	UserID   uint   `sql:"not null;index"`
	Name     string `sql:"size:32;unique_index;not null"`
	Messages []Message
}

func (p *Box) TableName() string {
	return "email_boxs"
}

type Message struct {
	itpkg.Model
	BoxID  uint   `sql:"not null;index"`
	Header []byte `sql:"not null"`
	Body   []byte `sql:"not null"`
}

func (p *Message) TableName() string {
	return "email_messages"
}
