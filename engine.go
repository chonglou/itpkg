package itpkg

type Engine interface {
	Mount()
	Migrate()
	Info() (name string, version string, desc string)
}
