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

ADD mnt/pkg /tmp/pkg
RUN pacman -U --noconfirm /tmp/pkg/rabbitmq-3.5.4-1-x86_64.pkg.tar.xz \
/tmp/pkg/jdk-8u51-2-x86_64.pkg.tar.xz \
/tmp/pkg/gradle-2.5-1-any.pkg.tar.xz
RUN pacman -S --noconfirm elasticsearch
RUN pacman -Scc --noconfirm

RUN cp /usr/share/vim/vim74/vimrc_example.vim ~/.vimrc
RUN echo 'root:changeme' | chpasswd

USER postgres
RUN initdb --locale en_US.UTF-8 -D '/var/lib/postgres/data' && \
pg_ctl -D /var/lib/postgres/data start && \
sleep 5 && \
createdb itpkg
USER root

RUN git clone https://github.com/chonglou/itpkg.git && \
cd itpkg/app && gradle wrapper && ./gradlew installDist && \
cd ../front && npm install && npm run release

ADD mnt/etc /tmp/etc
RUN cp /tmp/etc/bashrc /root/.bash_profile && \
cp -r /tmp/etc/ssl /etc/nginx && \
cp /tmp/etc/itpkg.conf /etc/nginx/ && \
cp /tmp/etc/itpkg.service /usr/lib/systemd/system/ && \
echo -e "en_US.UTF-8 UTF-8\nzh_CN.UTF-8 UTF-8\nzh_TW.UTF-8 UTF-8\n" > /etc/locale.gen && \
locale-gen && \
sed -i '$ d' /etc/nginx/nginx.conf && \
echo -e "\tinclude itpkg.conf;\n}" >> /etc/nginx/nginx.conf && \
mkdir -p /opt/itpkg/app/build/install/app/tmp


RUN for s in sshd postgresql redis rabbitmq elasticsearch nginx itpkg;do systemctl enable $s;done


EXPOSE 22 443

CMD ["/sbin/init"]
