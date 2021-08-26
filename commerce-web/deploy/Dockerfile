FROM maven:3.6.1-jdk-13-alpine

ENV SERVICE_NAME="commerce-web"

RUN mkdir /workspace && \
    mkdir /workspace/${SERVICE_NAME} && \
    mkdir /workspace/logs \
    && chmod 777 /workspace -R

# Copy build file
COPY ./ /workspace/${SERVICE_NAME}/

WORKDIR /workspace/${SERVICE_NAME} 

# Maven build
RUN mvn clean package deploy -Dmaven.test.skip=true
