package itpkg

import (
	"bytes"
	"fmt"
	"github.com/codegangsta/cli"
	"github.com/gin-gonic/gin"
	"github.com/op/go-logging"
	"gopkg.in/bluesuncorp/validator.v5"
	"net/http"
	"os"
)

type Application struct {
	cfg *Config
}

func (p *Application) Init(env string) error {

	cfg := Config{
		beans:    make(map[string]interface{}, 0),
		validate: validator.New("validate", validator.BakedInValidators),
	}

	if err := loadConfig(&cfg, fmt.Sprintf("config/%s.yml", env)); err != nil {
		return err
	}

	cfg.env = env
	p.cfg = &cfg
	if p.cfg.IsProduction() {
		bkd, err := logging.NewSyslogBackend("itpkg")
		if err != nil {
			return err
		}
		lvl := logging.AddModuleLevel(bkd)
		lvl.SetLevel(logging.INFO, "")
		logging.SetBackend(lvl)
	} else {
		logging.SetLevel(logging.DEBUG, "")
		logging.SetFormatter(logging.MustStringFormatter("%{color}%{time:15:04:05.000} %{shortfunc} ▶ %{level:.4s} %{id:03x}%{color:reset} %{message}"))
	}
	return nil
}

func (p *Application) loop(f func(en Engine)) error {
	err := p.cfg.OpenDb()
	if err != nil {
		return err
	}
	p.cfg.OpenRedis()
	p.cfg.OpenMailer()
	p.cfg.OpenToken()

	if err = p.cfg.OpenCache(); err != nil {
		return err
	}

	if err = p.cfg.OpenRouter(); err != nil {
		return err
	}

	p.cfg.beans["db"] = p.cfg.db
	p.cfg.beans["cache"] = p.cfg.cache
	p.cfg.beans["router"] = p.cfg.router
	p.cfg.beans["validate"] = p.cfg.validate
	p.cfg.beans["mailer"] = p.cfg.mailer
	p.cfg.beans["token"] = p.cfg.token

	for _, en := range engines {
		n, v, _ := en.Info()
		Logger.Info("Load engine %s(%s)", n, v)
		en.Setup(p.cfg)
		f(en)
	}
	return nil
}

func (p *Application) Migrate() error {
	return p.loop(func(en Engine) {
		en.Migrate()
	})
}

func (p *Application) Server() error {
	if err := p.loop(func(en Engine) {
		en.Map()
		en.Mount()
	}); err != nil {
		return err
	}
	return http.ListenAndServe(fmt.Sprintf(":%d", p.cfg.Http.Port), p.cfg.router)
}

func (p *Application) Routes() error {
	if err := p.loop(func(en Engine) {
		gin.SetMode(gin.DebugMode)
		en.Mount()
	}); err != nil {
		return err
	}

	return nil
}

func (p *Application) Db() error {
	cmd, args := p.cfg.DbShell()
	return Shell(cmd, args...)
}

func (p *Application) Redis() error {
	cmd, args := p.cfg.RedisShell()
	return Shell(cmd, args...)
}

func (p *Application) ClearRedis(pat string) error {
	p.cfg.OpenRedis()
	r := p.cfg.redis.Get()
	defer r.Close()

	v, e := r.Do("KEYS", pat)
	if e != nil {
		return e
	}
	_, e = r.Do("DEL", v.([]interface{})...)
	if e != nil {
		return e
	}
	Logger.Info("Clear redis keys by %s succressfully!", pat)
	return nil
}

func (p *Application) Openssl() {
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
}

func (p *Application) Nginx() {
	hn, _ := os.Hostname()
	wd, _ := os.Getwd()

	var buf bytes.Buffer
	fmt.Fprintf(
		&buf,
		`
upstream ksana.conf {
server http://localhost:%d fail_timeout=0;
}
`,
		p.cfg.Http.Port)
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
}

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
			Name:    "migrate",
			Aliases: []string{"m"},
			Usage:   "Migrate the database",
			Flags:   []cli.Flag{envF},
			Action: func(c *cli.Context) {
				a := load(c)
				a.Migrate()
			},
		},
		{
			Name:    "cache:clear",
			Aliases: []string{"cc"},
			Usage:   "Clear cache from redis",
			Flags:   []cli.Flag{envF},
			Action: func(c *cli.Context) {
				a := load(c)
				if e := a.ClearRedis("cache://*"); e != nil {
					Logger.Fatalf("Error on clear cache: %v", e)
				}
			},
		},
		{
			Name:    "token:clear",
			Aliases: []string{"tc"},
			Usage:   "Clear tokens from redis",
			Flags:   []cli.Flag{envF},
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
