package itpkg

import (
	"errors"
	"fmt"
	"time"

	"github.com/dgrijalva/jwt-go"
	"github.com/garyburd/redigo/redis"
	"github.com/op/go-logging"
	"github.com/pborman/uuid"
)

type Token struct {
	Redis  *redis.Pool     `inject:""`
	Key    []byte          `inject:"token key"` //16 bits
	Logger *logging.Logger `inject:""`
}

func (p *Token) tid(kid string) string {
	return fmt.Sprintf("token://%s", kid)
}

func (p *Token) New(data interface{}, dur time.Duration) (string, error) {
	kid := uuid.New()
	token := jwt.New(jwt.SigningMethodHS512)
	token.Header["kid"] = kid
	token.Claims["val"] = data
	token.Claims["exp"] = time.Now().Add(dur).Unix()

	key, err := RandomBytes(16)
	if err != nil {
		return "", err
	}
	conn := p.Redis.Get()
	defer conn.Close()
	if _, err := conn.Do("SET", p.tid(kid), key, "EX", int(dur.Seconds())); err != nil {
		p.Logger.Error("Set token key error: %v", err)
		return "", err
	}
	return token.SignedString(append(key, p.Key...))
}

func (p *Token) Parse(token string) (interface{}, error) {
	tk, err := jwt.Parse(token, func(tk *jwt.Token) (interface{}, error) {
		conn := p.Redis.Get()
		defer conn.Close()
		val, err := conn.Do("GET", p.tid(tk.Header["kid"].(string)))
		if err != nil {
			p.Logger.Error("Get token key error: %v", err)
			return nil, err
		}
		return append(val.([]byte), p.Key...), nil
	})

	if err != nil {
		return nil, err
	}
	if tk.Valid {
		return tk.Claims["val"], nil
	}
	return nil, errors.New("Invalid token")
}
