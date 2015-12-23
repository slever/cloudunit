#!/bin/bash

CACHE=false

if [ $CACHE = true ]; then
	BUILD_CMD="docker build --rm -t"
elif [ $CACHE = false ]; then
	BUILD_CMD="docker build --rm --no-cache -t"
fi

if [ -z "$(git describe --exact-match --tags 2>/dev/null)" ]; then
	GIT_TAG=dev
else
	GIT_TAG=`git describe --exact-match --tags 2>/dev/null`
fi

while read line
do
	image=`echo $line | cut -d" " -f 1`
	repository=`echo $line | cut -d" " -f 2`
	echo -e "\n Building image $image:$GIT_TAG."
	$BUILD_CMD $image:$GIT_TAG $repository
	docker images | grep $image | grep $GIT_TAG
	return=$?
	if [ "$return" -eq "0" ]; then
		echo -e "\nThe docker image $image:$GIT_TAG has been correctly built.\n"
	else
		echo -e "\nPROBLEM: the docker image $image:$GIT_TAG has not been built!\n"
	fi
done < cu-images
