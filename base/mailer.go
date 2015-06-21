package itpkg

import (
	"bytes"
	"html/template"

	"github.com/op/go-logging"
	"gopkg.in/gomail.v1"
)

type Mailer struct {
	Perform bool            `inject:"production?"`
	Mailer  *gomail.Mailer  `inject:""`
	Logger  *logging.Logger `inject:""`
}

func (p *Mailer) HtmlT(from, bcc string, to []string, subject, file string, arg interface{}, files ...string) {
	var buf bytes.Buffer
	var err error

	t := template.New("")
	t, err = t.ParseFiles(file)
	if err != nil {
		p.Logger.Error("Parse mailer template fail " + err.Error())
		return
	}
	if err = t.Execute(&buf, arg); err != nil {
		p.Logger.Error("Parse mailer template fail: " + err.Error())
		return
	}

	p.Html(from, bcc, to, subject, buf.String(), files...)
}

func (p *Mailer) Html(from, bcc string, to []string, subject, body string, files ...string) {
	msg := p.message(from, bcc, to, subject, files...)
	msg.SetBody("text/html", body)
	p.send(msg)
}

func (p *Mailer) message(from, bcc string, to []string, subject string, files ...string) *gomail.Message {
	msg := gomail.NewMessage()
	msg.SetHeader("From", from)
	msg.SetHeader("To", to...)
	if bcc != "" {
		msg.SetHeader("Bcc", bcc)
	}
	msg.SetHeader("Subject", subject)
	for _, n := range files {
		f, e := gomail.OpenFile(n)
		if e != nil {
			p.Logger.Error("Faile to add attachment: " + e.Error())
			continue
		}
		msg.Attach(f)
	}
	return msg
}

func (p *Mailer) send(msg *gomail.Message) {
	if p.Perform {
		if err := p.Mailer.Send(msg); err != nil {
			p.Logger.Error("Error on send mail: %v", err)
		}
	} else {
		p.Logger.Debug("%v", msg.Export())
	}
}
