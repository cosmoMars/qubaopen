# 确认取消收藏

**请求地址：** userFavorite/comfirmFavorite

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| selfId | long | false | 自测id |
| topicId | long | false | 专题id |

>响应结果：

```json
{
  "success" : "1",
  "favoriteId " : 1
}

```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标志位 |
| favoriteId | long | 收藏id |
