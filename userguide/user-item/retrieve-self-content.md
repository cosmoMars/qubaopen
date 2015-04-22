# 获取自测内容

**请求地址：** selfs/retrieveSelfContent

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| id | long | true | 专栏id |

>响应结果：

```json
{
  "success" : "1",
  "id" : 1,
  "name" : "霍兰德职业兴趣（SDS）测试",
  "guidanceSentence" : "       人格和职业有着密切的关系...",
  "tips" : "完成以下问卷",
  "content" : "       霍兰德职业适应性测验….。"
  "isFavorite" : false
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标志位 |
| id | long | 自测id |
| name | String | 自测名称 |
| guidanceSentence | String | 指导语 |
| tips | String | 小贴士|
| content | String | 备注 |
| isFavorite | boolean | 是否收藏 |
