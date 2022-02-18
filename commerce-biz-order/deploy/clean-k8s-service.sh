#!/bin/sh

kubectl delete -f ./k8s/istio-rules.yaml

kubectl delete -f ./k8s/deployment.yaml