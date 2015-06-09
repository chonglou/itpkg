package teamwork

import (
	"github.com/gin-gonic/gin"
)

type ProjectCtrl struct {
}

func (p *ProjectCtrl) CreateHandler(*gin.Context) {}
func (p *ProjectCtrl) ListHandler(*gin.Context)   {}
func (p *ProjectCtrl) TakeHandler(*gin.Context)   {}
func (p *ProjectCtrl) UpdateHandler(*gin.Context) {}
func (p *ProjectCtrl) DeleteHandler(*gin.Context) {}

type TaskCtrl struct {
}

func (p *TaskCtrl) CreateHandler(*gin.Context) {}
func (p *TaskCtrl) ListHandler(*gin.Context)   {}
func (p *TaskCtrl) TakeHandler(*gin.Context)   {}
func (p *TaskCtrl) UpdateHandler(*gin.Context) {}
func (p *TaskCtrl) DeleteHandler(*gin.Context) {}

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
