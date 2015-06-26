
build:
	mkdir -pv release
	gulp clean
	gulp build
	go build -o itpkg -ldflags "-s" app.go
	mv public itpkg release/
	cp -r locales release/


watch:
	webpack --progress --colors --watch


clean:
	-rm -r tmp/* release public

