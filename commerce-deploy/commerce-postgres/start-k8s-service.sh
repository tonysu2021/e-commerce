#!/bin/sh

kubectl apply -f ./k8s/configmap.yaml

kubectl apply -f ./k8s/storage-pv.yaml

kubectl apply -f ./k8s/deployment.yaml

sleep 10

kubectl apply -f ./k8s/migration-configmap-config.yaml

kubectl apply -f ./k8s/migration-configmap-sql.yaml

kubectl apply -f ./k8s/migration-job.yaml