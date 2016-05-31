#!/usr/bin/env bash

M_MAJOR=2.6

docker images |grep mongo
if [ "$?" == "1" ]; then
	docker pull mongo:$M_MAJOR
fi

RETURN=`docker ps | grep letschat-mongo`

# If mongo is not running
if [ -z "$RETURN" ]; then

	docker run --name letschat-mongo -d mongo:$M_MAJOR
	
	# Maybe it could already exist
    if [ "$?" == "1" ]; then
        docker start letschat-mongo
    fi
fi

docker images |grep sdelements/lets-chat
if [ "$?" == "1" ]; then
    docker pull sdelements/lets-chat
fi

RETURN=`docker ps | grep lets-chat`

# If let's chat is not running
if [ -z "$RETURN" ]; then
   
   docker run --name lets-chat \
   			  --link letschat-mongo:mongo \
   			  -p 2000:8080 \
   			  -d sdelements/lets-chat

    # Maybe it could already exist
    if [ "$?" == "1" ]; then
        docker start lets-chat
    fi
fi

echo -e "*****************************************************"
echo -e "* ACCESS TO LET'S CHAT AT --> http://192.168.50.4:2000"
echo -e "*****************************************************"
