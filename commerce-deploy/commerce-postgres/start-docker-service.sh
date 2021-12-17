#!/bin/sh

echo "------------------------------------------------"
echo "Create commerce-postgres stack"
echo "------------------------------------------------"
docker-compose --compatibility -f commerce-postgres.yml up --build -d

