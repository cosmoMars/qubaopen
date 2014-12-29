# 医师身份证获取

**请求地址：** doctorIdCardBind/retrieveIdCard

**HTTP请求方式：** GET

>响应结果：

```json
{
    "success" : "1",
    "message" : "成功",
    "exist" : true,
    "name" : "XXX",
    "idCard" : "31022619881221XXXX"
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success| String | 成功标记 |
| message | String | 信息 |
| exist | boolean | 是否存在 |
| name | String | 姓名 |
| idCard | String | 身份证号 |

