# 医师修改默认日程

**请求地址：** bookingTime/modifyBookingTimeByJson

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| json | String | true | |
| date | String | true | 日期 |
| times | String | true | 时间点 |
| type |String | true | 0有空，1没空 |

---

注：

[{"date":"2014-12-20","times":[1,2,3,4,5],"type": 0},{"date":"2014-12-19","times":[2,3,4,5,6],"type":1,"content":"222","location":"3333"}]

>响应结果：

```json
{
    "success" : "1"
}
```
