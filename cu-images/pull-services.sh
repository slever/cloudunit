#!/bin/bash

CACHE=false

if [ -z "$(git describe --exact-match --tags 2>/dev/null)" ]; then
	GIT_TAG=latest
else
	if [ -z `basename $(git symbolic-ref HEAD) 2>/dev/null` ]; then
		GIT_TAG=`git describe --exact-match --tags 2>/dev/null`
	else
		GIT_TAG=latest
	fi
fi

while read line
do
	image=`echo $line | cut -d" " -f 1`
	echo -e "\nPulling image $image."
	docker pull $image:$GIT_TAG
	docker images | grep $image
	return=$?
	if [ "$return" -eq "0" ]; then
		echo -e "\nThe docker image $image:$GIT_TAG has been correctly pulled.\n"
	else
		echo -e "\nPROBLEM: the docker image $image:$GIT_TAG has not been pulled!\n"
	fi
done < cu-images
