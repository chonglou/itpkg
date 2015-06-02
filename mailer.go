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

func (p *Mailer) Html(to []string, subject, file string, arg interface{}, files ...string) {
	var buf bytes.Buffer
	var err error

	t := template.New("")
	t, err = t.ParseFiles(file)
	if err != nil {
		log.Error("Parse mailer template fail: %v", err)
		return
	}
	if err = t.Execute(&buf, arg); err != nil {
		log.Error("Parse mailer template fail: %v", err)
		return
	}
	if p.perform {
		msg := gomail.NewMessage()
		msg.SetHeader("From", p.from)
		msg.SetHeader("To", to...)
		if p.bcc != "" {
			msg.SetHeader("Bcc", p.bcc)
		}
		msg.SetHeader("Subject", subject)
		msg.SetBody("text/html", buf.String())
		for _, n := range files {
			f, e := gomail.OpenFile(n)
			if e != nil {
				log.Error("Faile to add attachment: %v", e)
				continue
			}
			msg.Attach(f)
		}

		if err := p.mailer.Send(msg); err != nil {
			log.Error("Error on send mail")
		}

	} else {
		log.Debug("Send mail to %v\nSubject: %s\nBody: \n %s", to, subject, buf.String())
	}
}

//
// func (p *Mailer) Text(to []string, subject, body string) {
//
// 	if p.perform {
// 		log.Printf("Send mail to %v: %s", to, subject)
// 		p.send(to, []byte(fmt.Sprintf("subject: %s\r\n\r\n%s", subject, body)))
// 	} else {
// 		log.Printf("Send mail to %v\nSubject: %s\nBody: \n %s", to, subject, body)
// 	}
//
// }
//
// func (p *Mailer) send(to []string, msg []byte) {
// 	if err := smtp.SendMail(p.addr, p.auth, p.from, to, msg); err != nil {
// 		log.Printf("error on sendmail: %v", err)
// 	}
// }
