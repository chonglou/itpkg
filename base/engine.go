package itpkg

type EngineSetup struct {
	cfg *Config
}

func (p *EngineSetup) Setup(cfg *Config) {
	p.cfg = cfg
}

func (p *EngineSetup) Use(name string, val interface{}) {
	p.cfg.beans[name] = val
}

func (p *EngineSetup) Get(name string) interface{} {
	return p.cfg.beans[name]
}

type Engine interface {
	Setup(cfg *Config)
	Map()
	Mount()
	Migrate()
	Info() (name string, version string, desc string)
}

var engines = make([]Engine, 0)

func Register(en Engine) {
	for _, v := range engines {
		if en == v {
			return
		}
	}
	engines = append(engines, en)
}
