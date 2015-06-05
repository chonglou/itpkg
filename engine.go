package itpkg

import (
	"github.com/gorilla/pat"
)

type Engine interface {
	Map()
	Mount(*pat.Router)
	Migrate()
	Info() (name string, version string, desc string)
}
