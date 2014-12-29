# 获取医师信息

**请求地址：** doctorInfo/retrieveDoctorInfo

**HTTP请求方式：** GET

>响应结果：

```json
{
  "success" : "1",
  "doctorId" : 1,
  "phone" : "",
  "name" : "",
  "infoName" : "李医生",
  "contactPhone" : "",
  "sex" : "",
  "birthday" : "1984-07-13",
  "experience" : "心理专业1级",
  "field" : "精神病",
  "qq" : "32131212231",
  "faceToFace" : false,
  "video" : false,
  "targetUser" : "小学生",
  "genre" : "流派1",
  "time" : "111111100000000000000000,000000010000111110000000,000000001011000000000000,000101010110010100000000,000000001111100000000000,000000001010111000000000,000011000111000110000000",
  "introduce" : "自我介绍1",
  "quick" : false,
  "email" : "",
  "address" : "",
  "idCard" : "",
  "recordPath" : "",
  "avatarPath" : "",
  "loginStatus" : 3,
  "refauslReason" : "",
  "commentConsult" : true,
  "phoneConsult" : true
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标识位 |
| message | String | 返回信息 |
| doctorId | long | 医师id |
| phone | String | 电话 |
| name | String | 身份证名称 |
| infoName | String | 信息名称 |
| contactPhone | String | 联系电话 |
| sex | int | 性别 |
| birthday | Date | 出生年月 |
| experience | String | 资历 |
| field | String | 领域 |
| qq | String | qq |
| faceToFace | boolean | 面对面 |
| video | boolean | 视频 |
| targetUser | String | 擅长对象 |
| genre | String | 流派 |
| time | String | 时间表达式 |
| introduce | String | 介绍 |
| quick | boolean | 是否加急 |
| email | String | 邮箱 |
| address | String | 地址 |
