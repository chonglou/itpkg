IT-PACKAGE(PLATFORM)
--------------------------------

## Build
    pacman -S npm
    npm update -g
    npm install -g webpack
    npm install
    bower install
    make clean
    make

## Notes
    npm view <pkgname> versions
    npm -g ls | grep -v 'npm@' | awk '/@/ {print $2}' | awk -F@ '{print $1}' | xargs sudo npm -g rm


## Running
    cd public
    ./itpkg -h
    ./itpkg db:create -e production # create database
    ./itpkg db:migrate -e production # migrate database
    ./itpkg server -e production  # production env


## Development
    go run app.go db:create
    go run app.go db:migrate
    go run app.go server 
