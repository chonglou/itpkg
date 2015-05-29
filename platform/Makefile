
build:
	mkdir -pv release
	gulp clean
	gulp build
	go build -o itpkg -ldflags "-s" app.go
	mv public itpkg release/


clean:
	-rm -r tmp/* release/*

