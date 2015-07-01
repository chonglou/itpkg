IT-PACKAGE(PLATFORM)
--------------------------------

## Build
    pacman -S npm
    npm update -g
    npm install -g webpack
    npm install
    make clean
    make

## Notes
    npm view <pkgname> versions
    npm -g ls | grep -v 'npm@' | awk '/@/ {print $2}' | awk -F@ '{print $1}' | xargs sudo npm -g rm


## Running on production
    cd public
    ./itpkg -h
    ./itpkg db:create -e production # create database
    ./itpkg db:migrate -e production # migrate database
    ./itpkg db:seed -e production # insert seed data to database
    ./itpkg server -e production  # production env


## Development env
    go run app.go db:create
    go run app.go db:migrate
    go run app.go db:seed
    npm run server 
    npm run build
    npm start
    open http://localhost:8080


