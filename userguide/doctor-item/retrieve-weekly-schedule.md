# 获取医师订单列表

**请求地址：** booking/retrieveWeeklySchedule

**HTTP请求方式：** GET

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| time | String | false | yyyy-MM-dd |

>响应结果：

```json
{
  "success" : "1",
  "result" : [ {
    "dayOfWeek" : 6,
    "day" : "2014-12-20",
    "timeData" : [ {
      "type" : 3,
      "startTime" : "01:00",
      "endTime" : "02:00"
    }, {
      "type" : 2,
      "bookingId" : 3,
      "name" : "小明",
      "helpReason" : "有病",
      "consultType" : 0,
      "startTime" : "14:00",
      "endTime" : "15:00"
    },{
      "type" : 1,
      "timeId" : 1,
      "startTime" : "20:00",
      "endTime" : "00:00",
      "location" : "",
      "content" : ""
    }
    …
    ]
    …
  }]

}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success| String | 成功标记 |
| result | json | |
| dayOfWeek | int |	星期几 |
| day | Date | 日期 |
| timeData | json | |
| startTime | Date | 开始时间 |
| endTime | Date | 结束时间 |
| self | json | |
| timeId | long | bookingTime的id |
| location | String | 地点 |
| content | String | 内容 |
| remindTime | String | 提醒时间 |
| other | json | |
| bookingId | long | 订单id |
| name | String | 病人名称 |
| helpReason | String | 求助原因 |
| consultType | int | 咨询方式 |
| type | int | 0默认有空，1自己的事件，2订单时间 |
