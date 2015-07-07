FROM base/archlinux:latest
MAINTAINER Jitang Zheng <jitang.zheng@gmail.com>

RUN pacman-key --populate archlinux
RUN pacman-key --refresh-keys
RUN pacman -Syu --noconfirm
RUN pacman-db-upgrade
RUN pacman -S --needed --noconfirm openssh percona-server percona-server-clients postfix dovecot net-tools pwgen expect vim
RUN pacman -Scc --noconfirm
RUN systemctl start mysqld
RUN cp /usr/share/vim/vim74/vimrc_example.vim ~/.vimrc
RUN for s in sshd mysqld postfix dovecot; do systemctl enable $s; done
#RUN sed -i "s/#PermitRootLogin/PermitRootLogin/g" /etc/ssh/sshd_config 
RUN echo 'root:changeme' | chpasswd

ADD mnt /mnt
RUN sh /mnt/install.sh > /install.log 2>&1
RUN rm /mnt/*


EXPOSE 22 25 465 587 143 993 3306
USER root

CMD ["/sbin/init"]
