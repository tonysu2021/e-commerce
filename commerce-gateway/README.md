# Istio Gateway操作說明

* 檢查Istio Gateway的入口IP和端口

  如果`EXTERNAL-IP`值為<none>（或永久<pending>），則您的環境不為入口網關提供外部負載平衡器。

  ```sh
  kubectl get services istio-ingressgateway -n istio-system
  ```

  * 方法一

    ```sh
    ingress_port=$(sudo kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].nodePort}')
    ingress_host=$(sudo kubectl get po -l istio=ingressgateway -n istio-system -o jsonpath='{.items[0].status.hostIP}')
    
    echo "Gateway url : ${ingress_host}:${ingress_port}"
    ```

  * 方法二: 安裝Metallb

    ```sh

    ## 安裝
    # https://raw.githubusercontent.com/google/metallb/v0.8.3/manifests/metallb.yaml
    kubectl apply -f https://raw.githubusercontent.com/google/metallb/v0.8.3/manifests/metallb.yaml
    
    ## 驗證
    kubectl get pods -n metallb-system -o wide

    ## 設定
    kubectl apply -f metallb_configmap.yml
    ```

成功結果如圖:

<p align="center"><img src="./image/test-result.png" width="75%"/></p>

## Reference

[13.Load-Balancer](https://github.com/ansilh/kubernetes-the-hardway-virtualbox/blob/master/13.Load-Balancer.md)
