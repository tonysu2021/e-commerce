#!/bin/sh

kubectl apply -f deployment.yaml

kubectl apply -f istio-rules.yaml