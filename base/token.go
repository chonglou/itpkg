package itpkg

import (
	"errors"
	"fmt"
	"github.com/dgrijalva/jwt-go"
	"github.com/garyburd/redigo/redis"
	"github.com/pborman/uuid"
	"time"
)

type Token struct {
	redis *redis.Pool
	key   []byte //16 bits
}

func (p *Token) tid(kid string) string {
	return fmt.Sprintf("token://%s", kid)
}

func (p *Token) New(data interface{}, hours uint) (string, error) {
	kid := uuid.New()
	token := jwt.New(jwt.SigningMethodHS512)
	token.Header["kid"] = kid
	token.Claims["val"] = data
	token.Claims["exp"] = time.Now().Add(time.Hour * time.Duration(hours)).Unix()

	key := RandomBytes(16)
	conn := p.redis.Get()
	defer conn.Close()
	if _, err := conn.Do("SET", p.tid(kid), key, "EX", hours*60*60+3); err != nil {
		Log.Error("Set token key error: %v", err)
		return "", err
	}
	return token.SignedString(append(key, p.key...))
}

func (p *Token) Parse(token string) (interface{}, error) {
	tk, err := jwt.Parse(token, func(tk *jwt.Token) (interface{}, error) {
		conn := p.redis.Get()
		defer conn.Close()
		val, err := conn.Do("GET", p.tid(tk.Header["kid"].(string)))
		if err != nil {
			Log.Error("Get token key error: %v", err)
			return nil, err
		}
		return append(val.([]byte), p.key...), nil
	})

	if err != nil {
		return nil, err
	}
	if tk.Valid {
		return tk.Claims["val"], nil
	}
	return nil, errors.New("Invalid token")
}
