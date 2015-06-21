package itpkg

import (
	"bytes"
	"fmt"
	"net/http"
	"os"

	"github.com/garyburd/redigo/redis"
	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"
	"github.com/op/go-logging"
)

type Application struct {
	Cfg     *Configuration  `inject:""`
	Logger  *logging.Logger `inject:""`
	Router  *gin.Engine     `inject:""`
	Redis   *redis.Pool     `inject:""`
	Db      *gorm.DB        `inject:""`
	engines []Engine
}

func (p *Application) loop(f func(en Engine) error) error {
	for _, en := range p.engines {
		if err := f(en); err != nil {
			return err
		}
	}
	return nil
}

func (p *Application) DbMigrate() error {
	return p.loop(func(en Engine) error {
		en.Migrate()
		return nil
	})
}

func (p *Application) DbCreate() error {
	cmd, args := p.Cfg.DbCreate()
	return Shell(cmd, args...)
}

func (p *Application) DbDrop() error {
	cmd, args := p.Cfg.DbDrop()
	return Shell(cmd, args...)
}

func (p *Application) Server() error {

	if err := p.loop(func(en Engine) error {
		en.Mount()
		return nil
	}); err != nil {
		return err
	}
	return http.ListenAndServe(fmt.Sprintf(":%d", p.Cfg.Http.Port), p.Router)
}

func (p *Application) Routes() error {
	gin.SetMode(gin.DebugMode)

	return p.loop(func(en Engine) error {
		en.Mount()
		return nil
	})
}

func (p *Application) DbShell() error {
	cmd, args := p.Cfg.DbShell()
	return Shell(cmd, args...)
}

func (p *Application) RedisShell() error {
	cmd, args := p.Cfg.RedisShell()
	return Shell(cmd, args...)
}

func (p *Application) clearRedis(pat string) error {
	r := p.Redis.Get()
	defer r.Close()

	v, e := r.Do("KEYS", pat)
	if e != nil {
		return e
	}
	ks := v.([]interface{})
	if len(ks) == 0 {
		p.Logger.Info("Empty!!!")
		return nil
	}
	_, e = r.Do("DEL", ks...)
	if e != nil {
		return e
	}
	p.Logger.Info("Clear redis keys by %s succressfully!", pat)
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
		p.Cfg.Http.Port)
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
