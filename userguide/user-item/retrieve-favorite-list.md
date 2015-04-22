# 获取收藏列表

**请求地址：** userFavorite/retrieveFavoriteList

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| type | String | false | 类型 |

---
注：

type 0自测， 1专题

>响应结果：

```json
{
  "success" : "1",
  "list" : [ {
    "favoriteId" : 7,
    "selfId" : 5,
    "selfName" : "DISC性格测试",
    "selfContent" : "点点滴滴分"
    "selfUrl" : ""
  } ],
  "more" : false
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标志位 |
| favoriteId | long | 收藏id |
| selfId | String | 自测id |
| selfName | String | 自测名称 |
| selfContent | String | 自测内容 |
| selfUrl | boolean | 自测url |
| more | boolean | 更多 |
