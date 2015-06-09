package forum

import (
	"github.com/gin-gonic/gin"
)

type ArticleCtrl struct {
}

func (p *ArticleCtrl) CreateHandler(*gin.Context) {}
func (p *ArticleCtrl) ListHandler(*gin.Context)   {}
func (p *ArticleCtrl) TakeHandler(*gin.Context)   {}
func (p *ArticleCtrl) UpdateHandler(*gin.Context) {}
func (p *ArticleCtrl) DeleteHandler(*gin.Context) {}

type BoardCtrl struct {
}

func (p *BoardCtrl) CreateHandler(*gin.Context) {}
func (p *BoardCtrl) ListHandler(*gin.Context)   {}
func (p *BoardCtrl) TakeHandler(*gin.Context)   {}
func (p *BoardCtrl) UpdateHandler(*gin.Context) {}
func (p *BoardCtrl) DeleteHandler(*gin.Context) {}

type CommentCtrl struct {
}

func (p *CommentCtrl) CreateHandler(*gin.Context) {}
func (p *CommentCtrl) ListHandler(*gin.Context)   {}
func (p *CommentCtrl) TakeHandler(*gin.Context)   {}
func (p *CommentCtrl) UpdateHandler(*gin.Context) {}
func (p *CommentCtrl) DeleteHandler(*gin.Context) {}
