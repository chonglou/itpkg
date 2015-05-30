package itpkg

import (
	"fmt"
	"github.com/codegangsta/cli"
	"github.com/go-martini/martini"
	"log"
	"os"
)

func Run() error {

	envF := cli.StringFlag{
		Name:   "environment, e",
		Value:  "development",
		Usage:  "can be production, development, etc...",
		EnvVar: "ITPKG_ENV",
	}

	load := func(c *cli.Context, cfg *Config) {
		env := c.String("environment")
		os.Setenv("ITPKG_ENV", env)

		if err := loadConfig(cfg, fmt.Sprintf("config/%s.yml", env)); err != nil {
			log.Fatalf("Error on load config: %v", err)
		}
	}

	app := cli.NewApp()
	app.Name = "itpkg"
	app.Usage = "IT-PACKAGE"
	app.Version = "v20150529"
	app.Commands = []cli.Command{
		{
			Name:    "server",
			Aliases: []string{"s"},
			Usage:   "Start the ITPKG server",
			Flags: []cli.Flag{
				envF,
				cli.IntFlag{
					Name:   "port, p",
					Value:  3000,
					Usage:  "listen port",
					EnvVar: "PORT",
				},
			},
			Action: func(c *cli.Context) {
				os.Setenv("PORT", c.String("port"))

				cfg := Config{}
				load(c, &cfg)
				// todo
				martini.Env = os.Getenv("ITPKG_ENV")
				web := martini.Classic()
				web.Run()
			},
		},
		{
			Name:    "dbconsole",
			Aliases: []string{"db"},
			Usage:   "Start a console for the database",
			Flags:   []cli.Flag{envF},
			Action: func(c *cli.Context) {
				cfg := Config{}
				load(c, &cfg)
				cmd, args := cfg.DbShell()
				Shell(cmd, args...)
			},
		},
		{
			Name:    "redis",
			Aliases: []string{"r"},
			Usage:   "Start a console for the redis",
			Flags:   []cli.Flag{envF},
			Action: func(c *cli.Context) {
				cfg := Config{}
				load(c, &cfg)

				cmd, args := cfg.RedisShell()
				Shell(cmd, args...)
			},
		},
	}

	return app.Run(os.Args)

}
