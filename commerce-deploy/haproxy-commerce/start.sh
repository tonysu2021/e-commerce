#!/bin/sh

echo "------------------------------------------------"
echo "Build haproxy-commerce Service"
echo "------------------------------------------------"
docker stack deploy -c haproxy-commerce.yml --prune haproxy-commerce