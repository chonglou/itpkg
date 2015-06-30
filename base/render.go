package itpkg

import (
	"net/http"
	"strings"
	"time"

	"github.com/gin-gonic/gin"
)

func JSONP(c *gin.Context, v interface{}) {
	cb := c.DefaultQuery("callback", "")
	if cb == "" {
		c.JSON(http.StatusOK, v)
	} else {
		c.Header("Content-Type", "text/javascript")
		js, _ := Obj2json(v)
		c.String(http.StatusOK, "%s(%s)", cb, js)
	}
}

func LANG(c *gin.Context) string {
	return c.Request.Header.Get("Accept-Language")
	//return c.DefaultQuery("locale", "en-US")
}

type Link struct {
	Url   string `json:"url"`
	Name  string `json:"name"`
	Items []Link `json:"items"`
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
