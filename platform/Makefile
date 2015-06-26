
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


3rd:
	mkdir -pv public/node_modules/react/dist/
	cp -v  node_modules/react/dist/react-with-addons.min.js  public/node_modules/react/dist/react-with-addons.min.js


