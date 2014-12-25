# 修改密码

**请求地址：** uDoctor/modifyPassword

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| phone | String | true | 用户手机 |
| password | String | true | 密码 |
| captcha | String | true | 验证码 |

响应结果：
```json
{
    "success" : "1"
}
```
