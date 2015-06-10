package itpkg

import (
	"github.com/codegangsta/cli"
	"os"
)

func Run() error {

	load := func(c *cli.Context) Application {
		a := Application{}
		if err := a.Init(c.String("environment")); err != nil {
			Logger.Fatalf("error on load config:%v", err)
		}
		return a
	}

	envF := cli.StringFlag{
		Name:   "environment, e",
		Value:  "development",
		Usage:  "can be production, development, etc...",
		EnvVar: "ITPKG_ENV",
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
				a := load(c)
				if e := a.Server(); e != nil {
					Logger.Fatalf("Error on start server: %v", e)
				}
			},
		},
		{
			Name:    "dbconsole",
			Aliases: []string{"db"},
			Usage:   "Start a console for the database",
			Flags:   []cli.Flag{envF},
			Action: func(c *cli.Context) {
				a := load(c)
				a.Db()
			},
		},
		{
			Name:    "routes",
			Aliases: []string{"ro"},
			Usage:   "Print out all defined routes in match order, with names",
			Flags:   []cli.Flag{envF},
			Action: func(c *cli.Context) {
				a := load(c)
				a.Routes()
			},
		},
		{
			Name:    "redis",
			Aliases: []string{"re"},
			Usage:   "Start a console for the redis",
			Flags:   []cli.Flag{envF},
			Action: func(c *cli.Context) {
				a := load(c)
				a.Redis()
			},
		},
		{
			Name:    "nginx",
			Aliases: []string{"n"},
			Usage:   "Nginx config file demo",
			Flags:   []cli.Flag{envF},
			Action: func(c *cli.Context) {
				a := load(c)
				a.Nginx()
			},
		},
		{
			Name:    "openssl",
			Aliases: []string{"ssl"},
			Usage:   "Openssl certs command demo",
			Flags:   []cli.Flag{envF},
			Action: func(c *cli.Context) {
				a := Application{}
				a.Openssl()
			},
		},
		{
			Name:  "db:migrate",
			Usage: "Migrate the database",
			Flags: []cli.Flag{envF},
			Action: func(c *cli.Context) {
				a := load(c)
				a.DbMigrate()
			},
		},
		{
			Name:  "db:drop",
			Usage: "Drops the database",
			Flags: []cli.Flag{envF},
			Action: func(c *cli.Context) {
				a := load(c)
				a.DbDrop()
			},
		},
		{
			Name:  "db:create",
			Usage: "Creates the database",
			Flags: []cli.Flag{envF},
			Action: func(c *cli.Context) {
				a := load(c)
				a.DbCreate()
			},
		},
		{
			Name:  "cache:clear",
			Usage: "Clear cache from redis",
			Flags: []cli.Flag{envF},
			Action: func(c *cli.Context) {
				a := load(c)
				if e := a.ClearRedis("cache://*"); e != nil {
					Logger.Fatalf("Error on clear cache: %v", e)
				}
			},
		},
		{
			Name:  "token:clear",
			Usage: "Clear tokens from redis",
			Flags: []cli.Flag{envF},
			Action: func(c *cli.Context) {
				a := load(c)
				if e := a.ClearRedis("token://*"); e != nil {
					Logger.Fatalf("Error on clear tokens: %v", e)
				}
			},
		},
	}

	return app.Run(os.Args)
}
