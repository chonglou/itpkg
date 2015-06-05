package itpkg

import (
	"github.com/gorilla/pat"
)

type WikiEngine struct {
	cfg *Config
}

func (p *WikiEngine) Map() {

}

func (p *WikiEngine) Mount(r *pat.Router) {

}

func (p *WikiEngine) Migrate() {

}

func (p *WikiEngine) Info() (name string, version string, desc string) {
	return "", "", ""
}
