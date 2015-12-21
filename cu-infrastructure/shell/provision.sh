#!/bin/bash

export DEBIAN_FRONTEND=noninteractive
export LC_ALL=en_US.UTF-8
export LANG=en_US.UTF-8
export LANGUAGE=en_US.UTF-8

readonly COMPOSE_VERSION=1.5.1
readonly MACHINE_VERSION=v0.5.1

#==========================================================#

#
# error handling
#

catch_error() {
    echo { \"status\": $RETVAL, \"error_line\": $BASH_LINENO }
}

trap 'RETVAL=$?; echo "ERROR"; catch_error '  ERR
trap 'RETVAL=$?; echo "received signal to stop";  catch_error ' SIGQUIT SIGTERM SIGINT

#==========================================================#

cp -f cloudunit/cu-infrastructure/files/.bashrc /home/vagrant/.bashrc
sudo apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys 58118E89F3A912897C070ADBF76221572C52609D
sudo cp -f cloudunit/cu-infrastructure/files/sources.list /etc/apt/sources.list
sudo apt-get update
sudo apt-get -y upgrade
sudo apt-get install -y apt-transport-https
sudo apt-get install -y curl unzip
sudo apt-get install -y golang

#==========================================================#

#---------------------------------------#
# fix base box
#

sudo cat <<-HOSTNAME > /etc/hostname
  cloudunit.dev
HOSTNAME

cat <<-EOBASHRC  >> /home/vagrant/.bashrc
  export PS1='\[\033[01;32m\]\u@\h\[\033[01;34m\] \w \$\[\033[00m\] '
  export LC_CTYPE=C.UTF-8
EOBASHRC

# clean up
sudo apt-get clean
sudo rm -f \
  /var/log/messages   \
  /var/log/lastlog    \
  /var/log/auth.log   \
  /var/log/syslog     \
  /var/log/daemon.log \
  /var/log/docker.log \
  /home/vagrant/.bash_history \
  /var/mail/vagrant           \
  || true

#==========================================================#

#
# Docker-related stuff
#
sudo apt-get update
sudo apt-get install -y docker-engine
sudo gpasswd -a vagrant docker

# configure for systemd
sudo cp cloudunit/cu-infrastructure/files/docker.service  /lib/systemd/system/
sudo cp cloudunit/cu-infrastructure/files/docker.socket   /lib/systemd/system/

# enabled when booting
sudo systemctl enable docker
sudo systemctl start  docker

# enable memory and swap accounting
sudo cp cloudunit/cu-infrastructure/files/grub /etc/default/grub
sudo update-grub

# install Docker Compose (was: Fig)
# @see http://docs.docker.com/compose/install/
curl -o docker-compose -L https://github.com/docker/compose/releases/download/$COMPOSE_VERSION/docker-compose-`uname -s`-`uname -m`
chmod a+x docker-compose
sudo mv docker-compose /usr/local/bin

# install Docker Machine
# @see https://docs.docker.com/machine/
curl -o docker-machine.zip -L https://github.com/docker/machine/releases/download/$MACHINE_VERSION/docker-machine_linux-amd64.zip
unzip docker-machine.zip
rm docker-machine.zip
chmod a+x docker-machine*
sudo mv docker-machine* /usr/local/bin

# install swarm
sudo docker pull swarm

# install docker-bench-security
docker pull diogomonica/docker-bench-security
sudo cp cloudunit/cu-infrastructure/files/docker-bench-security /usr/local/bin
chmod a+x /usr/local/bin/docker-bench-security

docker pull cloudunit/manager:master
docker pull cloudunit/manager:master