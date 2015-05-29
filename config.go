package itpkg

import (
	"fmt"
	"gopkg.in/yaml.v2"
	"io/ioutil"
	"log"
	"os"
	"strconv"
)

type Config struct {
	Engines []string
	Secret  string
	Http    struct {
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

func (p *Config) DbUrl() string {
	return fmt.Sprintf(
		"%s://%s:%s@%s:%d/%s?sslmode=%s",
		p.Database.Driver, p.Database.User, p.Database.Password, p.Database.Host,
		p.Database.Port, p.Database.Name, p.Database.Ssl)
}

func (p *Config) DbShell() (string, []string) {
	d := p.Database.Driver
	switch d {
	case "postgres":
		return "psql", []string{
			"-h", p.Database.Host,
			"-p", strconv.Itoa(p.Database.Port),
			"-d", p.Database.Name,
			"-U", p.Database.User}
	default:
		return "echo", []string{"Unknown database driver " + d}
	}
}

func (p *Config) RedisShell() (string, []string) {
	return "telnet", []string{p.Redis.Host, strconv.Itoa(p.Redis.Port)}
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
		log.Printf("Load from config file: %s", file)
		err = yaml.Unmarshal(yml, &glConfig)
	} else {
		glConfig.Engines = []string{"auth", "forum", "wiki", "shop"}
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
		log.Printf("Generate config file: %s", file)
		err = ioutil.WriteFile(file, data, 0600)
	}
	return err
}
