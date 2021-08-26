# First Stage
FROM commerce-web:0.0.1 as builder

# Workspace path
WORKDIR /usr/app

# Copy build file
COPY ./ ./
COPY ./src ./src

# Maven build
RUN mvn clean package -Dmaven.test.skip=true

# Second Stage
FROM maven:3.6.1-jdk-13-alpine

ENV SERVICE_NAME="commerce-trading-center"

RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

RUN mkdir /workspace && \
    mkdir /workspace/${SERVICE_NAME} && \
    mkdir /workspace/logs \
    && chmod 777 /workspace -R

WORKDIR /workspace/${SERVICE_NAME}

COPY --from=builder /usr/app/target/${SERVICE_NAME}-0.0.1-SNAPSHOT.jar /workspace/app.jar

ENTRYPOINT ["java", "-jar", "/workspace/app.jar"]