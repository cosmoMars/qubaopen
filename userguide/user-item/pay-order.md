# 发起支付

**请求地址：** pingpp/pingppOrder

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| bookingId | long | true | 订单id |
| quick | boolean | false | 加急 |
| money | double | false | 金额 |
| time | String | false | 预约时间 |
| type | String | false | 支付类型 |

---

注：

time ： yyyy-MM-dd HH:mm

type ：1支付宝，2微信，3银联

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
  "credential": {
    "object": "credential",
    "alipay": {
      "orderInfo": "_input_charset\u003d\"utf-8\"\u0026body\u003d\"下单时间：2015-03-17，订单号：U1D5S1426493572102\"\u0026it_b_pay\u003d\"1440m\"\u0026notify_url\u003d\"https%3A%2F%2Fapi.pingxx.com%2Fnotify%2Fcharges%2Fch_nfnPWTSOqzT8j18OG4XDeXT8\"\u0026out_trade_no\u003d\"U1D5S1426493572102\"\u0026partner\u003d\"2008331798592916\"\u0026payment_type\u003d\"1\"\u0026seller_id\u003d\"2008331798592916\"\u0026service\u003d\"mobile.securitypay.pay\"\u0026subject\u003d\"知心心理咨询\"\u0026total_fee\u003d\"400\"\u0026sign\u003d\"Q3VmVEtLUFc5T0NTSHVESzBPUFdqMUNP\"\u0026sign_type\u003d\"RSA\""
    }
  },
  "extra": {},
  "description": null
}
```


