package email

import (
	"github.com/gin-gonic/gin"
)

type MessageCtrl struct {
}

func (p *MessageCtrl) CreateHandler(*gin.Context) {}
func (p *MessageCtrl) ListHandler(*gin.Context)   {}
func (p *MessageCtrl) TakeHandler(*gin.Context)   {}
func (p *MessageCtrl) UpdateHandler(*gin.Context) {}
func (p *MessageCtrl) DeleteHandler(*gin.Context) {}

type BoxCtrl struct {
}

func (p *BoxCtrl) CreateHandler(*gin.Context) {}
func (p *BoxCtrl) ListHandler(*gin.Context)   {}
func (p *BoxCtrl) TakeHandler(*gin.Context)   {}
func (p *BoxCtrl) UpdateHandler(*gin.Context) {}
func (p *BoxCtrl) DeleteHandler(*gin.Context) {}

type UserCtrl struct {
}

func (p *UserCtrl) CreateHandler(*gin.Context) {}
func (p *UserCtrl) ListHandler(*gin.Context)   {}
func (p *UserCtrl) TakeHandler(*gin.Context)   {}
func (p *UserCtrl) UpdateHandler(*gin.Context) {}
func (p *UserCtrl) DeleteHandler(*gin.Context) {}
