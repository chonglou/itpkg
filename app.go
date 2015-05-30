package itpkg

import (
	"fmt"
	"github.com/codegangsta/cli"
	"github.com/go-martini/martini"
	"github.com/martini-contrib/oauth2"
	"github.com/martini-contrib/sessions"
	goauth2 "golang.org/x/oauth2"
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

				martini.Env = os.Getenv("ITPKG_ENV")
				web := martini.Classic()

				web.Use(sessions.Sessions(
					cfg.Http.Cookie,
					sessions.NewCookieStore(cfg.secret[100:164], cfg.secret[170:202])),
				)
				oauth2.PathLogin = "/oauth2/login"
				oauth2.PathLogout = "/oauth2/logout"
				oauth2.PathCallback = "/oauth2/callback"
				oauth2.PathError = "oauth2/error"

				web.Use(oauth2.Google(
					&goauth2.Config{
						ClientID:     cfg.Google.Id,
						ClientSecret: cfg.Google.Secret,
						Scopes:       []string{}, // todo
						RedirectURL:  "redirect_url",
					},
				))

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
