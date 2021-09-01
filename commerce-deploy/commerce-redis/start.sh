#!/bin/sh

echo "------------------------------------------------"
echo "Build commerc-redis stack"
echo "------------------------------------------------"
docker-compose --compatibility -f commerc-redis.yml up -d
