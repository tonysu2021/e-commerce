# HAProxy

假如不採用istio-gateway，可以使用HAProxy

## 設定

必須將服務本身的vip(virtual IP) 換成 dnsrr(DNS Round Robin)，而非使用swarm本身的LB。

```sh
# 將swarm vip(virtual IP) 換成 dnsrr(DNS Round Robin)
docker service update --force --endpoint-mode 'dnsrr' qsf-client-1143_qsf-client


curl http://172.20.111.171:8511/qsf/version
```
