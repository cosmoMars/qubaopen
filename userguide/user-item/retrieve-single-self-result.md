# 获取自测答案

**请求地址：** selfs/retrieveResultBySelfId/{id}

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| id | long | true | 自测id |

>响应结果：

```json
{
  "success" : "1",
  "id" : 80,
  "resultTitle" : "",
  "content" : "【性格描述】\\n1、淡泊名利\\
  "optionTitle" : "极端B型性格",
  "resultRemark" : "",
  "optionNum" : "5"
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
