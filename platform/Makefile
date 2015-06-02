
build:
	mkdir -pv release
	gulp clean
	gulp build
	go build -o itpkg -ldflags "-s" app.go
	mv mailer public itpkg release/


clean:
	-rm -r tmp/* release/* mailer public

