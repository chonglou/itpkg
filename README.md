IT-PACKAGE
--------------------------------

## Install golang

### Only go
    sudo pacman -S go
    export GOPATH=$HOME/workspace # Or append to your .bashrc or .zshrc file

### Using gvm
    bash < <(curl -s -S -L https://raw.githubusercontent.com/moovweb/gvm/master/binscripts/gvm-installer) # If you are using zsh just change bash with zsh
    # Restart your terminal session
    gvm listall # List all Go versions available for download
    gvm install go1.4.2 # Install go
    gvm list
    gvm use go1.4.2 --default # Using go1.4.2

## Install source
    go get github.com/chonglou/itpkg/platform

## Examples
  cd $GOPATH/src/github.com/chonglou/itpkg/platform
