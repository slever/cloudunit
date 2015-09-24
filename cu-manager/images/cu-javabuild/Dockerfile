# LICENCE : CloudUnit is available under the Gnu Public License GPL V3 : https://www.gnu.org/licenses/gpl.txt
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

FROM maven:3-jdk-8

ENV BUILDER_UID=builder_uid
ENV BUILDER_GID=builder_gid

# Création de l'utilisateur builder
RUN groupadd -r --gid $BUILDER_GID builder && useradd -m -r --uid $BUILDER_UID -g builder builder
USER builder
RUN wget https://github.com/Treeptik/CloudUnit/releases/download/0.9/m2.tar.gz -O /tmp/m2.tar.gz && tar -xvf /tmp/m2.tar.gz -C /home/builder && mv /home/builder/m2 /home/builder/.m2

WORKDIR /home/builder/app