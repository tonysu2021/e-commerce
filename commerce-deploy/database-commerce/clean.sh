#!/bin/sh

echo "------------------------------------------------"
echo "Remove commerce-database stack"
echo "------------------------------------------------"
docker-compose --compatibility -f commerce-database.yml down -v
