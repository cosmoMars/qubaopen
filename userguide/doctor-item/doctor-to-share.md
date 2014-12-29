# 医师分享

**请求地址：** doctorShare/share

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| time | String | false | yyyy-MM-dd |
| target | Integer | false | 分享目标 |
| origin | Integer | false | 分享类型 0分享软件|

---

注：

target：0新浪微博 1腾讯微博 2微信朋友圈 3 QQ空间 4微信

>响应结果：

```json
{
  "success" : "1"
}
```
