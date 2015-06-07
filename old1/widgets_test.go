package itpkg

import (
	"encoding/json"
	"testing"
)

func TestWidgets(t *testing.T) {
	log.Info("==================WIDGETS=============================")
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
	log.Info("Form: %s", string(j))
}
