package itpkg

import ()

type Engine interface {
	Map()
	Mount()
	Migrate()
	Info() (name string, version string, desc string)
}

var engines = make([]string, 0)

func Register(en string) {
	for _, e := range engines {
		if e == en {
			return
		}
	}
	engines = append(engines, en)
}
