#!/bin/sh

kubectl apply -f ./k8s/storage-pv.yaml

kubectl apply -f ./k8s/deployment.yaml