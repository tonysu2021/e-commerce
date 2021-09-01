#!/bin/sh

export RABBITMQ_DEFAULT_USER='guest'
export RABBITMQ_DEFAULT_PASS='guest'

echo "------------------------------------------------"
echo "Build commerce-rabbit stack"
echo "------------------------------------------------"
docker-compose --compatibility -f commerce-rabbit.yml up -d
