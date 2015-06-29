package itpkg

import (
	"reflect"
	"strings"
)

type Engine interface {
	Mount()
	Migrate()
	Info() (name string, version string, desc string)
}

func IsInnerEngine(en Engine) bool {
	return strings.HasPrefix(reflect.TypeOf(en).String(), "*itpkg")
}
