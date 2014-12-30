# 修改医师信息

**请求地址：** doctorInfo/modifyDoctorInfo

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| phone | String | false | 电话 |
| email | String | false | 邮箱 |
| infoName | String | false | 名字 |
| contactPhone | String | false | 联系电话 |
| sex | int | false | 性别 |
| birthday | String | false | 生日 yyyy-MM-dd |
| experience | String | false | 资质 |
| field | String | false | 擅长领域 |
| qq | String | false | qq |
| faceToFace | boolean | false | 面对面 |
| video | boolean | false | 视频 |
| targetUser | String | false | 擅长对象 |
| genre | String | false | 流派 |
| quick | boolean | false | 是否可加急 |
| introduce | String | false | 自我介绍 |
| commentConsult | String | false | 文字咨询 |
| phoneConsult | String | false | 电话咨询 |
| address | String | false | 地址 |
| avatar | MultipartFile | false | 头像 |
| record | MultipartFile | false | 资质证明 |
| times | String | false | 时间表达式 |
| json | String | false | 时间表达式 |

>响应结果：

```json
{
    "success" : "1"
}
```
---

注：

**times：**

格式：
1#0#0,1,2,3,4,5,6&2#1#2,3,5,6

1星期，0类型：0代表修改为有空，1代表修改为占用，0,1,2,3,4,5 代表几点0点1点

**json：**

格式：
[{"day": 1,"type" : 1, "times":[0,1,2,3,4,5,6]},{"day": 2,"type" : 0, "times":[0,1,2,3,4,5,6]}]

day 周几
type 0 有空，1 没空
time 时间段


