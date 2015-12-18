FROM ubuntu:12.04

# MISE A JOUR DISTRIBUTION ET INSTALLATION OPEN-SSH
RUN apt-get update && apt-get install -y vim openssh-server curl expect netcat

# CREATION DES REPERTOIRES POUR LES ARCHIVES DES PROGRAMMES
RUN mkdir -p /cloudunit/java
RUN mkdir -p /cloudunit/scripts
RUN mkdir -p /cloudunit/binaries
RUN mkdir -p /cloudunit/tmp
RUN mkdir -p /cloudunit/tools
RUN mkdir -p /cloudunit/appconf
RUN mkdir -p /cloudunit/software
RUN mkdir -p /cloudunit/database
RUN mkdir -p /cloudunit/backup
RUN mkdir -p /var/run/sshd

# VARIABLES ENVIRONNEMENTS

ENV CU_JAVA /cloudunit/java
ENV CU_SCRIPTS /cloudunit/scripts
ENV CU_USER_HOME /cloudunit/home
ENV CU_DATABASE_HOME /cloudunit/database
ENV DEBIAN_FRONTEND noninteractive

RUN echo -n "CU_JAVA=$CU_JAVA\nCU_SCRIPTS=$CU_SCRIPTS\nCU_USER_HOME=$CU_USER_HOME\n" >> /etc/environment
RUN echo -n "CU_DATABASE_HOME=$CU_DATABASE_HOME\n" >> /etc/environment

# AGENT Cloud Unit pour mettre à jour le status en fin de démarrage (ssh)
RUN wget https://github.com/Treeptik/cloudunit/releases/download/1.0/cloudunitAgent-1.0-SNAPSHOT.jar -O /cloudunit/tools/cloudunitAgent-1.0-SNAPSHOT.jar

RUN echo "deb http://archive.ubuntu.com/ubuntu precise main universe" >> /etc/apt/sources.list
RUN apt-get update

RUN echo mysql-server-5.5 mysql-server/root_password password 'root' | debconf-set-selections
RUN echo mysql-server-5.5 mysql-server/root_password_again password 'root' | debconf-set-selections

RUN apt-get install -y mysql-server-5.5
RUN chown mysql:mysql /var/run/mysqld

RUN sed -i -e"s/^bind-address\s*=\s*127.0.0.1/bind-address = 0.0.0.0/" -e"s/\/var\/lib\/mysql/\/cloudunit\/database/g" -e"/skip-external-locking/ a\lower_case_table_names = 1" /etc/mysql/my.cnf
ADD php.ini /etc/php5/apache2/

RUN echo 'phpmyadmin phpmyadmin/dbconfig-install boolean true' | debconf-set-selections
RUN echo 'phpmyadmin phpmyadmin/app-password-confirm password root' | debconf-set-selections
RUN echo 'phpmyadmin phpmyadmin/mysql/admin-pass password root' | debconf-set-selections
RUN echo 'phpmyadmin phpmyadmin/mysql/app-pass password root' | debconf-set-selections
RUN echo 'phpmyadmin phpmyadmin/reconfigure-webserver multiselect apache2' | debconf-set-selections

RUN apt-get install -y phpmyadmin

# Ajout des scripts utile aux managers et au démarrage container
ADD scripts /cloudunit/scripts/

# SSH
EXPOSE 22	

ENTRYPOINT ["/bin/bash", "/cloudunit/scripts/start-service.sh"]

