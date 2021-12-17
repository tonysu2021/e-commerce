#!/bin/sh

kubectl delete -f deployment.yaml

kubectl delete -f storage-pv.yaml

kubectl delete -f configmap.yaml

kubectl delete -f job.yaml