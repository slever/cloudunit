# LICENCE : CloudUnit is available under the Affero Gnu Public License GPL V3 : https://www.gnu.org/licenses/agpl-3.0.html
# but CloudUnit is licensed too under a standard commercial license.
# Please contact our sales team if you would like to discuss the specifics of our Enterprise license.
# If you are not sure whether the GPL is right for you,
# you can always test our software under the GPL and inspect the source code before you contact us
# about purchasing a commercial license.

# LEGAL TERMS : "CloudUnit" is a registered trademark of Treeptik and can't be used to endorse
# or promote products derived from this project without prior written permission from Treeptik.
# Products or services derived from this software may not be called "CloudUnit"
# nor may "Treeptik" or similar confusing terms appear in their names without prior written permission.
# For any questions, contact us : contact@treeptik.fr

FROM ubuntu:14.04

# Passage du système en UTF-8
RUN echo "en_US.UTF-8 UTF-8" >> /etc/locale.gen
RUN locale-gen en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
ENV MAVEN_HOME /usr/share/maven
ENV JAVA_HOME /cloudunit/java/jdk1.8.0_25

# Dépendances Debian
RUN apt-get update && apt-get install -y\
    ruby-full\
    nodejs\
    nodejs-legacy\
    git \
    wget \
    git-core \
    npm

# Dépendances Ruby
RUN gem install sass compass

# Dépendances Node.js
RUN npm install -g grunt grunt-cli bower

# Fix pour permettre de tirer correctement certaines dépendances
RUN git config --global url."https://".insteadOf git://

RUN wget https://github.com/Treeptik/CloudUnit/releases/download/1.0/apache-maven-3.3.3-bin.tar.gz -O /tmp/apache-maven-3.3.3-bin.tar.gz \
RUN tar -xvf /tmp/apache-maven-3.3.3-bin.tar.gz -C /usr/share && rm /tmp/apache-* \
RUN mv /usr/share/apache-maven-3.3.3 /usr/share/maven && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

WORKDIR /home/builder/app

RUN git clone https://github.com/Treeptik/CloudUnit.git

# CMD ./build.sh




