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
    npm install <pkgname> --save
    npm install <pkgname> --save-dev    
    npm update --save
    npm view <pkgname> versions
    npm -g ls | grep -v 'npm@' | awk '/@/ {print $2}' | awk -F@ '{print $1}' | xargs sudo npm -g rm



## Development
    npm run build
    npm start
    open http://localhost:8088


