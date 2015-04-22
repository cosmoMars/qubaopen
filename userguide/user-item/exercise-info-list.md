# 练习内容列表

**请求地址：** exerciseInfo/retrieveInfoList

**HTTP请求方式：** GET

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| id | long | true | 练习专题id |

>响应结果：

```json
{
  "success" : "1",
  "size" : 2,
  "data" : [ {
    "id" : 1,
    "name" : "抑郁练习1",
    "content" : "深呼吸1",
    "number" : "1"
  }, {
    "id" : 2,
    "name" : "抑郁练习2",
    "content" : "深呼吸2",
    "number" : "2"
  } ]
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标志位 |
| size | long | 练习总数 |
| id | String | 练习id |
| name | String | 练习名称 |
| content | int | 练习内容 |
| number | int | 练习题号 |
