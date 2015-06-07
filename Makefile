

test:
	go test


format:
	go fmt base/*.go 
	go fmt wiki/*.go




clean:
	-rm -r tmp/*
