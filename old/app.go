package itpkg

import (
	"bytes"
	"fmt"
	"github.com/gin-gonic/contrib/expvar"
	"github.com/gin-gonic/contrib/sessions"
	"github.com/gin-gonic/contrib/static"
	"github.com/gin-gonic/gin"

	"os"
)




func Run() error {


	load := func(c *cli.Context) Config {
		var err error
		env := 
		os.Setenv("ITPKG_ENV", env)



	}

				os.Setenv("PORT", c.String("port"))

				cfg := load(c)
				db, err := cfg.Db()
				if err != nil {
					log.Fatalf("Error on open database: %v", err)
				}

				if IsProduction() {
					gin.SetMode(gin.ReleaseMode)
				}
				web := gin.Default()

				// web.Use(gin.Logger())
				// web.Use(gin.Recovery())
				web.Use(sessions.Sessions(cfg.Http.Cookie, cfg.SessionStore()))

				// web.Use(csrf.Generate(&csrf.Options{
				// 	Secret:     string(cfg.secret[210:242]),
				// 	SessionKey: "UID",
				// 	ErrorFunc: func(w http.ResponseWriter) {
				// 		http.Error(w, "CSRF token validation failed", http.StatusBadRequest)
				// 	},
				// }))
				//
				// web.Use(render.Renderer())
				// web.Map(cfg.Mailer())
				//
				// oauth2.PathLogin = "/oauth2/login"
				// oauth2.PathLogout = "/oauth2/logout"
				// oauth2.PathCallback = "/oauth2/callback"
				// oauth2.PathError = "oauth2/error"
				//
				// web.Use(oauth2.Google(
				// 	&goauth2.Config{
				// 		ClientID:     cfg.Google.Id,
				// 		ClientSecret: cfg.Google.Secret,
				// 		Scopes:       []string{}, // todo
				// 		RedirectURL:  "redirect_url",
				// 	},
				// ))

				for _, e := range []Engine{
					&BaseEngine{app: web, db: db, cfg: &cfg},
					&AuthEngine{app: web, db: db, cfg: &cfg},
					&WikiEngine{},
					&ForumEngine{},
					&TeamworkEngine{},
					&ShopEngine{},
				} {
					n, v, _ := e.Info()
					log.Info("Mount engine %s(%s)", n, v)
					e.Migrate()
					e.Map()
					e.Mount()
				}

				if !IsProduction() {

					web.Use(static.Serve("/", static.LocalFile("public", false)))
					web.GET("/debug/vars", expvar.Handler())
				}

				web.Run(fmt.Sprintf(":%d", cfg.Http.Port))
			},


}
