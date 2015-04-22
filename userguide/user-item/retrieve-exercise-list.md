# 获取练习列表

**请求地址：** exercise/retrieveExerciseList

**HTTP请求方式：** GET

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| id | long | true | 练习专题id |

>响应结果：

```json
{
  "success" : "1",
  "uExerciseCount" : 1,
  "data" : [ {
    "id" : 1,
    "name" : "抑郁练习",
    "pic" : "",
    "size" : 2
  }, {
    "id" : 2,
    "name" : "启迪练习",
    "pic" : "",
    "size" : 2
  } ]
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标志位 |
| uExerciseCount | int | 用户完成练习总数 |
| id | long | 练习id |
| name | String | 练习名称 |
| pic | String | 练习图片 |
| size | int | 练习小题总数 |
