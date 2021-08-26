#!/bin/sh

# 本機Run Project 請註解掉
# Docker Run Project 移除註解
export DOCKER_HOST_IP=$(hostname -I | awk '{print $1}')

docker-compose --compatibility -f full-stack.yml up --build -d
