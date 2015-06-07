package itpkg

import (
	"testing"
	"time"
)

func TestI18n(t *testing.T) {
	LoadLocales("tmp/locales")
	for _, l := range []string{"en", "zh_CN"} {
		t.Logf("%s: %v", l, T(l, "hello", time.Now()))
	}
}
