# 获取用户心理档案

**请求地址：** selfUserQuestionnaire/retrieveSelfResult

**HTTP请求方式：** GET

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| userId | long | true | 用户id |

>响应结果：

```json
{
  "success" : "1",
  "data" : [{
    "id" : 117,
    "resultTitle" : "",
    "content" : "1.个性特点：很传统…",
    "optionTitle" : "猫头鹰型人格——精确型",
    "resultRemark" : "",
    "optionNum" : ""
  }]
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标示 |
| data | json | |
| id | long | id |
| resultTitle | String | 结果标题 |
| content | String | 内容 |
| optionTitle | String | 选项标题 |
| resultRemark | String | 结果备注 |
| optionNum | String | 选项号 |

