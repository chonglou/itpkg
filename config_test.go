package itpkg

import (
	"testing"
)

func TestLoadConfig(t *testing.T) {
	log.Debug("==================LoadConfig=============================")
	cfg := Config{}
	err := loadConfig(&cfg, "tmp/config.yml")
	if err != nil {
		t.Errorf("%v", err)
	}
}
