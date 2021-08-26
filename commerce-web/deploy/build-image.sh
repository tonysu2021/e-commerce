#!/bin/sh


echo "------------------------------------------------"
echo "Remove Old Image"
echo "------------------------------------------------"
docker rmi commerce-web:0.0.1

echo "------------------------------------------------"
echo "Build New Image"
echo "------------------------------------------------"
docker build --network=host --tag commerce-web:0.0.1 --file ./Dockerfile ../