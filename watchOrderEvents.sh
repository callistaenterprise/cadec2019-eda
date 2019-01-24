#!/bin/sh
docker-compose exec kafka bin/kafka-console-consumer.sh --bootstrap-server kafka:9092 --topic orders $*
