# 查看日程详情

**请求地址：** bookingTime/retrieveBookingTime/{id}

**HTTP请求方式：** GET

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| id | long | true | id |

>响应结果：

```json
{
    "success" : "1",
    "bookingTimeId" : 1,
    "startTime" : "2014-12-01 13:00:00",
    "endTime" : "2014-12-01 14:00:00",
    "location" : "短发短发",
    "content" : "加拿大难得的",
    "remindTime" : 10
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标示 |
| bookingTimeId | Long | 评论id |
| startTime | Date | 开始时间 |
| endTime | Date | 结束时间 |
| userId | Long | 用户id |
| userName | String | 用户姓名 |
| location | String | 地点 |
| content | String | 内容 |
| remindTime | int | 提醒时间 |
| bookingId | long | 订单id |

