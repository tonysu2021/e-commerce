#!/bin/sh

echo "------------------------------------------------"
echo "Remove commerce-rabbit stack"
echo "------------------------------------------------"
docker-compose --compatibility -f commerce-rabbit.yml down