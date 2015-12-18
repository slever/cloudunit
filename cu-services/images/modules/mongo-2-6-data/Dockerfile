FROM ubuntu:12.04

# DEF DE MONGO USER
RUN groupadd -r mongodb && useradd -r -g mongodb mongodb

RUN apt-get install -y vim openssh-server cron
RUN mkdir /var/run/sshd
RUN mkdir -p /cloudunit/backup

LABEL module_name="mongo-data"
LABEL module_version="2.6.9"

ADD scripts /cloudunit/scripts
VOLUME /cloudunit/database

EXPOSE 22

