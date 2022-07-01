FROM openjdk:8u191-jre-alpine3.8
# install curl into container
RUN apk add curl jq

# Workspace
WORKDIR /test

# ADD .jar under target from host into this image
ADD target/selenium-docker.jar 			selenium-docker.jar
ADD target/selenium-docker-tests.jar 	selenium-docker-tests.jar
ADD target/libs							libs

# in case of any other dependency like .csv / .json / .xls
# please ADD that as well

# ADD suite files
ADD search-module.xml					search-module.xml

# ADD health check script
# due to some windows specifc characters it might not work properly so let's
# convert the file from windows to unix format
ADD healthcheck.sh                      healthcheck.sh
RUN dos2unix healthcheck.sh

# BROWSER
# HUB_HOST
# MODULE
ENV BROWSER ${BROWSER}

ENTRYPOINT sh healthcheck.sh