# 获取医师案例列表

**请求地址：** doctorCase/retrieveDoctorCaseList

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| doctorId | long | false | 医师id |

>响应结果：

```json
{
  "success" : "1",
  "more" : true,
  "data" : [ {
    "id" : 1,
    "title" : "愁吃愁穿",
    "createTime" : ""
  } ]
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标志位 |
| more | boolean | 更多 |
| id | long | 文章id |
| title | String | 文章标题 |
| createTime | Date | 创建时间 |
