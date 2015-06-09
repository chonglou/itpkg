IT-PACKAGE(PLATFORM)
--------------------------------

## Build
    pacman -S npm
    npm install --global gulp
    npm install --global bower
    npm install
    bower install
    make clean
    make

## Notes
    npm view <pkgname> versions
    gulp -T


## Running
    cd public
    ./itpkg -h
    ./itpkg server # development env
    ./itpkg server -e production  # production env


## Development
    gulp
    go run app.go s
