package itpkg

import (
	"fmt"
	"github.com/op/go-logging"
)

type Seed struct {
	AuthDao *AuthDao        `inject:""`
	I18n    *LocaleDao      `inject:""`
	Logger  *logging.Logger `inject:""`
	Cfg     *Configuration  `inject:""`
}

func (p *Seed) run() {
	root, err := p.AuthDao.AddEmailUser(
		fmt.Sprintf("root@%s", p.Cfg.Http.Host),
		"root",
		"changeme")
	if err != nil {
		p.Logger.Fatalf("error on init user root: %v", err)
	}
	p.AuthDao.ConfirmUser(root.ID)

}
