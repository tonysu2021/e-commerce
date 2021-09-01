# HA Proxy

## 

```sh
# 將swarm vip(virtual IP) 換成 dnsrr(DNS Round Robin)
docker service update --force --endpoint-mode 'dnsrr' qsf-client-1143_qsf-client


curl http://172.20.111.171:8511/qsf/version
```
