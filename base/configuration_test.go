package itpkg_test

import (
	"github.com/chonglou/itpkg/base"
	"testing"
)

func TestLoadConfig(t *testing.T) {
	cfg := itpkg.Configuration{}
	err := itpkg.LoadConfig(&cfg, "tmp/config.yml")
	if err != nil {
		t.Errorf("%v", err)
	}
}
