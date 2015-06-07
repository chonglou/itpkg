package itpkg

import (
	"github.com/gorilla/pat"
)

type ShopEngine struct {
	cfg *Config
}

func (p *ShopEngine) Map() {

}

func (p *ShopEngine) Mount(r *pat.Router) {

}

func (p *ShopEngine) Migrate() {

}

func (p *ShopEngine) Info() (name string, version string, desc string) {
	return "", "", ""
}
