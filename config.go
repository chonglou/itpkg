package itpkg

import (
	"gopkg.in/yaml.v2"
	"io/ioutil"
	"os"
)

type Config struct {
	Secret string
	Http   struct {
		Port   int
		Cookie string
		Expire int
	}
	Database struct {
		Driver   string
		Host     string
		Port     int
		User     string
		Password string
		Name     string
		Ssl      string
	}
	Redis struct {
		Host string
		Port int
		Db   int
		Pool int
	}
}

var glConfig = Config{}

func loadConfig(file string) error {
	_, err := os.Stat(file)

	if err == nil {
		var yml []byte
		yml, err = ioutil.ReadFile(file)
		if err != nil {
			return err
		}
		err = yaml.Unmarshal(yml, &glConfig)
	} else {
		glConfig.Secret = string(Base64Encode(RandomBytes(512)))

		glConfig.Http.Port = 8080
		glConfig.Http.Cookie = RandomStr(8)
		glConfig.Http.Expire = 60 * 30

		glConfig.Database.Driver = "postgres"
		glConfig.Database.Host = "localhost"
		glConfig.Database.Port = 5432
		glConfig.Database.User = "postgres"
		glConfig.Database.Password = ""
		glConfig.Database.Name = "itpkg"
		glConfig.Database.Ssl = "disable"

		glConfig.Redis.Host = "localhost"
		glConfig.Redis.Port = 6379
		glConfig.Redis.Db = 0
		glConfig.Redis.Pool = 12

		var data []byte
		data, err = yaml.Marshal(&glConfig)
		if err != nil {
			return err
		}
		err = ioutil.WriteFile(file, data, 0600)
	}
	return err
}
