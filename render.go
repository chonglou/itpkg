package itpkg

import (
	"encoding/json"
	"encoding/xml"
	"net/http"
	//"html/template"
)

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
