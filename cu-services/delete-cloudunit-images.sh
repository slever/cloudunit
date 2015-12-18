#!/bin/bash

TEMP_FILE=images_cu_todelete
docker images | grep 'cloudunit' | cut -d" " -f 1 > $TEMP_FILE

# delete all images which contains 'cloudunit'
while read line
do
	docker rmi -f $line
done < $TEMP_FILE

# delete temp file
rm $TEMP_FILE
