#!/bin/sh

kubectl delete -f istio-rules.yaml

kubectl delete -f deployment.yaml