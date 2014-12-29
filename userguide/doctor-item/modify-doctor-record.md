# 修改医师资质

**请求地址：** doctorRecord/modifyDoctorRecord

**HTTP请求方式：** POST

>响应结果：

```json
{
    "success" : "1",
    "selfTimeId" : 1
}
```

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| educationalStart | String | false | 学习开始时间 yyyy-MM-dd |
| educationalEnd | String | false | 学习结束时间 |
| school | String | false | 学校 |
| profession | String | false | 专业 |
| degree | String | false | 学位 |
| educationalIntroduction | String | false | 学习介绍 |
| trainStart | String | false | 培训开始时间 |
| trainEnd | String | false | 培训结束时间 |
| course | String | false | 课程 |
| organization | String | false | 培训机构 |
| trainIntroduction | String | false | 培续介绍 |
| supervise | String | false | 督导 |
| orientation | String | false | 取向|
| superviseHour | int |	false |	督导总计时间 |
| contactMethod	| String | false | 督导联系方式 |
| superviseIntroduction | String | false |督导介绍 |
| selfStart | String | false | 经历开始时间 |
| selfEnd | String | false | 经历结束时间 |
| totalHour | int |false | 总计小时 |
| selfIntroduction | String | false | 自我介绍 |
| record | MultipartFile | false | 资质证明 |


>响应结果：

```json
{
    "success" : "1"
}
```
