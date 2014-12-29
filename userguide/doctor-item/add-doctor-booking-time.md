# 医师添加日程

**请求地址：** bookingTime/addSelfTime

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| content | String | false | 内容 |
| startTime | String | false | 开始yyyy-MM-dd HH:mm |
| endTime | String | false | 结束yyyy-MM-dd HH:mm |
| location | String | false | 地点 |

>响应结果：

```json
{
    "success" : "1",
    "selfTimeId" : 1
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标示 |
| selfTimeId | long | 日程id |
