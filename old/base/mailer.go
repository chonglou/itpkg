package itpkg

import (
	"bytes"
	"crypto/tls"
	"gopkg.in/gomail.v1"
	"html/template"
)

type Mailer struct {
	perform bool

	bcc  string
	from string

	mailer *gomail.Mailer
}

func (p *Mailer) Auth(from, host string, port int, ssl bool, username, password, bcc string) {

	if ssl {
		p.mailer = gomail.NewMailer(host, username, password, port, gomail.SetTLSConfig(&tls.Config{InsecureSkipVerify: true}))
	} else {
		p.mailer = gomail.NewMailer(host, username, password, port)
	}

	p.bcc = bcc
	p.from = from
}

func (p *Mailer) HtmlT(to []string, subject, file string, arg interface{}, files ...string) {
	var buf bytes.Buffer
	var err error

	t := template.New("")
	t, err = t.ParseFiles(file)
	if err != nil {
		Logger.Error("Parse mailer template fail " + err.Error())
		return
	}
	if err = t.Execute(&buf, arg); err != nil {
		Logger.Error("Parse mailer template fail: " + err.Error())
		return
	}

	p.Html(to, subject, buf.String(), files...)
}

func (p *Mailer) Html(to []string, subject, body string, files ...string) {
	msg := gomail.NewMessage()
	msg.SetHeader("From", p.from)
	msg.SetHeader("To", to...)
	if p.bcc != "" {
		msg.SetHeader("Bcc", p.bcc)
	}
	msg.SetHeader("Subject", subject)
	msg.SetBody("text/html", body)
	for _, n := range files {
		f, e := gomail.OpenFile(n)
		if e != nil {
			Logger.Error("Faile to add attachment: " + e.Error())
			continue
		}
		msg.Attach(f)
	}

	p.send(msg)
}

func (p *Mailer) send(msg *gomail.Message) {
	if p.perform {
		if err := p.mailer.Send(msg); err != nil {
			Logger.Error("Error on send mail: %v", err)
		}
	} else {
		Logger.Debug("%v", msg.Export())
	}
}
