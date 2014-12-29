# 获取订单时间列表

**请求地址：** booking/retrieveBookingList

**HTTP请求方式：** GET

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| time | String | false | yyyy-MM-dd |

>响应结果：

```json
{
  "success" : "1",
  "dayData" : [ {
    "dayId" : 1,
    "dayOfWeek" : 7,
    "day" : "2014-12-07"
  }, {
    "dayId" : 2,
    "dayOfWeek" : 1,
    "day" : "2014-12-08"
  },
  …
  ],
  "timeData" : [ {
    "time" : [ {
      "dayId" : 1,
      "startTime" : "00:00",
      "endTime" : "01:00"
    }, {
      "dayId" : 1,
      "startTime" : "01:00",
      "endTime" : "02:00"
    },
 	 …
    ],
    "self" : [ {
      "timeId" : 1,
      "startTime" : "2014-12-05 13:00:00",
      "endTime" : "2014-12-05 14:00:00",
      "location" : "短发短发",
      "content" : "加拿大难得的",
      "remindTime" : 10
    } ],
    "other" : [ {
      "bookingId" : 5,
      "name" : "小明3",
      "helpReason" : "有病",
      "consultType" : 0,
      "startTime" : "2014-12-06 15:35:15",
      "endTime" : "2014-12-06 16:00:00"
    }, {
      "bookingId" : 6,
      "name" : "小明4",
      "helpReason" : "有病",
   "consultType" : 0,
      "startTime" : "2014-12-06 15:35:15",
      "endTime" : "2014-12-06 16:00:00"
    } ]
  },
  …
  ]

}

```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success |	String | 成功标记 |
| dayData | json | |
| dayId	| Long | Dayid |
| day |	Date | 日期 |
| timeData | json | |
| startTime	| Date | 开始时间 |
| endTime | Date | 结束时间 |
| self | json | |
| timeId | long | 安排id |
| location | String	| 地点 |
| content | String | 内容 |
| remindTime | String | 提醒时间 |
| other	| json | |
| bookingId | long | 订单id |
| name | String	| 病人名称 |
| helpReason | String | 求助原因 |
| consultType |int | 咨询方式 |
