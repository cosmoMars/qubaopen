# 获取自测结果列表

**请求地址：** selfs/retrieveSelfResult

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| page | int | false | 页码 |
| size | int | false | 大小 |

>响应结果：

```json
{
  "success" : "1",
  "data" : [ {
    "id" : "",
    "resultTitle" : "",
    "content" : "",
    "optionTitle" : "",
    "resultRemark" : "",
    "optionNum" : ""
  }, {
    "id" : 205,
    "resultTitle" : "",
    "content" : "       通常情况下，。。。",
    "optionTitle" : "您容纳他人的程度较低",
    "resultRemark" : "",
    "optionNum" : "1"
  } ],
  "more" : false
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标志位 |
| id | long | 自测id |
| resultTitle | String | 结果标题 |
| content | String | 结果内容 |
| optionTitle | String | 选项标题 |
| resultRemark | boolean | 结果备注 |
| optionNum | boolean | 结果题号 |
| more | boolean | 更多 |
