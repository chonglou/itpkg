IT-PACKAGE(PLATFORM)
--------------------------------

## Build
    pacman -S npm
    npm install --global gulp
    npm install
    make clean
    make

## Notes
    npm view <pkgname> versions


## Running
    cd public
    ./itpkg -h
    ./itpkg server # development env
    ./itpkg server -e production  # production env
    
