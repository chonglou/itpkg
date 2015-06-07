package itpkg

import ()

type Engine interface {
	Map()
	Mount()
	Migrate()
	Info() (name string, version string, desc string)
}

var engines = make(map[string]Engine, 0)

func Register(en string) {
	if _, ok := engines[en]; ok {
		return
	}
	engines[en] = nil
}
