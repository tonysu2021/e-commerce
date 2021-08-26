#!/bin/sh

SERVICE_NAME="commerce-trading-center"

echo "------------------------------------------------"
echo "Push New Image : $SERVICE_NAME:0.0.1 $1"
echo "------------------------------------------------"
docker tag $SERVICE_NAME:0.0.1 $1
docker login $2 -u $3 -p $4
docker push $1
docker rmi -f $1