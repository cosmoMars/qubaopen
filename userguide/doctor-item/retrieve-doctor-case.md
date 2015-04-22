# 获取医师案例列表

**请求地址：** doctorCase/retrieveCaseList

**HTTP请求方式：** GET

>响应结果：

```json
{
  "success" : "1",
  "data" : [ {
    "id" : 2,
    "title" : "ddddff",
    "content" : "qqqqqq",
    "createdDate" : "2015-04-20 17:15:11",
    "status" : 0,
    "refusalReason" : "",
    "url" : "http://7xi893.com2.z0.glb.clouddn.com/doctor/article/da1_201504201715000004"
  }, {
    "id" : 1,
    "title" : "ddddff",
    "content" : "qqqqqq",
    "createdDate" : "2015-04-20 16:57:02",
    "status" : 0,
    "refusalReason" : "",
    "url" : "http://7xi893.com2.z0.glb.clouddn.com/doctor/article/da1_201504201657000001"
  } ],
  "more" : false
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标示 |
| id | long | 文章id |
| title | String | 标题 |
| content | String | 内容 |
| createdDate | Date | 创建时间 |
| status | int | 状态位 |
| refusalReason | String | 拒绝原因 |
| url | String | 图片地址 |
| more | boolean | 更多 |

---

注：

status ：0 审核中，1 失败，2 成功
