# 获取诊所列表

**请求地址：** hospital/retrieveHospitalList

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| ids | String | false | id集合 |

---
例：

ids 1,2,3

>响应结果：

```json
{
  "success" : "1",
  "data" : [ {
    "hospitalId" : 5,
    "hospitalName" : "wwwfsss"
  }, {
    "hospitalId" : 9,
    "hospitalName" : ""
  }, {
    "hospitalId" : 10,
    "hospitalName" : "知心诊所"
  } ]
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标志位 |
| hospitalId | long | 诊所id |
| hospitalName | String | 诊所名称 |

