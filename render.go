package itpkg

import (
	"encoding/json"
	"encoding/xml"
	"net/http"
	"strings"
	//"html/template"
	"time"
)

func LANG(req *http.Request) string {

	lang := req.URL.Query().Get("locale")
	if lang != "" {
		return lang
	}
	if cke, err := req.Cookie("NG_TRANSLATE_LANG_KEY"); err == nil {
		return strings.Replace(cke.Value, "%22", "", -1)
	}
	return "en"
}

func XML(writer http.ResponseWriter, val interface{}) {
	writer.Header().Set("Content-Type", "application/xml")

	x, e := xml.MarshalIndent(val, "", "  ")
	if e == nil {
		writer.Write(x)
	} else {
		ERROR(writer, e)
	}
}

func ERROR(writer http.ResponseWriter, e error) {
	http.Error(writer, e.Error(), http.StatusInternalServerError)
}

func JSON(writer http.ResponseWriter, val interface{}) {
	writer.Header().Set("Content-Type", "application/json")
	j, e := json.Marshal(val)
	if e == nil {
		writer.Write(j)
	} else {
		ERROR(writer, e)
	}
}

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
