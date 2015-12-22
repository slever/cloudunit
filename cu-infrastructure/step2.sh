#!/usr/bin/env bash

vagrant ssh -c "sudo mkdir /opt/cloudunit"
vagrant ssh -c "cd /home/vagrant/cloudunit/cu-services && ./build-services.sh"