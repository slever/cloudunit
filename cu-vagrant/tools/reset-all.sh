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

#!/bin/bash

function todo_or_exit
{
    if [ "$1" != "-y" ]; then
        echo "Do you want to reset CloudUnit ? [y/n]"
        read PROD_ASW
        if [ "$PROD_ASW" != "y" ] && [ "$PROD_ASW" != "n" ]; then
            echo "Entrer y ou n!"
            exit 1
        elif [ "$PROD_ASW" = "n" ]; then
            exit 1
        fi
    fi
}

function cleaning
{
    echo -e "\nKilling containers\n"
    docker kill $(docker ps -aq)

    echo -e "\nRemoving containers\n"
    docker rm -vf $(docker ps -aq)

    echo -e "\nChanging directory\n"
    cd /home/$USER/cloudunit/cu-platform

    echo -e "\nCurrent directory: `pwd`\n"
    sudo rm -rf /registry/* /var/log/cloudunit
}

function run_services
{
    CONT_NAME=(java tomcat-6 tomcat-7 tomcat-8 jboss-8)
    IMAGE_NAME=(cloudunit/java cloudunit/tomcat-6 cloudunit/tomcat-7 cloudunit/tomcat-8 cloudunit/jboss-8)
    LOG_FILE=/home/$USER/cloudunit/cu-services/run-log
    if [ -f $LOG_FILE ]; then
        rm $LOG_FILE
    fi
    for i in 0 1 2 3 4
    do
        docker ps -a | grep ${CONT_NAME[$i]} | grep -q ${IMAGE_NAME[$i]}
        return=$?
        if [ "$return" -eq "0" ]; then
            echo -e "\nThe docker container ${CONT_NAME[$i]} has already been launched.\n" >> $LOG_FILE
        else
            echo -e "\nLaunching the docker container ${CONT_NAME[$i]}.\n" >> $LOG_FILE
            docker run --name ${CONT_NAME[$i]} ${IMAGE_NAME[$i]}:dev
        fi
    done
}

# ##################################### MAIN ###############################

todo_or_exit
cleaning
run_services
