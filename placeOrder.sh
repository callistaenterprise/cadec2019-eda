#!/bin/sh
curl -i -X POST -H "Content-Type:application/json" -d "{  \"customer\" : \"bb@callistaenterprise.se\", \"content\" : \"Event Driven Architecture\" }" http://$DOCKER_HOST_IP:8080/order
