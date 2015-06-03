
build:
	mkdir -pv release
	gulp clean
	gulp build
	go build -o itpkg -ldflags "-s" app.go
	mv public itpkg release/
	cp -r locales release/


clean:
	-rm -r tmp/* release/* mailer public

