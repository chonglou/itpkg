package itpkg

import (
	"encoding/json"
	"log"
	"testing"
)

func TestWidgets(t *testing.T) {
	log.Printf("==================WIDGETS=============================")
	fm := Form{
		Id:    "test-fm",
		Token: "token",
		Fields: []Field{
			Field{Id: "id1", Label: "L 1", Value: 0},
			Field{Id: "id2", Label: "L 1", Value: "aaa"},
			Field{Id: "id1", Type: "hidden", Value: 0},
		},
	}

	j, e := json.MarshalIndent(fm, "", "  ")
	if e != nil {
		t.Errorf("Json error: %v", e)
	}
	log.Printf("Form: %s", string(j))
}
