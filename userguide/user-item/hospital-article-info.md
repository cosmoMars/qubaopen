# 获取文章信息

**请求地址：** hospitalArticle/retrieveHospitalArticle/{id}

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| id | long | true | 文章id |

>响应结果：

```json
{
  "success" : "1",
  "id" : 1,
  "title" : "愁吃愁穿",
  "content" : "吃吃吃",
  "createTime" : ""
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标志位 |
| id | long | 文章id |
| title | String | 文章标题 |
| content | String | 文章内容 |
| createTime | String | 创建时间 |

