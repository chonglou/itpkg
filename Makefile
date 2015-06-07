

test:
	go test


format:
	go fmt base/*.go 
	go fmt wiki/*.go
	go fmt platform/*.go




clean:
	-rm -r tmp/*
