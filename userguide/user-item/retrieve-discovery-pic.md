# 获取明日发现图片

**请求地址：** discoveryPic/retrieveDiscoveryPic

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| month | String | false | 时间 |

---

注：

month yyyy-MM

>响应结果：

```json
{
  "success" : "1",
  "picId" : 1,
  "picName" : "xxxx",
  "picUrl" : "xxxx"
}

```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标志位 |
| picId | long | 图片id |
| picName | String | 图片名称 |
| picUrl | String | 图片地址 |
