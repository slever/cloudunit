#!/usr/bin/env bash

vagrant plugin install vagrant-reload
vagrant plugin install vagrant-vbguest

# linux is the default choice
vagrant up linux
