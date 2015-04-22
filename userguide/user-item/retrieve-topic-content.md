# 获取专栏内容

**请求地址：** topic/retrieveTopicContent

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| id | long | true | 专栏id |
| time | String | false | 时间 |

---

注：

time yyyy-MM-dd

>响应结果：

```json
{
  "success" : "1",
  "id" : 1,
  "name" : "2测测测",
  "content" : "测测测2",
  "topicPic" : ""
  "isFavorite" : false
}

```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标志位 |
| id | long | 专栏id |
| name | String | 专栏名称 |
| content | String | 专栏内容 |
| topicPic | String | 专栏图片 |
| isFavorite | boolean | 是否收藏 |
