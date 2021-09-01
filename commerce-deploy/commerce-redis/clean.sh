#!/bin/sh

echo "------------------------------------------------"
echo "Remove commerc-redis stack"
echo "------------------------------------------------"
docker-compose --compatibility -f commerc-redis.yml down