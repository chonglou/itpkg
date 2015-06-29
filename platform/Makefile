target=build

release:
	npm run release
	go build -o $(target)/itpkg -ldflags "-s" app.go
	cp -r locales $(target)


clean:
	-rm -r build



