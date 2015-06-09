

test:
	go test


format:
	go fmt base/*.go 
	go fmt platform/*.go
	go fmt wiki/*.go
	go fmt cms/*.go
	go fmt forum/*.go




clean:
	-rm -r tmp/*
