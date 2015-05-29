IT-PACKAGE
--------------------------------

## Install

### gvm
    bash < <(curl -s -S -L https://raw.githubusercontent.com/moovweb/gvm/master/binscripts/gvm-installer) # If you are using zsh just change bash with zsh
    # Restart your terminal session
    gvm listall # List all Go versions available for download
    gvm install go1.4.2 # Install go
    gvm list
    gvm use go1.4.2 --default # Using go1.4.2

### itpkg
    go get github.com/chonglou/itpkg

## Examples
  cd ~/.gvm/pkgsets/go1.4.2/global/src/github.com/chonglou/itpkg/platform
