#!/bin/sh

echo "------------------------------------------------"
echo "Build commerce-haproxy Service"
echo "------------------------------------------------"
docker stack deploy -c commerce-haproxy.yml --prune commerce-haproxy