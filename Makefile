

test:
	go test


format:
	go fmt *.go
	go fmt platform/*.go




clean:
	-rm -r tmp/*
