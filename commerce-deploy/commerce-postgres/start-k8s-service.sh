#!/bin/sh

kubectl apply -f configmap.yaml

kubectl apply -f storage-pv.yaml

kubectl apply -f deployment.yaml

sleep 10

kubectl apply -f job.yaml