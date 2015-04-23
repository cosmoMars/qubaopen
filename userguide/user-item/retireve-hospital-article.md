# 获取诊所文章列表

**请求地址：** hospitalArticle/retrieveHospitalArticleList

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| hospitalId | long | false | 诊所id |

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
| id | long | 文章id |
| title | String | 文章标题 |
| createTime | String | 创建时间 |
