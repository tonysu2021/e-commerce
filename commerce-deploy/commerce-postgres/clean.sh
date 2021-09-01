#!/bin/sh

echo "------------------------------------------------"
echo "Remove commerce-postgres stack"
echo "------------------------------------------------"
docker-compose --compatibility -f commerce-postgres.yml down -v
