# 获取医师推送

**请求地址：** doctorUdid/modifyUdid

**HTTP请求方式：** GET

>响应结果：

```json
{
  "success" : "1",
  "message" : "成功",
  "id" : 4,
  "push" : false,
  "startTime" : "09:00",
  "endTime" : "22:00"
}

```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标识位 |
| message | String | 返回信息 |
| id | long | id |
| push | String | 是否推送 |
| startTime | String | 开始时间 |
| endTime | String | 结束时间 |
