# 医师身份证认证

**请求地址：** doctorIdCardBind/submitIdCard

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| idCard | String | true | 身份证号 |
| name | String | true | 姓名 |

>响应结果：

```json
{
  "success" : "1",
  "message" : "认证成功"
}
```

