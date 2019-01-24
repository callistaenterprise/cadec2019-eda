#!/bin/sh
curl -i -X DELETE http://$DOCKER_HOST_IP:8083/customer/$1
