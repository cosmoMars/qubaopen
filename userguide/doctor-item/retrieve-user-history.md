# 获取对用户历史订单

**请求地址：** booking/retrieveHistory

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| userId | long | true | 用户id |
| idsStr | String | false | 排除的id |

---

注：

idsStr :

    eg : 1,2,3

>响应结果：

```json
{
  "success" : "1",
  "data" : [ {
    "userId" : 1,
    "userName" : "wo我我",
    "helpReason" : "",
    "refusalReason" : "",
    "time" : "2014-11-26 10:31:42",
    "quick" : false,
    "consultType" : "",
    "status" : 3,
    "money" : 10
  } ],
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

