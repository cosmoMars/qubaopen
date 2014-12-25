# 医师登陆

**请求地址：** uDoctor/login

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| phone | String | true | 用户手机 |
| password | String | true | 密码 |
| idfa | String | false | idfa |
| udid | String | false | udid |
| imei | String | false | imei |

响应结果：
```json
{
    "success" : "1",
    "message" : "登录成功",
    "doctorId" : 1,
    "phone" : "13621673989",
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
    "phoneConsult" : true,
    "timeData" : [{
        "dayId" : 1,
        "startTime" : "1:00",
        "endTime" : "2:00"
      }, {
        "dayId" : 1,
        "startTime" : "2:00",
        "endTime" : "3:00"
      }, {
        "dayId" : 1,
        "startTime" : "3:00",
        "endTime" : "4:00"
      }, {
        "dayId" : 1,
        "startTime" : "4:00",
        "endTime" : "5:00"
      }
    …
    }]
}
```
**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- | -- |
| success | String | 成功标识位 |
| message | long | 返回信息 |
| doctorId | long | 用户 |
| phone | String | 电话 |
| name | String | 身份证名称 |
| infoName | String | 信息名称 |
| contactPhone | String | 联系电话 |
| sex | int | 性别 |
| birthday | Date | 生日 |
| experience | String | 资历 |
| field | String | 领域 |
| qq | String | qq |
| faceToFace | boolean | 面对面 |
| video | boolean | 视频 |
| targetUser | String | 擅长对象 |
| genre | String | 流派 |
| introduce | String | 介绍 |
| quick | boolean | 加急否 |
| email | String | 邮箱 |
| address | String | 地址 |
| idCard | String | 身份证 |
| recordPath | String | 证书 |
| avatarPath | String | 头像 |
| loginStatus | int | 注册状态0未审核，1审核中，2拒绝，3已审核 |
| refauslReason | String | 拒绝原因 |
| commentConsult | boolean | 文字咨询否 |
| phoneConsult | boolean | 电话咨询否 |
| timeData | json | -- |
| dayId | long | dayId |
| startTime | Date | 开始时间 |
| endTime | Date | 结束时间 |
| 返回的是被占用时间 |
