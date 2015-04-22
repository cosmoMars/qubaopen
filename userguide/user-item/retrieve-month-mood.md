# 获取每月心情概况

**请求地址：** userMood/retrieveMonthMoodList

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| month | String | false | 月 |

---
注：

month yyyy-MM

>响应结果：

```json
{
  "success" : "1",
  "monthData" : [ {
    "day" : "2015-03-12",
    "haveMood" : false,
    "status" : ""
  }, {
    "day" : "2015-03-12",
    "haveMood" : true,
    "status" : 1
  } ]
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标志位 |
| day | Date | 时间 |
| haveMood | boolean | 是否有心情 |
| status | int | 心情状态 |
