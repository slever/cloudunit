FROM ubuntu:14.04

# MISE A JOUR DISTRIBUTION ET INSTALLATION OPEN-SSH
RUN apt-get update && apt-get install -y vim openssh-server curl expect netcat

# CREATION DES REPERTOIRES POUR LES ARCHIVES DES PROGRAMMES

RUN mkdir -p /cloudunit/java
RUN mkdir -p /cloudunit/scripts
RUN mkdir -p /cloudunit/binaries
RUN mkdir -p /cloudunit/tmp
RUN mkdir -p /cloudunit/tools
RUN mkdir -p /cloudunit/appconf
RUN mkdir -p /cloudunit/software /var/run/sshd

# VARIABLES ENVIRONNEMENTS

ENV CU_JAVA         /cloudunit/java
ENV CU_SCRIPTS      /cloudunit/scripts
ENV CU_USER_HOME    /cloudunit/

RUN echo -n "CU_JAVA=$CU_JAVA\nCU_SCRIPTS=$CU_SCRIPTS\nCU_USER_HOME=$CU_USER_HOME\n" >> /etc/environment

# AGENT Cloud Unit pour mettre à jour le status en fin de démarrage (ssh)
RUN wget https://github.com/Treeptik/cloudunit/releases/download/1.0/cloudunitAgent-1.0-SNAPSHOT.jar -O /cloudunit/tools/cloudunitAgent-1.0-SNAPSHOT.jar

# add our user and group first to make sure their IDs get assigned consistently, regardless of whatever dependencies get added
RUN groupadd -r mongodb && useradd -r -g mongodb mongodb

RUN curl -sL https://deb.nodesource.com/setup | sudo bash -

RUN apt-get update \
	&& apt-get install -y numactl nodejs \ 
	&& rm -rf /var/lib/apt/lists/*

# grab gosu for easy step-down from root
RUN gpg --keyserver pool.sks-keyservers.net --recv-keys B42F6819007F00F88E364FD4036A9C25BF357DD4
RUN curl -o /usr/local/bin/gosu -SL "https://github.com/tianon/gosu/releases/download/1.2/gosu-$(dpkg --print-architecture)" \
	&& curl -o /usr/local/bin/gosu.asc -SL "https://github.com/tianon/gosu/releases/download/1.2/gosu-$(dpkg --print-architecture).asc" \
	&& gpg --verify /usr/local/bin/gosu.asc \
	&& rm /usr/local/bin/gosu.asc \
	&& chmod +x /usr/local/bin/gosu

# INSTALL DE MONGO
ENV MONGO_RELEASE_FINGERPRINT DFFA3DCF326E302C4787673A01C4E7FAAAB2461C
RUN gpg --keyserver pool.sks-keyservers.net --recv-keys $MONGO_RELEASE_FINGERPRINT

ENV MONGO_VERSION 2.6.9

RUN mkdir -p /cloudunit/database 
RUN wget https://github.com/Treeptik/cloudunit/releases/download/1.0/mongo.tgz -O /tmp/mongo.tgz
RUN tar -xvf /tmp/mongo.tgz -C /usr/local --strip-components=1

# CONFIG DE MONGO
RUN mkdir /mongo && chown -R mongodb:mongodb /mongo
COPY user.js /mongo/user.js

# INSTALLATION DE MONGO ADMIN UI
RUN wget https://github.com/Treeptik/cloudunit/releases/download/1.0/mms-v1.7.0-community-linux.tar.gz -O /tmp/mms-v1.7.0-community-linux.tar.gz
RUN tar -xvf /tmp/mms-v1.7.0-community-linux.tar.gz -C /tmp
RUN tar -xvf /tmp/mms-v1.7.0-community-linux-x86_64.tar.gz
RUN cd mms-v1.7.0-community-linux-x86_64 && ./install.sh

# CONFIG DE L'UI
COPY config.js /opt/lx-mms/config.js

# Ajout des scripts utile aux managers et au démarrage container                
ADD scripts /cloudunit/scripts/

# SSH
EXPOSE 22

ENTRYPOINT ["/bin/bash", "/cloudunit/scripts/start-service.sh"]
