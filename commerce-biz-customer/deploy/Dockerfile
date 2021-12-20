FROM azul/zulu-openjdk-alpine:13.0.7

ENV PROFILE_ACTIVE=dev
ENV SERVICE_NAME="commerce-biz-customer"

RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

RUN mkdir /workspace && \
    mkdir /workspace/${SERVICE_NAME} && \
    mkdir /workspace/logs \
    && chmod 777 /workspace -R

WORKDIR /workspace/${SERVICE_NAME}

COPY ${SERVICE_NAME}-0.0.1-SNAPSHOT.jar /workspace/app.jar

ENTRYPOINT ["java", "-jar", "/workspace/app.jar"]