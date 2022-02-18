#!/bin/sh

kubectl delete -f ./k8s/deployment.yaml

kubectl delete -f ./k8s/storage-pv.yaml

kubectl delete -f ./k8s/configmap.yaml

kubectl delete -f ./k8s/migration-job.yaml

kubectl delete -f ./k8s/migration-configmap-sql.yaml

kubectl delete -f ./k8s/migration-configmap-config.yaml