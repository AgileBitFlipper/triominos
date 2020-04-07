#!/bin/bash

# start up the jenkins pipeline for triominoes
echo docker run --name jenkins -u root -p 8080:8080 -p 50000:50000 -v ~/work:/root/work -v jenkins_home:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock -v /usr/local/bin/docker:/usr/bin/docker jenkins/jenkins:lts
docker run --name jenkins -u root -p 8080:8080 -p 50000:50000 -v ~/work:/root/work -v jenkins_home:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock -v /usr/local/bin/docker:/usr/bin/docker jenkins/jenkins:lts
