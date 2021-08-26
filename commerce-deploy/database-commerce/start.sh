#!/bin/sh

echo "------------------------------------------------"
echo "Create commerce-database stack"
echo "------------------------------------------------"
docker-compose --compatibility -f commerce-database.yml up --build -d

