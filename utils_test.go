package itpkg

import (
	"testing"
)

const hello = "Hello, IT-PACKAGE!!!"

// func TestUuid(t *testing.T) {
// 	log.Info("==================UTILS=============================")
// 	log.Info("UUID: %s\t%s", Uuid(), Uuid())
// }

func TestRandom(t *testing.T) {
	log.Info("Random string: %s\t%s", RandomStr(16), RandomStr(16))
}

func TestHmac(t *testing.T) {
	key := RandomBytes(32)
	h := Hmac{key: key}
	dest := h.Sum([]byte(hello))
	log.Info("HMAC(%d): %x", len(dest), dest)
	if !h.Equal(h.Sum([]byte(hello)), dest) {
		t.Errorf("HMAC FAILED!")
	}
}

func TestMd5AndSha(t *testing.T) {
	log.Info("MD5: %x", Md5([]byte(hello)))
}

func TestBase64(t *testing.T) {
	dest := Base64Encode([]byte(hello))
	log.Info("Base64: %s => %x", hello, dest)
	src, err := Base64Decode(dest)
	if err != nil || string(src) != hello {
		t.Errorf("val == %x, want %x", src, hello)
	}
}

func TestAes(t *testing.T) {
	key := RandomBytes(32)

	a := Aes{}

	a.Init(key)

	dest, iv := a.Encrypt([]byte(hello))
	log.Info("AES(iv=%x): %s => %x", iv, hello, dest)

	src := a.Decrypt(dest, iv)
	if string(src) != hello {
		t.Errorf("val == %x, want %x", src, hello)
	}
}
