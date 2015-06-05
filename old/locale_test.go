package itpkg

import (
	"testing"
	"time"
)

func TestI18n(t *testing.T) {
	log.Info("==================LOCALE=============================")
	LoadLocales("tmp/locales")
	for _, l := range []string{"en", "zh_CN"} {
		log.Info("%s: %v", l, T(l, "hello", time.Now()))
	}
}
