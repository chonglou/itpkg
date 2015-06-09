package cms

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

type TagCtrl struct {
}

func (p *TagCtrl) CreateHandler(*gin.Context) {}
func (p *TagCtrl) ListHandler(*gin.Context)   {}
func (p *TagCtrl) TakeHandler(*gin.Context)   {}
func (p *TagCtrl) UpdateHandler(*gin.Context) {}
func (p *TagCtrl) DeleteHandler(*gin.Context) {}

type CommentCtrl struct {
}

func (p *CommentCtrl) CreateHandler(*gin.Context) {}
func (p *CommentCtrl) ListHandler(*gin.Context)   {}
func (p *CommentCtrl) TakeHandler(*gin.Context)   {}
func (p *CommentCtrl) UpdateHandler(*gin.Context) {}
func (p *CommentCtrl) DeleteHandler(*gin.Context) {}
