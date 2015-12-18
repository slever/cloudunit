FROM ubuntu:12.04

RUN apt-get update
RUN apt-get install -y vim openssh-server cron

RUN mkdir -p /var/run/sshd
RUN mkdir -p /cloudunit/backup

LABEL module_name "mysql-data"
LABEL module_version "5.5"

ADD scripts /cloudunit/scripts
VOLUME /cloudunit/database
EXPOSE 22

