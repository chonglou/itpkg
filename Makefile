

format:
	go fmt base/*.go 
	go fmt platform/*.go
	go fmt wiki/*.go
	go fmt cms/*.go
	go fmt forum/*.go
	go fmt shop/*.go
	go fmt teamwork/*.go
	go fmt email/*.go
	go fmt ops/*.go
	go fmt deploy/*.go


update:
	go get -u all


clean:
	-rm -r tmp/*
