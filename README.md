IT-PACKAGE
--------------------------------

## Needed packages

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

