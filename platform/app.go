package main

import (
	"github.com/chonglou/itpkg/base"
	_ "github.com/chonglou/itpkg/wiki"
	_ "github.com/lib/pq"
)

func main() {
	itpkg.Run()
}
