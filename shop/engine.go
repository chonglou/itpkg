package shop

import (
	"github.com/chonglou/gin-contrib/rest"
	. "github.com/chonglou/itpkg/base"
	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"
)

type ShopEngine struct {
	EngineSetup
	dao *ShopDao
}

func (p *ShopEngine) Map() {
	dao := &ShopDao{db: p.Get("db").(*gorm.DB)}
	p.Use("shopDao", dao)
	p.dao = dao
}

func (p *ShopEngine) Mount() {
	g := p.Get("router").(*gin.Engine).Group("/shop")
	rest.CRUD(g, "/product", &ProductCtrl{})
	rest.CRUD(g, "/tag", &TagCtrl{})
	rest.CRUD(g, "/price", &PriceCtrl{})
	rest.CRUD(g, "/order", &OrderCtrl{})
	rest.CRUD(g, "/comment", &CommentCtrl{})
	rest.CRUD(g, "/delivery", &DeliveryCtrl{})
}

func (p *ShopEngine) Migrate() {
	db := p.Get("db").(*gorm.DB)
	db.AutoMigrate(&Product{})
	db.AutoMigrate(&Tag{})
	db.AutoMigrate(&Price{})
	db.Model(&Price{}).AddUniqueIndex("idx_shop_prices_type_tid_name", "type", "tid", "name")
	db.AutoMigrate(&Order{})
	db.Model(&Order{}).AddUniqueIndex("idx_shop_orders_uid_ver", "uid", "ver")
	db.AutoMigrate(&Comment{})
	db.AutoMigrate(&Delivery{})

}

func (p *ShopEngine) Info() (name string, version string, desc string) {
	return "shop", "v102500609", ""
}

func init() {
	Register(&ShopEngine{})
}
