

test:
	go test


clean:
	-rm -r tmp/* 


vet:
	go vet *.go
	go vet platform/*.go


fmt:
	go fmt *.go
	go fmt platform/*.go
