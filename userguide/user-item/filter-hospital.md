# 筛选诊所

**请求地址：** hospital/retrieveHospitalByFilter

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| genreId | long | false | 流派id |
| targetId | long | false | 擅长对象id |
| areaCode | String | false | 地区代码 |
| faceToFace | boolean | false | 面对面 |
| video | boolean | false | 视频 |
| ids | String | false | 不需要的诊所id |

---

注：

ids 1,2,3

>响应结果：

```json
{
  "success" : "1",
  "more" : false,
  "data" : [ {
    "hospitalId" : 5,
    "hospitalName" : "wwwfsss",
    "hospitalAddress" : "aad",
    "hospitalAvatar" : "xxx",
    "hospitalIntroduce" : "eeee",
    "faceToFace" : false,
    "video" : false
  } ]
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标志位 |
| hospitalId | long | 诊所id |
| hospitalName | String | 诊所名称 |
| hospitalAddress | String | 诊所地址 |
| hospitalAvatar | String | 诊所头像 |
| hospitalIntroduce | String | 诊所介绍 |
| faceToFace | boolean | 面对面 |
| video | boolean | 视频 |
| more | boolean | 更多 |
