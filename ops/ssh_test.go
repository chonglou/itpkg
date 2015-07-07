package main_test

import (
	ops "github.com/chonglou/itpkg/ops"

	"testing"
)

var host = "192.168.1.108"
var user = "deploy"

func TestSsh(t *testing.T) {
	s, e := ops.Open(user, host, 0, "")
	if e != nil {
		t.Errorf("Ssh login failed: %v", e)
	}

	if e = s.Run("uname -a"); e != nil {
		t.Errorf("Ssh run failed: %v", e)
	}
}
