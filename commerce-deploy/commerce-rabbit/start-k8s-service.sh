#!/bin/sh

kubectl apply -f configmap.yaml

kubectl apply -f storage-pv.yaml

kubectl apply -f deployment.yaml
