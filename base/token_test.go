package itpkg_test

import (
	"github.com/chonglou/itpkg/base"
	"github.com/garyburd/redigo/redis"
	"testing"
	"time"
)

func getRedis() *redis.Pool {
	return &redis.Pool{
		Dial: func() (redis.Conn, error) {
			c, err := redis.Dial("tcp", "localhost:6379")
			if err != nil {
				return nil, err
			}
			if _, err = c.Do("SELECT", 6); err != nil {
				return nil, err
			}
			return c, err
		},
	}
}

func TestToken(t *testing.T) {
	key, _ := itpkg.RandomBytes(16)
	tk := itpkg.Token{Redis: getRedis(), Key: key}
	ts1, _ := tk.New(hello, time.Hour*1)
	ts2, _ := tk.New(hello, time.Hour*2)
	t.Logf("%s\n%s\n%s", hello, ts1, ts2)
	if ts1 == ts2 {
		t.Errorf("Token ERROR!")
	}

	// h1, e1 := tk.Parse(ts1)
	// h2, e2 := tk.Parse(ts2)
	// if h1 != hello || h2 != hello || e1 != nil || e2 != nil {
	// 	t.Errorf("Token FAILED!%s", e1)
	// }

}
