#!/bin/sh

echo "------------------------------------------------"
echo "Remove rabbit-commerce stack"
echo "------------------------------------------------"
docker-compose --compatibility -f rabbit-commerce.yml down