package itpkg

import (
	"log"
	"testing"
)

func TestLoadConfig(t *testing.T) {
	log.Printf("==================LoadConfig=============================")
	err := loadConfig("tmp/config.yml")
	if err != nil {
		t.Errorf("%v", err)
	}
}
