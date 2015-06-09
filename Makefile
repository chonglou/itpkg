

test:
	go test


format:
	go fmt base/*.go 
	go fmt platform/*.go
	go fmt wiki/*.go
	go fmt cms/*.go




clean:
	-rm -r tmp/*
