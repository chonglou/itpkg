package itpkg

import (
	// "github.com/go-martini/martini"

	"fmt"
	"github.com/codegangsta/cli"
	"log"
	"os"
	"strings"
)

func Run() {
	var err error

	modes := []string{"server", "db", "redis"}

	app := cli.NewApp()
	app.Name = "itpkg"
	app.Usage = "IT-PACKAGE"
	app.Flags = []cli.Flag{
		cli.StringFlag{
			Name:  "environment, e",
			Value: "development",
			Usage: "can be production, development, etc...",
		},
		cli.StringFlag{
			Name:  "config, c",
			Value: "config.yml",
			Usage: "configuration file",
		},
		cli.StringFlag{
			Name:  "run, r",
			Value: "server",
			Usage: fmt.Sprintf("can be %s", strings.Join(modes, ", ")),
		},
	}
	app.Action = func(c *cli.Context) {
		os.Setenv("MARTINI_ENV", c.String("env"))

		if err = loadConfig(c.String("config")); err != nil {
			log.Fatalf("Load config error: %v", err)
		}

		run := c.String("run")
		switch run {
		case "server":
		case "db":
			cmd, args := glConfig.DbShell()
			err = Shell(cmd, args...)
		case "redis":
			cmd, args := glConfig.RedisShell()
			err = Shell(cmd, args...)
		default:
			println("Unknown mode " + run)
		}
	}
	app.Run(os.Args)
	//app := martini.Classic()

	// app.Get("/", func() string {
	//   return "Hello world!"
	// })
	//app.Run()
}
