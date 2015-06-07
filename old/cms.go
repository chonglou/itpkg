package itpkg

import (
	"github.com/gorilla/pat"
)

type CmsEngine struct {
	cfg *Config
}

func (p *CmsEngine) Map() {

}

func (p *CmsEngine) Mount(r *pat.Router) {

}

func (p *CmsEngine) Migrate() {

}

func (p *CmsEngine) Info() (name string, version string, desc string) {
	return "", "", ""
}
