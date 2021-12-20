#!/bin/sh

kubectl apply -f configmap.yaml

kubectl apply -f storage-pv.yaml

kubectl apply -f deployment.yaml

sleep 10

kubectl apply -f migration-configmap-config.yaml

kubectl apply -f migration-configmap-sql.yaml

kubectl apply -f migration-job.yaml