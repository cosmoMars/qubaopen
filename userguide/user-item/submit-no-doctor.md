# 反馈没有医师

**请求地址：** userFeedBacks/submitNoDoctor

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| area | String | false | 地区 |
| content | String | false | 内容 |
| contactMethod | String | false | 联系方式 |

>响应结果：

```json
{
  "success" : "1"
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标志位 |
