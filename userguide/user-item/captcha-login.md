# 短信登陆

**请求地址：** users/captchaLogin

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| phone | String | true | 手机 |
| captcha | String | true | 验证码 |
| loginType | String | true | 登陆方式 |
| udid | String | false | udid |
| idfa | String | false | idfa |
| imei | String | false | imei |

---

注：

loginType ：1 手动登陆，2 自动登陆

>响应结果：

```json
{
  "success" : "1",
  "message" : "登录成功",
  "userId" : 5,
  "phone" : "13917377795",
  "name" : "",
  "sex" : "",
  "nickName" : "",
  "bloodType" : "",
  "district" : "",
  "email" : "",
  "defaultAddress" : "",
  "defaultAddressId" : "",
  "consignee" : "",
  "defaultAddressPhone" : "",
  "idCard" : "",
  "birthday" : "",
  "avatarPath" : "",
  "signature" : ""
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标志位 |
| userId | String | 用户id |
| phone | String | 手机 |
| name | String | 姓名 |
| sex | String | 性别 |
| bloodType | String | 血型 |
| district | String | 区 |
| email | String | 邮箱 |
| defaultAddress | String | 默认地址 |
| defaultAddressId | String | 默认地址id |
| consignee | String | 收货人 |
| defaultAddressPhone | String | 默认地址手机 |
| idCard | String | 身份证 |
| birthday | String | 生日 |
| avatarPath | String | 头像 |
| signature | String | 签名 |
