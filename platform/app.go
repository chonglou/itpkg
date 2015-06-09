package main

import (
	"github.com/chonglou/itpkg/base"
	_ "github.com/chonglou/itpkg/cms"
	_ "github.com/chonglou/itpkg/forum"
	_ "github.com/chonglou/itpkg/shop"
	_ "github.com/chonglou/itpkg/teamwork"
	_ "github.com/chonglou/itpkg/wiki"
	_ "github.com/lib/pq"
)

func main() {
	itpkg.Run()
}
