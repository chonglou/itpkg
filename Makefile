


format:
	go fmt *.go
	go fmt platform/*.go
	#go vet *.go
	#go vet platform/*.go



test:
	go test


clean:
	-rm -r tmp/*
