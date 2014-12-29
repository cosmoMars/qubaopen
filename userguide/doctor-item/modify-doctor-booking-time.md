# 医师修改日程

**请求地址：** bookingTime/modifySelfTime

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| id | long | true | id |
| content | String | false | 内容 |
| startTime | String | false | 开始yyyy-MM-dd HH:mm |
| endTime | String | false | 结束yyyy-MM-dd HH:mm |
| location | String | false | 地点 |

>响应结果：

```json
{
    "success" : "1"
}
```
