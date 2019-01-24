#!/bin/sh
curl -i -X POST -H "Content-Type:application/json" -d "{  \"firstname\" : \"Björn\",  \"lastname\" : \"Beskow\", \"street\" : \"Norra Saltsjönäsvägen 11\",  \"zip\" : \"421 66\",  \"city\" : \"Göteborg\",  \"email\" : \"bb@callistaenterprise.se\" }" http://$DOCKER_HOST_IP:8083/customer
