# 交易查询

**请求地址：** pingpp/queryCharge/{id}

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| id | long | true | 订单id |

>响应结果：

```json
{
  "id": "ch_nfnPWTSOqzT8j18OG4XDeXT8",
  "object": "charge",
  "created": 1426495050,
  "livemode": false,
  "paid": false,
  "refunded": false,
  "app": "app_KavHuL08GO8O4Wbn",
  "channel": "alipay",
  "order_no": "U1D5S1426493572102",
  "client_ip": "127.0.0.1",
  "amount": 40000,
  "amount_settle": 0,
  "currency": "cny",
  "subject": "知心心理咨询",
  "body": "下单时间：2015-03-17，订单号：U1D5S1426493572102",
  "time_paid": null,
  "time_expire": 1426581450,
  "time_settle": null,
  "transaction_no": null,
  "refunds": {
    "object": "list",
    "url": "/v1/charges/ch_nfnPWTSOqzT8j18OG4XDeXT8/refunds",
    "has_more": false,
    "data": []
  },
  "amount_refunded": 0,
  "failure_code": null,
  "failure_msg": null,
  "metadata": {},
  "credential": {},
  "extra": {},
  "description": null
}
```
