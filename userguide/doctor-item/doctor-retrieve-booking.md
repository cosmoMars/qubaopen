# 获取订单列表

**请求地址：** booking/retrieveBookingInfo

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| index | int | true | 咨询状态下标 |
| idsStr | String | false | 排除的id |

----

注：

index : 0 预约，1 接受，2 拒绝，3 已咨询，4 未咨询，5 已约下次, 6 已付款

>响应结果：

```json
{
  "success" : "1",
  "data" : [{
    "bookingId" : 1,
    "userId" : 1,
    "userName" : "wo我我",
    "helpReason" : "",
    "refusalReason" : "",
    "time" : "2014-11-26 10",
    "quick" : false,
    "consultType" : "",
    "status" : 3,
    "money" : 10
  }],
  "more" : false
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标识位 |
| data | json | 返回信息 |
| userId | long | 用户id |
| userName | String | 用户姓名 |
| helpReason | String | 求助原因 |
| refusalReason | String | 拒绝原因 |
| time | Date | 预约时间 |
| quick | boolean | 是否加急 |
| consultType | int | 咨询方式 |
| status | int | 咨询状态 |
| money | double | 金额 |
| more | boolean | 更多 |

----

注：

咨询方式： 0 facetoface 1 video
