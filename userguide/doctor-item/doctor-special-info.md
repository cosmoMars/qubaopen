# 获取医师专栏

**请求地址：** doctorInfo/retrieveInfoSpecial

**HTTP请求方式：** GET

>响应结果：

```json
{
  "success" : "1",
  "address" : "南京市",
  "video" : false,
  "faceToFace" : true,
  "introduce" : "",
  "articleCount" : 0,
  "caseCount" : 0
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标示 |
| address | String | 地址 |
| video | boolean | 是否视频 |
| faceToFace | boolean | 是否面对面 |
| introduce | String | 介绍 |
| articleCount | int | 文章数量 |
| caseCount | int | 案例数量 |
