IT-PACKAGE(executable jar package)
===============================

## Run local tomcat
    gradle cargoRunLocal # Start tomcat on port 8080


## War mode
    gradle build
    ls -lh build/libs/itpkg-*.war


## Jar mode
    gradle wrapper
    ./gradlew installDist
    cd build/install/app
    ./bin/itpkg




