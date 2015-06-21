package itpkg

import (
	"bytes"
	"crypto/aes"
	"crypto/cipher"
	"crypto/hmac"
	"crypto/md5"
	c_rand "crypto/rand"
	"crypto/sha512"
	"encoding/base64"
	"encoding/gob"
	m_rand "math/rand"
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
	const letters = "0123456789abcdefghijklmnopqrstuvwxyz"
	buf := make([]byte, size)
	for i := range buf {
		buf[i] = letters[m_rand.Intn(len(letters))]
	}
	return string(buf)

}

func RandomBytes(size int) ([]byte, error) {
	b := make([]byte, size)
	if _, err := c_rand.Read(b); err != nil {
		return nil, err
	}
	return b, nil
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
	Key []byte `inject:"hmac key"` //32 bits
}

func (p *Hmac) Sum(src []byte) []byte {
	mac := hmac.New(sha512.New, p.Key)
	mac.Write(src)
	return mac.Sum(nil)
}

func (p *Hmac) Equal(src, dst []byte) bool {
	return hmac.Equal(src, dst)
}

type Aes struct {
	//16、24或者32位的[]byte，分别对应AES-128, AES-192或AES-256算法
	Cip cipher.Block `inject:"aes cipher"`
}

func (p *Aes) Encrypt(src []byte) ([]byte, []byte, error) {
	iv, err := RandomBytes(aes.BlockSize)
	if err != nil {
		return nil, nil, err
	}
	cfb := cipher.NewCFBEncrypter(p.Cip, iv)
	ct := make([]byte, len(src))
	cfb.XORKeyStream(ct, src)
	return ct, iv, nil

}

func (p *Aes) Decrypt(src, iv []byte) []byte {
	cfb := cipher.NewCFBDecrypter(p.Cip, iv)
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
