# 添加医师文章

**请求地址：** doctorArticle/addArticle

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| title | String | false | 标题 |
| content | String | false | 内容 |
| file | MultipartFile | false | 图片 |

>响应结果：

```json
{
    "success" : "1",
    "articleId" : 2
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标示 |
| articleId | long | 文章id |
