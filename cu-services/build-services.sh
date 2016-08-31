#!/bin/bash


CACHE=false

BUILD_CMD="docker build --rm"

if [ $CACHE = false ]; then
	BUILD_CMD="$BUILD_CMD --no-cache"
fi

if [ $http_proxy ]; then
	echo "Use http proxy: $http_proxy"
	BUILD_CMD="$BUILD_CMD --build-arg http_proxy=$http_proxy"
fi

if [ $https_proxy ]; then
        echo "Use https proxy: $https_proxy"
        BUILD_CMD="$BUILD_CMD --build-arg https_proxy=$https_proxy"
fi

BUILD_CMD="$BUILD_CMD -t"

GIT_TAG=latest

while read line
do
	image=`echo $line | cut -d" " -f 1`
	repository=`echo $line | cut -d" " -f 2`
	echo -e "\n Building image $image:$GIT_TAG.($BUILD_CMD)"
	$BUILD_CMD $image:$GIT_TAG $repository
	docker images | grep $image | grep $GIT_TAG
	return=$?
	if [ "$return" -eq "0" ]; then
		echo -e "\nThe docker image $image:$GIT_TAG has been correctly built.\n"
	else
		echo -e "\nPROBLEM: the docker image $image:$GIT_TAG has not been built!\n"
	fi
done < cu-images

# Build test of Cloudunit Docker images
./check_build_images.sh
# Exit on child script error
if [ $? -eq 1 ]; then
	exit 1
fi
