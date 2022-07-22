# 🚀 Selenium based test framework + execution in docker containers

The list of some basic Linux commands using in this course:

- Print the current working directory - pwd
- List all the files & folders - ls -al
- Create directory - mkdir dirname
- Change to directory - cd dirname
- Create file - touch filename
- Find files with the name - find / -name partialfilename
- Remove file - rm file
- Remove directory - rm -r directory
- Get list of IP addresses - ipconfig -all -(run from C:\Users\user.name)

# install docker:

- https://docs.docker.com/desktop/windows/install/  Use WSL 2 instead of Hyper-V option is not selected for this course

# PORT_MAPPING

- $ docker run -d -p hostPort:containerPort image - example of command
- $ docker run -p 4444:80 mongo
- $ docker run -p 8050:80 nginx - run 'nginx' APP on http://192.168.100.6:8050

# VOLUME_MAPPING

- $ docker run -v /path/to/host/dir:/path/to/container/dir image

# DOCKER_NETWORK

- $ docker network create test-network
- $ docker run -d --name=nginx --network=test-network nginx
- $ docker run -it --network=test-network alpine

# # DOCKER_IMAGE_CREATION:

You should create file with name "Dockerfile" and fill in the data below:

````
FROM alpine
ENTRYPOINT data
````

Then, let's build created image from directory, where "Dockerfile" was created and run it:

- $ docker build -t=aksionauivan/datatest . - build image with name dockerHub_account_name
- $ docker run

# CREATE folder 'jars' and Test.java file inside and fill in with:

- $ mkdir jars
- $ cd jars
- $ vi Test.java

````
  public class Test {
  public static void main(String[] args) {
  System.out.println("Hello world");
  }
  }
````

# CREATE IMAGE AND INSTALL JAVA

- $ docker run -it alpine - create new container in interactive mode
- $ apk add openjdk8 - install java inside container (if we need - apk add curl)
- $ find / -name javac - check javac location
- $ export PATH=$PATH:/usr/lib/jvm/java-1.8-openjdk/bin - set java env variable to be able to use 'javac'

Let's edit "Dockerfile" and update with the data below:

- $ vi Dockerfile

````
FROM alpine
RUN apk add openjdk8
ENV PATH $PATH:/usr/lib/jvm/java-1.8-openjdk/bin
WORKDIR /home/admin/selenium
ADD /jars/Test.java Test.java
ENTRYPOINT javac Test.java && java Test
````

Now let's build image from docker file, then run it:

- $ docker build -t=aksionauivan/helloword . -build image based on 'Dockerfile' data
- $ docker run aksionauivan/helloword - run container based on image
- $ docker run -it --entrypoint=/bin/sh aksionauivan/helloword run container in interactive mode to debug container with
  shell

# PASSING ARGUMENTS TO A JAVA PROGRAM RUNNING INSIDE IMAGE

Let's edit Test.java file:

- $ vi Test.java

````
  public class Test {
  public static void main(String[] args) {
  System.out.println("Hello world \n We have got a number from you. The number is: "
  + Integer.parseInt(args[0]));
  }
  }
````

Let's modify 'Dockerfile':

- $ vi Dockerfile

````
FROM alpine
RUN apk add openjdk8
ENV PATH $PATH:/usr/lib/jvm/java-1.8-openjdk/bin
WORKDIR /home/admin/selenium
ADD /jars/Test.java Test.java
ENTRYPOINT javac Test.java && java Test $NUMBER           - added argument, which will be passed to 'Test.java'
````

- $ docker build -t=aksionauivan/helloword . -build image based on 'Dockerfile' data
- $ docker run -e NUMBER=5 aksionauivan/helloword - run container based on image

# PUSH IMAGE TO A DOCKER HUB

- $ docker login - login into account aksionauivan
- $ docker push aksionauivan/helloword - push an image to dockerhub
- $ docker tag aksionauivan/helloword:latest aksionauivan/helloword:release-1.0 - tag image locally
- $ docker push aksionauivan/helloword:release-1.0 - push a tagged image to dockerhub

# DOCKER COMPOSE

- $ vi docker-compose.yaml - to start file creation, then fill in the data below:

````
version: '3'
services:
  helloword:
    container_name: aksionauivan/helloword
    image: aksionauivan/helloword
    ports:
      - "8080:8080"                                   -  "hostPort:containerPort"
	environment:
	  - NUMBER=5
	working_dir: a/b/c                                  - create directory inside container
	entrypoint: "javac Test.java && java Test $NUMBER"       - will be executed inside 'a/b/c'
    volumes:
      - "$PWD/selenium:a/b/c"     "container_hostPath:working_directory path" - so we will copy an output of programm to 'docker-compose.yaml' location
    networks:
      - net                                            -  usefull to comunicate with external container
    nginx:
    container_name: nginx
    image: anginx
    ports:
      - "8090:8080"
    depends_on:
	  - helloword               will check if 'helloword' container created, then create 'nginx' container
    networks:
      - net
networks:
  net:
````

# SELENIUM GRID

- $ docker pull selenium/hub:3.14
- $ docker pull selenium/node-firefox:3.14
- $ docker pull selenium/node-chrome:3.14
- $ mkdir selenium - create directory for 'volumes'
- $ vi docker-compose.yaml - to start file creation, then fill in the data below:

````
version: "3"
services:
  hub:
    image: selenium/hub:4.1.0
    ports:
      - "4444:4444"
  chrome:
    image: selenium/node-chrome:4.1.0
    shm_size: '2g'
    depends_on:
      - hub
    environment:
      - SE_EVENT_BUS_HOST=hub
      - SE_EVENT_BUS_PUBLISH_PORT=4442
      - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
  firefox:
    image: selenium/node-firefox:4.1.0
    shm_size: '2g'
    depends_on:
      - hub
    environment:
      - SE_EVENT_BUS_HOST=hub
      - SE_EVENT_BUS_PUBLISH_PORT=4442
      - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
````

- $ docker-compose up -d --scale chrome=4 increase the number of containers for parallel tests execution

# RUNNING TESTS VIA JAR

- $ mvn clean package -DskipTests
- $ cd target
- $ java -cp "selenium-docker.jar;selenium-docker-tests.jar;libs/*" -DHUB_HOST="192.168.100.5" org.testng.TestNG ../search-module.xml - run tests based on xml suite via TestNG class

# CREATE DOCKER IMAGE FOR TESTS

Let's create 'Dockerfile':

- $ vi Dockerfile

````
# Workspace
WORKDIR /test/folder

# ADD .jar under target from host
# into this image
ADD target/selenium-docker.jar 			selenium-docker.jar
ADD target/selenium-docker-tests.jar 	selenium-docker-tests.jar
ADD target/libs							libs

# in case of any other dependency like .csv / .json / .xls
# please ADD that as well

# ADD suite files
ADD search-module.xml					search-module.xml

# ADD health check script
#ADD healthcheck.sh                      healthcheck.sh

# BROWSER
# HUB_HOST
# MODULE

ENTRYPOINT java -cp selenium-docker.jar:selenium-docker-tests.jar:libs/* \
-DHUB_HOST=$HUB_HOST \
-DBROWSER=$BROWSER \
org.testng.TestNG $MODULE
````

Let's build, save and run our test image:

- $ docker build -t=aksionauivan/selenium-docker . - build image with name dockerHub_account_name
- $ docker push aksionauivan/selenium-docker - push image to docker hub
- $ docker run -it --entrypoint=/bin/sh aksionauivan/selenium-docker - run container in interactive mode(entrypoint is
  overridden) to debug container with shell
- $ docker run -e HUB_HOST=192.168.100.5 -e MODULE=search-module.xml aksionauivan/selenium-docker- run container(tests
  are executed inside)

# How to clean docker data

- $ docker image rm aksionauivan/selenium-docker
- $ docker system prune -a - will remove all unused containers, networks, image
- $ docker container prune - remove all stopped containers
- $ docker image rm image_id - delete specific image
- $ docker rmi -f $(docker images -aq) - delete all the images
- $ docker kill $(docker ps -q) - stop all Docker containers

# Move tests results outside container with volume mapping

- $ docker run -e HUB_HOST=192.168.100.5 -e MODULE=search-module.xml -v
  /C/Users/ivan.aksionau/IdeaProjects/selenium-docker/output:/test/test-output aksionauivan/selenium-docker

# CREATE DOCKER-COMPOSE FILE

- $ vi docker-compose.yaml - to start file creation, then fill in the data below:

````
version: "3"
services:
  hub:
    image: selenium/hub:4.1.0
    ports:
      - "4444:4444"
  chrome:
    image: selenium/node-chrome:4.1.0
    shm_size: '2g'
    depends_on:
      - hub
    environment:
      - SE_EVENT_BUS_HOST=hub
      - SE_EVENT_BUS_PUBLISH_PORT=4442
      - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
  firefox:
    image: selenium/node-firefox:4.1.0
    shm_size: '2g'
    depends_on:
      - hub
    environment:
      - SE_EVENT_BUS_HOST=hub
      - SE_EVENT_BUS_PUBLISH_PORT=4442
      - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
  test-module:
    image: aksionauivan/selenium-docker
    depends_on:
      - chrome
      - firefox
    environment:
      - HUB_HOST=hub
      - MODULE=search-module.xml
    volumes:
      - ./search-result:/test/test-output

````

# Let's add script to wait till 'hub' is started before execution of tests container:

````
#!/usr/bin/env bash
# Environment Variables
# HUB_HOST
# BROWSER
# MODULE

echo "Checking if hub is ready - $HUB_HOST"
# curl SeleniumGrid hub status and parse response with 'jq', which will convert string response into JSon format
while [ "$(curl -s http://$HUB_HOST:4444/wd/hub/status | jq -r .value.ready)" != "true" ]; do
  sleep 1
done

# start the java command
java -cp selenium-docker.jar:selenium-docker-tests.jar:libs/* \
  -DHUB_HOST=$HUB_HOST \
  -DBROWSER=$BROWSER \
  org.testng.TestNG $MODULE
````

docker compose file and docker file are also modified.(see into https://github.com/IvanAksionau/selenium-docker)

- $ docker build -t=aksionauivan/selenium-docker . - build image with name dockerHub_account_name
- $ docker push aksionauivan/selenium-docker - push image to docker hub
- $ docker-compose up -d --scale chrome=4 increase the number of containers for parallel tests execution
- $ docker-compose up | grep -e 'test-module' - run container and log only test-module container

# How to find the host file locally

Windows:
C:\Windows\System32\Drivers\etc\
Linux:
/etc/hosts

# INSTALL JENKINS WITH DOCKER
Visit https://itnext.io/docker-inside-docker-for-jenkins-d906b7b5f527 to get details (sudo installed to access docker)
all created docker files are in ./jenkins folder

- $ docker build -t=aksionauivan/jenkins_with_docker . - build image with name dockerHub_account_name/image_name
- $ docker-compose up - up image from ./jenkins folder
- access jenkins on http://192.168.100.5:8080

# CONFIGURE DOCKER AGENT(is optional, as we have all what we need inside jenkins container)

Visit https://www.jenkins.io/doc/book/using/using-agents/ to get details.
- $ docker run -d --rm --name=agentInDocker -p 22:22 -e "JENKINS_AGENT_SSH_PUBKEY=your-public-key" jenkins/ssh-agent:jdk11
- $ docker exec -it container_name bash (name can be found with 'docker ps') - go inside specific container
- $ docker exec container_name /bin/sh -c /usr/bin/env - get variables inside specific container

# CONFIGURE GitHub Actions to trigger jenkins pipeline on created merge request
- https://docs.github.com/en/actions
- https://github.com/marketplace/actions/trigger-jenkins-job
- https://github.com/appleboy/jenkins-action/issues/3
- https://github.com/marketplace?query=jenkins&type=actions
- You may need to get the crumb with the following URL on the browser: http://<jenkins_url>/crumbIssuer/api/json?tree=crumb