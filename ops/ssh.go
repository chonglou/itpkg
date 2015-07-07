package main

import (
	"bytes"
	"fmt"
	"io/ioutil"
	"os"

	"golang.org/x/crypto/ssh"
)

type Ssh struct {
	client *ssh.Client
}

func (p *Ssh) Run(cmd string) error {
	ss, err := p.client.NewSession()
	if err != nil {
		return err
	}
	defer ss.Close()
	var buf bytes.Buffer
	ss.Stdout = &buf
	if err = ss.Run(cmd); err != nil {
		return err
	}
	fmt.Println(buf.String())
	return nil
}

func Open(user, host string, port int, key string) (*Ssh, error) {
	if port == 0 {
		port = 22
	}
	if key == "" {
		key = fmt.Sprintf("%s/.ssh/id_rsa", os.Getenv("HOME"))
	}

	pem, e := ioutil.ReadFile(key)

	var signer ssh.Signer
	signer, e = ssh.ParsePrivateKey(pem)
	if e != nil {
		return nil, e
	}

	var c *ssh.Client
	c, e = ssh.Dial(
		"tcp",
		fmt.Sprintf("%s:%d", host, port),
		&ssh.ClientConfig{
			User: user,
			Auth: []ssh.AuthMethod{
				ssh.PublicKeys(signer),
			},
		},
	)
	if e != nil {
		return nil, e
	}
	return &Ssh{client: c}, nil
}
