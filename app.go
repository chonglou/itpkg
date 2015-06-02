package itpkg

import (
	"bytes"
	"fmt"
	"github.com/codegangsta/cli"
	"github.com/go-martini/martini"
	"github.com/jinzhu/gorm"
	"github.com/martini-contrib/csrf"
	"github.com/martini-contrib/oauth2"
	"github.com/martini-contrib/render"
	"github.com/martini-contrib/sessions"
	goauth2 "golang.org/x/oauth2"
	"log"
	"net/http"
	"os"
)

func Run() error {

	envF := cli.StringFlag{
		Name:   "environment, e",
		Value:  "development",
		Usage:  "can be production, development, etc...",
		EnvVar: "ITPKG_ENV",
	}

	load := func(c *cli.Context) (Config, gorm.DB) {
		var err error
		env := c.String("environment")
		os.Setenv("ITPKG_ENV", env)

		cfg := Config{}
		if err = loadConfig(&cfg, fmt.Sprintf("config/%s.yml", env)); err != nil {
			log.Fatalf("Error on load config: %v", err)
		}

		var db gorm.DB
		if db, err = gorm.Open(cfg.Database.Driver, cfg.DbUrl()); err != nil {
			log.Fatalf("Error on open database: %v", err)
		}
		db.LogMode(env != "production")
		if err = db.DB().Ping(); err != nil {
			log.Fatalf("Error on ping database: %v", err)
		}

		return cfg, db
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

				cfg, db := load(c)

				martini.Env = os.Getenv("ITPKG_ENV")
				web := martini.Classic()

				web.Use(sessions.Sessions(
					cfg.Http.Cookie,
					sessions.NewCookieStore(cfg.secret[100:164], cfg.secret[170:202])),
				)
				web.Use(csrf.Generate(&csrf.Options{
					Secret:     string(cfg.secret[210:242]),
					SessionKey: "UID",
					ErrorFunc: func(w http.ResponseWriter) {
						http.Error(w, "CSRF token validation failed", http.StatusBadRequest)
					},
				}))

				web.Use(render.Renderer())
				web.Map(cfg.Mailer())

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

				for _, e := range []Engine{
					&BaseEngine{app: web, db: &db, cfg: &cfg},
					&AuthEngine{app: web, db: &db, cfg: &cfg},
					&WikiEngine{},
					&ForumEngine{},
					&TeamworkEngine{},
					&ShopEngine{},
				} {
					n, v, _ := e.Info()
					log.Printf("Mount engine %s(%s)", n, v)
					e.Migrate()
					e.Map()
					e.Mount()
				}
				web.RunOnAddr(fmt.Sprintf(":%d", cfg.Http.Port))
			},
		},
		{
			Name:    "dbconsole",
			Aliases: []string{"db"},
			Usage:   "Start a console for the database",
			Flags:   []cli.Flag{envF},
			Action: func(c *cli.Context) {
				cfg, _ := load(c)
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
				cfg, _ := load(c)
				cmd, args := cfg.RedisShell()
				Shell(cmd, args...)
			},
		},
		{
			Name:    "nginx",
			Aliases: []string{"n"},
			Usage:   "Nginx config file demo",
			Flags:   []cli.Flag{envF},
			Action: func(c *cli.Context) {
				hn, _ := os.Hostname()
				wd, _ := os.Getwd()
				cfg, _ := load(c)

				var buf bytes.Buffer
				fmt.Fprintf(
					&buf,
					`
upstream ksana.conf {
	server http://localhost:%d fail_timeout=0;
}
`,
					cfg.Http.Port)
				fmt.Fprintf(
					&buf,
					`
server {
	listen 443;
	ssl  on;
	ssl_certificate  ssl/ksana.crt;
	ssl_certificate_key  ssl/ksana.key;
	ssl_session_timeout  5m;
	ssl_protocols  SSLv2 SSLv3 TLSv1;
	ssl_ciphers  RC4:HIGH:!aNULL:!MD5;
	ssl_prefer_server_ciphers  on;

	client_max_body_size 4G;
	keepalive_timeout 10;

	server_name %s;

	root %s/public;
	try_files $uri $uri/index.html @ksana.conf;

	location @ksana.conf {
		proxy_set_header X-Forwarded-Proto https;
		proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
		proxy_set_header Host $http_host;
		proxy_set_header  X-Real-IP $remote_addr;
		proxy_redirect off;
		proxy_pass http://ksana.conf;
		# limit_req zone=one;
		access_log log/ksana.access.log;
		error_log log/ksana.error.log;
	}

	#location ^~ /assets/ {
	location ~* \.(?:css|js|html|jpg|jpeg|gif|png|ico)$ {
		gzip_static on;
		expires max;
		add_header Cache-Control public;
	}

	location = /50x.html {
		root html;
	}

	location = /404.html {
		root html;
	}

	location @503 {
		error_page 405 = /system/maintenance.html;
		if (-f $document_root/system/maintenance.html) {
			rewrite ^(.*)$ /system/maintenance.html break;
		}
		rewrite ^(.*)$ /503.html break;
	}

	if ($request_method !~ ^(GET|HEAD|PUT|PATCH|POST|DELETE|OPTIONS)$ ){
		return 405;
	}

	if (-f $document_root/system/maintenance.html) {
		return 503;
	}

	location ~ \.(php|jsp|asp)$ {
		return 405;
	}

}
		`, hn, wd)
				fmt.Fprintf(&buf, "\n")
				buf.WriteTo(os.Stdout)
			},
		},
		{
			Name:    "openssl",
			Aliases: []string{"ssl"},
			Usage:   "Openssl certs command demo",
			Flags:   []cli.Flag{envF},
			Action: func(c *cli.Context) {
				var buf bytes.Buffer
				fmt.Fprintf(&buf,
					`
openssl genrsa -out root/root-key.pem 2048
openssl req -new -key root/root-key.pem -out root/root-req.csr -text
openssl x509 -req -in root/root-req.csr -out root/root-cert.pem -sha512 -signkey root/root-key.pem -days 3650 -text -extfile /etc/ssl/openssl.cnf -extensions v3_ca

openssl genrsa -out server/server-key.pem 2048
openssl req -new -key server/server-key.pem -out server/server-req.csr -text
openssl x509 -req -in server/server-req.csr -CA root/root-cert.pem -CAkey root/root-key.pem -CAcreateserial -days 3650 -out server/server-cert.pem -text

openssl verify -CAfile root/root-cert.pem server/server-cert.pem
openssl rsa -noout -text -in server-key.pem
openssl req -noout -text -in server-req.csr
openssl x509 -noout -text -in server-cert.pem
		`)
				fmt.Fprintf(&buf, "\n")
				buf.WriteTo(os.Stdout)
			},
		},
	}

	return app.Run(os.Args)

}
