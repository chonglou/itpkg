package itpkg

import (
	"testing"
)

func TestToken(t *testing.T) {
	log.Info("==================Token=============================")
	tk := Token{key: RandomBytes(32)}
	ts1, _ := tk.New(hello)
	ts2, _ := tk.New(hello)
	log.Info("%s\n%s\n%s", hello, ts1, ts2)

	h1, e1 := tk.Parse(ts1)
	h2, e2 := tk.Parse(ts2)
	if h1 != hello || h2 != hello || e1 != nil || e2 != nil {
		t.Errorf("Token FAILED!")
	}

}
