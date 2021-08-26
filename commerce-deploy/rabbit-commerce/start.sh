#!/bin/sh

export RABBITMQ_DEFAULT_USER='guest'
export RABBITMQ_DEFAULT_PASS='guest'

echo "------------------------------------------------"
echo "Build rabbit-commerce stack"
echo "------------------------------------------------"
docker-compose --compatibility -f rabbit-commerce.yml up -d
