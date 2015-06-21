package itpkg

import ()

type Engine interface {
	Setup()
	Mount()
	Migrate()
	Info() (name string, version string, desc string)
}
