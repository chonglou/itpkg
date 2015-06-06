package itpkg

import (
	"net/http"
	"strings"
	"time"
)


type Form struct {
	Message
	Id      string   `json:"id"`
	Method  string   `json:"method"`
	Action  string   `json:"action"`
	Token   string   `json:"token"`
	Fields  []Field  `json:"fields"`
	Buttons []Button `json:"buttons"`
}

func (fm *Form) Add(f Field) {
	fm.Fields = append(fm.Fields, f)
}

func (fm *Form) Btn(b Button) {
	fm.Buttons = append(fm.Buttons, b)
}

func (fm *Form) Submit() {
	fm.Btn(Button{Id: "submit", Style: "primary"})

}
func (fm *Form) Reset() {
	fm.Btn(Button{Id: "reset", Style: "warning"})
}

type Button struct {
	Id    string `json:"id"`
	Style string `json:"style"`
}

type Field struct {
	Id    string      `json:"id"`
	Type  string      `json:"type"`
	Value interface{} `json:"value"`
	Label string      `json:"label"`
	Text  string      `json:"text"`
	Valid string      `json:"valid"`
}


func NewForm(id string) Form {
	return Form{
		Message: Message{
			Ok:      true,
			Type:    "form",
			Errors:  make([]string, 0),
			Created: time.Now()},
		Id:      "fm_" + id,
		Method:  "POST",
		Token:   RandomStr(64),
		Fields:  make([]Field, 0),
		Buttons: make([]Button, 0)}
}
