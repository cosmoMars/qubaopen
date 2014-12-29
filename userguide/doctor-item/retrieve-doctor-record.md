# 获取医师资质

**请求地址：** doctorRecord/retrieveDoctorRecord

**HTTP请求方式：** GET

>响应结果：

```json
{
    "success" : "1",
    "educationalStart" : "1988-12-21",
    "educationalEnd" : "1988-12-21",
    "school" : "1",
    "profession" : "2",
    "degree" : "3",
    "educationalIntroduction" : "4",
    "trainStart" : "1988-12-21",
    "trainEnd" : "1988-12-21",
    "course" : "6",
    "organization" : "7",
    "trainIntroduction" : "8",
    "supervise" : "9",
    "orientation" : "10",
    "superviseHour" : 11,
    "contactMethod" : "12",
    "superviseIntroduction" : "13",
    "selfStart" : "1988-12-21",
    "selfEnd" : "1988-12-21",
    "totalHour" : 0,
    "selfIntroduction" : "1988-12-21",
    "record" : "/recordDir/1_20141208-212003.png"
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标示 |
| educationalStart | Date | 学习开始时间 |
| educationalEnd | Date | 学习结束时间 |
| school | String | 学校 |
| profession | String | 专业 |
| degree | String | 学位 |
| educationalIntroduction | String | 学习介绍 |
| trainStart | Date | 培训开始时间 |
| trainEnd | Date | 培训结束时间 |
| course | String | 课程 |
| organization | String | 培训机构 |
| trainIntroduction | String | 培续介绍 |
| supervise | String | 督导 |
| orientation | String | 取向 |
| superviseHour | int | 督导总计时间 |
| contactMethod | String | 督导联系方式 |
| superviseIntroduction | String | 督导介绍 |
| selfStart | Date | 经历开始时间 |
| selfEnd | Date | 经历结束时间 |
| totalHour | int | 总计小时 |
| selfIntroduction | String | 自我介绍 |
| record | String | 资质证明 |
