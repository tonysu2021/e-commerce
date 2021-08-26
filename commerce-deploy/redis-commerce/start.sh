#!/bin/sh

echo "------------------------------------------------"
echo "Build redis-commerce stack"
echo "------------------------------------------------"
docker-compose --compatibility -f redis-commerce.yml up -d
