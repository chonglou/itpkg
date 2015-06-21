package itpkg

import (
	"github.com/gin-gonic/gin"
	"strings"
	"time"
)

func LANG(c *gin.Context) string {
	return c.DefaultQuery("locale", "en")
}

type Response struct {
	Ok      bool          `json:"ok"`
	Errors  []string      `json:"errors"`
	Data    []interface{} `json:"data"`
	Created time.Time     `json:"created"`
}

func (p *Response) Add(items ...interface{}) {
	p.Data = append(p.Data, items...)
}

func (p *Response) Error(errs ...string) {
	p.Ok = false
	p.Errors = append(p.Errors, errs...)
}

func (p *Response) Invalid(err error) {
	errs := strings.Split(err.Error(), "\n")
	s := len(errs)
	if s < 2 {
		p.Error(errs...)
	} else {
		p.Error(errs[1:(len(errs) - 1)]...)
	}
}

func NewResponse(ok bool, data ...interface{}) Response {
	return Response{
		Ok:      ok,
		Data:    data,
		Errors:  make([]string, 0),
		Created: time.Now()}
}
