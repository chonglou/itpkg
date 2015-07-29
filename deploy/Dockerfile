FROM base/archlinux:latest
MAINTAINER Jitang Zheng <jitang.zheng@gmail.com>

USER root
WORKDIR /opt

RUN pacman-key --populate archlinux
RUN pacman-key --refresh-keys
RUN pacman -Syu --noconfirm
RUN pacman-db-upgrade
RUN pacman -S --needed --noconfirm base-devel git wget unzip net-tools vim npm libpng \
nginx redis openssh postgresql
RUN pacman -Scc --noconfirm
RUN cp /usr/share/vim/vim74/vimrc_example.vim ~/.vimrc
RUN su - postgres -c "initdb --locale en_US.UTF-8 -D '/var/lib/postgres/data'"

ADD tmp/pkg /tmp/pkg
RUN pacman -U --noconfirm /tmp/pkg/rabbitmq-3.5.4-1-x86_64.pkg.tar.xz \
/tmp/pkg/jdk-8u51-2-x86_64.pkg.tar.xz \
/tmp/pkg/gradle-2.5-1-any.pkg.tar.xz

RUN git clone https://github.com/chonglou/itpkg.git && \
cd itpkg/app && gradle wrapper && ./gradlew installDist && \
cd ../front && npm install && npm run release

ADD tmp/etc /tmp/etc
RUN cp /tmp/etc/bashrc /root/.bash_profile && \
cp -r /tmp/etc/ssl /etc/nginx && \
cp /tmp/etc/itpkg.conf /etc/nginx/ && \
cp /tmp/etc/itpkg.service /usr/lib/systemd/system/

RUN for s in sshd mysqld redis rabbitmq nginx itpkg;do systemctl enable $s;done
RUN echo 'root:changeme' | chpasswd

EXPOSE 22 25 443 465 587 143 993 3306 8080

CMD ["/sbin/init"]
