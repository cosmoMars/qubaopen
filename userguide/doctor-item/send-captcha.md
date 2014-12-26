# 发送验证码

**请求地址:** uDoctor/sendCaptcha?phone=13621673xxx

**HTTP请求方式：** GET

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| phone | String | true | 手机号码 |
| activated | Boolean | false | 忘记密码true |

>响应结果：

```json
{
    "success": "1"
}
```
