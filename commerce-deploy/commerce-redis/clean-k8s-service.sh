#!/bin/sh

kubectl delete -f ./k8s/deployment.yaml

kubectl delete -f ./k8s/storage-pv.yaml