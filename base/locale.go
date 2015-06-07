package itpkg

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"os"
)

var locales = make(map[string]map[string]string, 0)

func LoadLocales(path string) error {
	Log.Info("Loading i18n from " + path)
	files, err := ioutil.ReadDir(path)
	if err != nil {
		return err
	}
	for _, f := range files {
		fn := f.Name()

		lang := fn[0:(len(fn) - 5)]

		ss := make(map[string]string, 0)
		fd, err := os.Open(path + "/" + fn)
		if err != nil {
			return err
		}
		defer fd.Close()

		err = json.NewDecoder(fd).Decode(&ss)
		if err != nil {
			return err
		}
		Log.Info("Find locale file %s(%d)", fn, len(ss))
		locales[lang] = ss

	}
	return nil
}

func T(locale, key string, args ...interface{}) string {
	if l, ok := locales[locale]; ok {
		if v, ok := l[key]; ok {
			return fmt.Sprintf(v, args...)
		}
	}
	return fmt.Sprintf("Translation [%s] not found", key)
}
