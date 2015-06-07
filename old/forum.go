package itpkg

import (
	"github.com/gorilla/pat"
)

type ForumEngine struct {
	cfg *Config
}

func (p *ForumEngine) Map() {

}

func (p *ForumEngine) Mount(r *pat.Router) {

}

func (p *ForumEngine) Migrate() {

}

func (p *ForumEngine) Info() (name string, version string, desc string) {
	return "", "", ""
}
