# 修改授权

**请求地址：** userAuthorization/modifyAuthorization

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| authorise | boolean | false | 授权 |
| doctorId | long | false | 医师id |

>响应结果：

```json
{
  "success" : "1"
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标示 |
