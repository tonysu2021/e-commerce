# Introduction

* 練習規劃電子商務系統。
* 練習服務註冊及請求路由，採用Service Mesh (Istio + Envoy)

## Members

Tony.su

## 專案Local執行環境

* Java 13 (jdk-13.0.2)
* apache-maven-3.6.3
* gradle-6.4
* host設定

  ```text
  # E-commerce
  172.20.111.189 commerce-postgres
  172.20.111.189 commerce-rabbit
  172.20.111.189 commerce-redis
  127.0.0.1 commerce-biz-customer
  ```

## E-Commerce Project Structure

業務模組

* commerce-biz-customer : 消費者(customer)
* commerce-biz-product : 商品(product)、庫存(inventory)、供應商(supplier)
* commerce-biz-order : 訂單(order)
* commerce-biz-payment `(尚未開發)`: 金流(payment)、信用卡(credit card)、發票(invoice)
* commerce-trading-center : 主要作為對比三方的事務記錄表(order、product、payment)，做一個最終決定。
* commerce-client : 客戶端暫時不模組化

整合模組

* commerce-web : Reactor 、Web 常用的功能。

## 整合組件部屬到本機.m2

windows預設路徑 C:\Users\User\.m2\repository

```sh
# 必須先將共用組件commerce-web部屬至.m2
mvn clean package deploy "-Dmaven.test.skip=true"

mvn clean package "-Dmaven.test.skip=true"
```
