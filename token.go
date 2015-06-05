package itpkg

import (
	"errors"
	"github.com/dgrijalva/jwt-go"
	"time"
)

type Token struct {
	key []byte
}

func (p *Token) New(data interface{}) (string, error) {
	token := jwt.New(jwt.SigningMethodHS256)
	token.Claims["data"] = data
	token.Claims["exp"] = time.Now().Add(time.Hour * 1).Unix()

	return token.SignedString(p.key)
}

func (p *Token) Parse(token string) (interface{}, error) {
	tk, err := jwt.Parse(token, func(tk *jwt.Token) (interface{}, error) {
		return p.key, nil
	})
	if err != nil {
		return nil, err
	}
	if tk.Valid {
		return tk.Claims["data"], nil
	}
	return nil, errors.New("Invalid token")
}
