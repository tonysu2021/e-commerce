#!/bin/sh

SERVICE_NAME="commerce-biz-order"

echo "------------------------------------------------"
echo "Remove Old Image : $SERVICE_NAME"
echo "------------------------------------------------"
docker rmi $SERVICE_NAME:0.0.1

echo "------------------------------------------------"
echo "Build New Image : $SERVICE_NAME"
echo "------------------------------------------------"
docker build --network=host --tag $SERVICE_NAME:0.0.1 --file ./Dockerfile ../