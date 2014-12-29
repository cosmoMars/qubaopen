# 获取订单详情

**请求地址：** booking/retrieveDetailInfo/{id}

**HTTP请求方式：** GET

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| id | long | true | 订单id |

>响应结果：

```json
{
  "success" : "1",
  "userId" : 1,
  "userName" : "wo我我",
  "userSex" : 1,
  "birthday" : "",
  "profession" : "",
  "city" : "",
  "married" : false,
  "haveChildren" : false,
  "helpReason" : "",
  "otherProblem" : "",
  "treatmented" : false,
  "haveConsulted" : false
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标识位 |
| userId | long | 用户id |
| userName | String | 用户姓名 |
| userSex | int | 性别 |
| birthday | Date | 生日 |
| profession | String | 职业 |
| city | String | 城市 |
| married | boolean | 已婚否 |
| haveChildren | boolean | 有孩子否 |
| helpReason | String | 求助原因 |
| otherProblem | String | 其他原因 |
| treatmented | boolean | 治疗过否 |
| haveConsulted | boolean | 咨询过否 |
