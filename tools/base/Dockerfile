FROM base/archlinux:latest
MAINTAINER Jitang Zheng <jitang.zheng@gmail.com>

RUN pacman-key --populate archlinux
RUN pacman-key --refresh-keys
RUN pacman -Syu --noconfirm
RUN pacman-db-upgrade
RUN pacman -S --needed --noconfirm openssh net-tools vim nginx
RUN pacman -Scc --noconfirm
RUN cp /usr/share/vim/vim74/vimrc_example.vim ~/.vimrc
RUN systemctl enable sshd
RUN for s in sshd nginx;do systemctl enable $s;done

RUN echo 'root:changeme' | chpasswd

ADD mnt /mnt

EXPOSE 22 25 465 587 143 993 3306
USER root

CMD ["/sbin/init"]
