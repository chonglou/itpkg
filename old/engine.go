package itpkg

type Engine interface {
	Map()
	Mount()
	Migrate()
	Info() (name string, version string, desc string)
}
