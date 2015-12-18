FROM ubuntu:12.04

# MISE A JOUR DISTRIBUTION ET INSTALLATION OPEN-SSH
RUN apt-get update && apt-get install -y vim openssh-server curl expect netcat

# CREATION DES REPERTOIRES POUR LES ARCHIVES DES PROGRAMMES

RUN mkdir -p /cloudunit/java /cloudunit/scripts /cloudunit/binaries /cloudunit/tmp /cloudunit/tools /cloudunit/appconf /cloudunit/software /var/run/sshd

# VARIABLES ENVIRONNEMENTS

ENV CU_JAVA /cloudunit/java
ENV CU_SCRIPTS /cloudunit/scripts
ENV CU_USER_HOME /cloudunit/home

RUN echo -n "CU_JAVA=$CU_JAVA\nCU_SCRIPTS=$CU_SCRIPTS\nCU_USER_HOME=$CU_USER_HOME\n" >> /etc/environment

# AGENT Cloud Unit pour mettre à jour le status en fin de démarrage (ssh)
RUN wget https://github.com/Treeptik/cloudunit/releases/download/1.0/cloudunitAgent-1.0-SNAPSHOT.jar -O /cloudunit/tools/cloudunitAgent-1.0-SNAPSHOT.jar


# AJOUT DE LA VERSION DE TOMCAT
RUN wget https://github.com/Treeptik/cloudunit/releases/download/1.0/tomcat-appconf7.tar.gz -O /tmp/tomcat-appconf7.tar.gz \
	&& tar -xvf /tmp/tomcat-appconf7.tar.gz -C /cloudunit && rm /tmp/tomcat-appconf7.tar.gz

# ajout du dossier lib-ext

RUN mkdir -p /cloudunit/appconf/lib-ext
RUN mkdir -p /cloudunit/appconf/scripts

ADD scripts/ /cloudunit/appconf/scripts
RUN chmod +x /cloudunit/appconf/scripts/*

LABEL upgrade="commit"

# EXPOSE PORT
EXPOSE 22

VOLUME /cloudunit/appconf/logs

ENTRYPOINT ["/cloudunit/scripts/start-service.sh"]

