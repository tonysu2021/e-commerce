#!/bin/sh

echo "------------------------------------------------"
echo "Remove redis-commerce stack"
echo "------------------------------------------------"
docker-compose --compatibility -f redis-commerce.yml down