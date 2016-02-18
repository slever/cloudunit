#!/bin/bash
# add backport sources to the end of the file
echo "deb http://ftp.us.debian.org/debian/ jessie-backports main" >> /etc/apt/sources.list
echo "deb-src http://ftp.us.debian.org/debian/ jessie-backports main" >> /etc/apt/sources.list
# update apt & install kernel 4.3 at this date
apt-get update ; apt-get install -t jessie-backports linux-image-amd64

