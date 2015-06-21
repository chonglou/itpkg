package itpkg_test

import (
	"github.com/chonglou/itpkg/base"
	"testing"
)

const hello = "Hello, IT-PACKAGE!!!"

func TestRandom(t *testing.T) {
	t.Logf("Random string: %s\t%s", itpkg.RandomStr(16), itpkg.RandomStr(16))
}

func TestHmac(t *testing.T) {
	key, _ := itpkg.RandomBytes(32)
	h := itpkg.Hmac{}
	h.SetKey(key)

	dest := h.Sum([]byte(hello))
	t.Logf("HMAC(%d): %x", len(dest), dest)
	if !h.Equal(h.Sum([]byte(hello)), dest) {
		t.Errorf("HMAC FAILED!")
	}
}

func TestMd5AndSha(t *testing.T) {
	t.Logf("MD5: %x", itpkg.Md5([]byte(hello)))
}

func TestBase64(t *testing.T) {
	dest := itpkg.Base64Encode([]byte(hello))
	t.Logf("Base64: %s => %x", hello, dest)
	src, err := itpkg.Base64Decode(dest)
	if err != nil || string(src) != hello {
		t.Errorf("val == %x, want %x", src, hello)
	}
}

func TestAes(t *testing.T) {
	key, _ := itpkg.RandomBytes(32)

	a := itpkg.Aes{}

	a.SetKey(key)

	dest, iv, _ := a.Encrypt([]byte(hello))
	t.Logf("AES(iv=%x): %s => %x", iv, hello, dest)

	src := a.Decrypt(dest, iv)
	if string(src) != hello {
		t.Errorf("val == %x, want %x", src, hello)
	}
}
