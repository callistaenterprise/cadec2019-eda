# Event Driven Architectures with Apache Kafka

This repository contains examples that illustrates the three Event Driven Architecture flavours presented at [Callista Developer's Conference 2019](http://callistaenterprise.se/event/cadec/): *Event Notification*, *Event-Carried State Transfer* and *Event Sourcing*.

The examples gradually evolve an initial, simple micro service application portfolio into using Event Notification, Event-Carried State Transfer and Event Sourcing. There are git tags for each stage in the evolution:

* initial
* eventNotification
* choreography
* eventCarriedStateTransfer
* eventSourcing

In order to build and run the examples, Maven, a Java8 JDK and Docker are required. The environment variable `DOCKER_HOST_IP` must be set to the ip address of your DOCKER_HOST (typically `localhost` when running native docker om Linux, Mac or Windows, or the ip address of the VirtualBox Docker host when running docker-toolbox on Mac or Windows):

`export DOCKER_HOST_IP=localhost`

## Building the example components and Docker images

Run the following command to build Docker images for the involved components:

`mvn clean install`

## Running the examples

Run the following command to start the docker component portfolio:

`docker-compose up -d`

To see the console output for the components, run the following command in another terminal:

`docker-compose logs -f`

Then create a customer into the Customer component:

```
curl -i -X POST -H "Content-Type:application/json" -d "{  \"firstname\" \
: \"Björn\",  \"lastname\" : \"Beskow\", \"street\" \
: \"Norra Saltsjönäsvägen 11\",  \"zip\" : \"421 66\",  \"city\" \
: \"Göteborg\",  \"email\" : \"bb@callistaenterprise.se\" }" \
http://$DOCKER_HOST_IP:8083/customer
```

Then place an order into the Order component, using the customer email previously created:

```
curl -i -X POST -H "Content-Type:application/json" -d "{  \"customer\" \
: \"bb@callistaenterprise.se\", \"content\" : \"Event Driven Architecture\" }" \
http://$DOCKER_HOST_IP:8080/order
```

Enjoy!