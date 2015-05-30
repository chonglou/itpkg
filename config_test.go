package itpkg

import (
	"log"
	"testing"
)

func TestLoadConfig(t *testing.T) {
	log.Printf("==================LoadConfig=============================")
	cfg := Config{}
	err := loadConfig(&cfg, "tmp/config.yml")
	if err != nil {
		t.Errorf("%v", err)
	}
}
