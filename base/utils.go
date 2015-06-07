package itpkg

import (
	"bytes"
	"crypto/aes"
	"crypto/cipher"
	"crypto/hmac"
	"crypto/md5"
	"crypto/rand"
	"crypto/sha512"
	"encoding/base64"
	"encoding/gob"
	"os"
	"os/exec"
	"syscall"
)

//-----------------------------TOOLS--------------------------------------------

func Shell(cmd string, args ...string) error {
	bin, err := exec.LookPath(cmd)
	if err != nil {
		return err
	}
	return syscall.Exec(bin, append([]string{cmd}, args...), os.Environ())
}

func RandomStr(size int) string {
	b := RandomBytes(size)
	const dictionary = "0123456789abcdefghijklmnopqrstuvwxyz"
	for k, v := range b {
		b[k] = dictionary[v%byte(len(dictionary))]
	}
	return string(b)

}

func RandomBytes(size int) []byte {
	b := make([]byte, size)
	if _, err := rand.Read(b); err != nil {
		Logger.Fatalf("Error on random: %v", err)
	}
	return b
}

//--------------------------ENCRYPTS--------------------------------------------

func Md5(src []byte) [16]byte {
	return md5.Sum(src)
}

func Base64Encode(src []byte) []byte {
	return []byte(base64.StdEncoding.EncodeToString(src))
}

func Base64Decode(src []byte) ([]byte, error) {
	return base64.StdEncoding.DecodeString(string(src))
}

type Hmac struct {
	key []byte
}

func (h *Hmac) Sum(src []byte) []byte {
	mac := hmac.New(sha512.New, h.key)
	mac.Write(src)
	return mac.Sum(nil)
}

func (h *Hmac) Equal(src, dst []byte) bool {
	return hmac.Equal(src, dst)
}

type Aes struct {
	cip cipher.Block
}

//16、24或者32位的[]byte，分别对应AES-128, AES-192或AES-256算法
func (a *Aes) Init(key []byte) error {
	c, e := aes.NewCipher(key)
	if e != nil {
		return e
	}
	a.cip = c
	return nil
}

func (a *Aes) Encrypt(src []byte) ([]byte, []byte) {
	iv := RandomBytes(aes.BlockSize)
	cfb := cipher.NewCFBEncrypter(a.cip, iv)
	ct := make([]byte, len(src))
	cfb.XORKeyStream(ct, src)
	return ct, iv

}

func (a *Aes) Decrypt(src, iv []byte) []byte {
	cfb := cipher.NewCFBDecrypter(a.cip, iv)
	pt := make([]byte, len(src))
	cfb.XORKeyStream(pt, src)
	return pt
}

func Obj2bits(obj interface{}) ([]byte, error) {
	var buf bytes.Buffer
	enc := gob.NewEncoder(&buf)
	err := enc.Encode(obj)
	if err != nil {
		return nil, err
	}
	return buf.Bytes(), nil
}

func Bits2obj(data []byte, obj interface{}) error {
	var buf bytes.Buffer
	dec := gob.NewDecoder(&buf)
	buf.Write(data)
	err := dec.Decode(obj)
	if err != nil {
		return err
	}
	return nil
}