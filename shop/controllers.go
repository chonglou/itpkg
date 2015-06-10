package shop

import (
	"github.com/gin-gonic/gin"
)

type ProductCtrl struct {
}

func (p *ProductCtrl) CreateHandler(*gin.Context) {}
func (p *ProductCtrl) ListHandler(*gin.Context)   {}
func (p *ProductCtrl) TakeHandler(*gin.Context)   {}
func (p *ProductCtrl) UpdateHandler(*gin.Context) {}
func (p *ProductCtrl) DeleteHandler(*gin.Context) {}

type TagCtrl struct {
}

func (p *TagCtrl) CreateHandler(*gin.Context) {}
func (p *TagCtrl) ListHandler(*gin.Context)   {}
func (p *TagCtrl) TakeHandler(*gin.Context)   {}
func (p *TagCtrl) UpdateHandler(*gin.Context) {}
func (p *TagCtrl) DeleteHandler(*gin.Context) {}

type PriceCtrl struct {
}

func (p *PriceCtrl) CreateHandler(*gin.Context) {}
func (p *PriceCtrl) ListHandler(*gin.Context)   {}
func (p *PriceCtrl) TakeHandler(*gin.Context)   {}
func (p *PriceCtrl) UpdateHandler(*gin.Context) {}
func (p *PriceCtrl) DeleteHandler(*gin.Context) {}

type CommentCtrl struct {
}

func (p *CommentCtrl) CreateHandler(*gin.Context) {}
func (p *CommentCtrl) ListHandler(*gin.Context)   {}
func (p *CommentCtrl) TakeHandler(*gin.Context)   {}
func (p *CommentCtrl) UpdateHandler(*gin.Context) {}
func (p *CommentCtrl) DeleteHandler(*gin.Context) {}

type OrderCtrl struct {
}

func (p *OrderCtrl) CreateHandler(*gin.Context) {}
func (p *OrderCtrl) ListHandler(*gin.Context)   {}
func (p *OrderCtrl) TakeHandler(*gin.Context)   {}
func (p *OrderCtrl) UpdateHandler(*gin.Context) {}
func (p *OrderCtrl) DeleteHandler(*gin.Context) {}

type DeliveryCtrl struct {
}

func (p *DeliveryCtrl) CreateHandler(*gin.Context) {}
func (p *DeliveryCtrl) ListHandler(*gin.Context)   {}
func (p *DeliveryCtrl) TakeHandler(*gin.Context)   {}
func (p *DeliveryCtrl) UpdateHandler(*gin.Context) {}
func (p *DeliveryCtrl) DeleteHandler(*gin.Context) {}
