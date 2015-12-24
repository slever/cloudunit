#!/usr/bin/env bash

alias drmi='docker rmi -f $(docker images -q)'
alias cu-reset='cd /home/vagrant/cloudunit/cu-vagrant/utils && ./reset-all.sh -y'


