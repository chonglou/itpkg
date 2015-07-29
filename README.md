IT-PACKAGE
--------------------------------

## Using by docker

### For mac(https://docs.docker.com/installation/mac/)
    boot2docker init
    boot2docker start
    eval "$(boot2docker shellinit)"
### For linux
    pacman -S docker
    gpasswd -a YOUR_NAME docker # then need relogin.


### First run
    docker pull chonglou/itpkg
    docker run -d --name itpkg -p 2222:22 -p 443:443 -p 8080:8080 -P --privileged -v /sys/fs/cgroup:/sys/fs/cgroup:ro chonglou/itpkg:latest

### Other commands
    docker ps
    docker start itpkg         # start itpkg 
    docker stop itpkg          # stop itpkg
    firefox https://localhost  # open in web browser
    ssh -p 2222 root@localhost # password is changeme

## Using in local

### Needed packages

 * JDK8: http://www.oracle.com/technetwork/java/javase/downloads/index.html
 * JCE: http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
 * Gradle: https://gradle.org/
 * Redis: http://redis.io/
 * RabbitMQ: https://www.rabbitmq.com/
 * A database, recommend mysql(https://www.mysql.com/, https://www.percona.com/software/percona-server) or postgresql(http://www.postgresql.org/)
## Setup
#### Env
    mkdir local
    cd local
    tar xf /tmp/jdk-8u51-linux-x64.tar.gz
    ln -s jdk1.8.0_51 jdk
    unzip /tmp/gradle-2.5-all.zip
    ln -s gradle-2.5 gradle
    unzip /tmp/jce_policy-8.zip
    cp UnlimitedJCEPolicyJDK8/*.jar jdk/jre/lib/security/
    

#### Add to .bashrc or .zshrc
    JAVA_HOME=$HOME/local/jdk
    GRADLE_HOME=$HOME/local/gradle
    PATH=$JAVA_HOME/bin:$GRADLE_HOME/bin:$PATH
    export JAVA_HOME GRADLE_HOME PATH

#### Build
    cd app; more README.md     # backend
    cd front; more README.md   # frontend

