package shop

import (
	. "github.com/chonglou/itpkg/base"
)

type Product struct {
	Model
	Lang     string `sql:"size:5;index;not null;default:'en'"`
	Title    string `sql:"size:255;index;not null"`
	Body     string `sql:"type:TEXT;not null"`
	Prices   []Price
	Comments []Comment
}

func (p *Product) TableName() string {
	return "shop_products"
}

type Price struct {
	Type string  `sql:"site:16;index;not null"`
	Tid  uint    `sql:"index;not null"`
	Name string  `sql:"size:8;index;not null"`
	Val  float64 `sql:"not null"`
}

func (p *Price) TableName() string {
	return "shop_prices"
}

type Tag struct {
	ID       uint
	Name     string    `sql:"size:32;unique_index;not null"`
	Products []Product `gorm:"many2many:shop_tags_articles;"`
}

func (p *Tag) TableName() string {
	return "shop_tags"
}

type Comment struct {
	Model
	ProductID uint   `sql:"index;not null"`
	Content   string `sql:"type:TEXT;not null"`
}

func (p *Comment) TableName() string {
	return "shop_comments"
}

type Delivery struct {
	Model
	Name   string `sql:"size:255;unique_index;not null"`
	Prices []Price
}

func (p *Delivery) TableName() string {
	return "shop_deliveries"
}

type Order struct {
	VModel

	UserID   uint   `sql:"index;not null"`
	Address  string `sql:"size:255;unique_index;not null"`
	Snapshot string `sql:"type:TEXT;not null"`
	Status   string `sql:"size:16;default:'submit';not null"`
}

func (p *Order) TableName() string {
	return "shop_orders"
}
