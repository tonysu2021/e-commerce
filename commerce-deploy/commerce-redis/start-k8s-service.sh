#!/bin/sh

kubectl apply -f storage-pv.yaml

kubectl apply -f deployment.yaml