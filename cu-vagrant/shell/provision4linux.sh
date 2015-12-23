#!/bin/bash

export DEBIAN_FRONTEND=noninteractive
export LC_ALL=en_US.UTF-8
export LANG=en_US.UTF-8
export LANGUAGE=en_US.UTF-8

readonly COMPOSE_VERSION=1.5.1
readonly MACHINE_VERSION=v0.5.1

#==========================================================#

cp -f cloudunit/cu-vagrant/files/.bashrc /home/vagrant/.bashrc
sudo apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys 58118E89F3A912897C070ADBF76221572C52609D
sudo cp -f cloudunit/cu-vagrant/files/sources.list /etc/apt/sources.list
sudo apt-get update
sudo apt-get -y upgrade
sudo apt-get install -y apt-transport-https
sudo apt-get install -y curl
sudo apt-get install -y golang

#==========================================================#

# clean up
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

# configure docker for systemd
sudo cp cloudunit/cu-vagrant/files/docker.secure.service  /lib/systemd/system/docker.service
sudo mkdir /root.docker
sudo cp cloudunit/cu-vagrant/certificats/ca.pem /root/.docker
sudo cp cloudunit/cu-vagrant/certificats/ca.pem
sudo cp cloudunit/cu-vagrant/certificats/server-* /root/.docker

# configure docker for systemd
sudo cp cloudunit/cu-vagrant/files/docker.socket   /lib/systemd/system/

# enabled when booting
sudo systemctl enable docker
sudo systemctl start  docker

# enable memory and swap accounting
sudo cp cloudunit/cu-vagrant/files/grub /etc/default/grub
sudo update-grub

# install Docker Compose
# @see http://docs.docker.com/compose/install/
curl -o docker-compose -L https://github.com/docker/compose/releases/download/$COMPOSE_VERSION/docker-compose-`uname -s`-`uname -m`
chmod a+x docker-compose
sudo mv docker-compose /usr/local/bin

# install Docker Machine
# @see https://docs.docker.com/machine/
sudo curl -L https://github.com/docker/machine/releases/download/$MACHINE_VERSION/docker-machine_linux-amd64 > docker-machine
chmod +x docker-machine
sudo mv docker-machine /usr/local/bin/

# install swarm
sudo docker pull swarm

# install docker-bench-security
#docker pull diogomonica/docker-bench-security
#sudo cp cloudunit/cu-vagrant/files/docker-bench-security /usr/local/bin
#chmod a+x /usr/local/bin/docker-bench-security

sudo mkdir /opt/cloudunit
cd /home/vagrant/cloudunit/cu-services && sudo ./build-images.sh
