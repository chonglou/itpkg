package itpkg_test

import (
	"crypto/aes"
	"github.com/chonglou/itpkg/base"
	"testing"
)

const hello = "Hello, IT-PACKAGE!!!"

func TestRandom(t *testing.T) {
	t.Logf("Random string: %s\t%s", itpkg.RandomStr(16), itpkg.RandomStr(16))
}

func TestHmac(t *testing.T) {
	key, _ := itpkg.RandomBytes(16)
	h := itpkg.Hmac{Key: key}

	dest1 := h.Sum([]byte(hello))
	dest2 := h.Sum([]byte(hello))

	t.Logf("HMAC1(%d): %x", len(dest1), dest1)
	t.Logf("HMAC2(%d): %x", len(dest2), dest2)
	if !h.Equal(dest1, dest2) {
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
	c, _ := aes.NewCipher(key)
	a := itpkg.Aes{Cip: c}

	dest1, iv1, _ := a.Encrypt([]byte(hello))
	dest2, iv2, _ := a.Encrypt([]byte(hello))
	t.Logf("AES1(%d, iv=%x): %s => %x", len(dest1), iv1, hello, dest1)
	t.Logf("AES2(%d, iv=%x): %s => %x", len(dest2), iv2, hello, dest2)

	src := a.Decrypt(dest1, iv1)
	if string(src) != hello {
		t.Errorf("val == %x, want %x", src, hello)
	}
}
