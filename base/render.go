package itpkg

import (
	//"encoding/json"
	//"encoding/xml"
	//"net/http"
	//"strings"
	//"html/template"
	"github.com/gin-gonic/gin"
	//"fmt"
	"time"
)

func LANG(c *gin.Context) string {
	return c.DefaultQuery("locale", "en")
/*
	lang := req.URL.Query().Get("locale")
	if lang != "" {
		return lang
	}
	if cke, err := req.Cookie("NG_TRANSLATE_LANG_KEY"); err == nil {
		return strings.Replace(cke.Value, "%22", "", -1)
	}
	return "en"
	*/
}
/*

func XML(wrt http.ResponseWriter, val interface{}) {
	wrt.Header().Set("Content-Type", "application/xml")
	wrt.Write([]byte(xml.Header))
	if err := xml.NewEncoder(wrt).Encode(val); err == nil {
		fmt.Fprintf(wrt, "\n")
	} else {
		ERROR(wrt, err)
	}
}

func ERROR(writer http.ResponseWriter, e error) {
	http.Error(writer, e.Error(), http.StatusInternalServerError)
}

func JSON(wrt http.ResponseWriter, val interface{}) {
	wrt.Header().Set("Content-Type", "application/json")
	en := json.NewEncoder(wrt)
	if err := en.Encode(val); err != nil {
		ERROR(wrt, err)
	}
}
*/

type Response struct {
	Ok      bool          `json:"ok"`
	Errors  []string      `json:"errors"`
	Data    []interface{} `json:"data"`
	Created time.Time     `json:"created"`
}

func (msg *Response) Error(err string) {
	msg.Ok = false
	msg.Errors = append(msg.Errors, err)
}

func NewResponse(ok bool, data ...interface{}) Response {
	return Response{
		Ok:      ok,
		Data:    data,
		Errors:  make([]string, 0),
		Created: time.Now()}
}
