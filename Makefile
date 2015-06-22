

format:
	go fmt base/*.go 
	go fmt platform/*.go
	go fmt wiki/*.go
	go fmt cms/*.go
	go fmt forum/*.go
	go fmt shop/*.go
	go fmt teamwork/*.go




clean:
	-rm -r tmp/*
