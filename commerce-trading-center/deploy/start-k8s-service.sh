#!/bin/sh

kubectl apply -f ./k8s/deployment.yaml

kubectl apply -f ./k8s/istio-rules.yaml