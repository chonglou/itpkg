package itpkg

import (
	"github.com/gorilla/pat"
)

type TeamworkEngine struct {
	cfg *Config
}

func (p *TeamworkEngine) Map() {

}

func (p *TeamworkEngine) Mount(r *pat.Router) {

}

func (p *TeamworkEngine) Migrate() {

}

func (p *TeamworkEngine) Info() (name string, version string, desc string) {
	return "", "", ""
}
