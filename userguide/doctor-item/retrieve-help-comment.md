# 获取求助评价

**请求地址：** help/retrieveHelpComment

**HTTP请求方式：** POST

>响应结果：

```json
{
  "success" : "1",
  "hcGoods" : 0,
  "hcIds" : [ ],
  "data" : [ {
    "helpId" : 17,
    "helpContent" : "好困，不想上班咋办",
    "helpTime" : "2015-04-17",
    "userName" : "喵",
    "userAvatar" : "",
    "commentSize" : 5,
    "commentData" : [ {
      "commentId" : "202",
      "doctorId" : "1",
      "doctorName" : "六1",
      "hospitalId" : "",
      "hospitalName" : "",
      "doctorAvatar" : "http://7xi893.com2.z0.glb.clouddn.com/doctor/d1_201504141828000011",
      "hospitalAvatar" : "",
      "content" : "4891269416",
      "time" : "2015-04-21",
      "goods" : 0
    }, {
      "commentId" : "201",
      "doctorId" : "1",
      "doctorName" : "六1",
      "hospitalId" : "",
      "hospitalName" : "",
      "doctorAvatar" : "http://7xi893.com2.z0.glb.clouddn.com/doctor/d1_201504141828000011",
      "hospitalAvatar" : "",
      "content" : "114555",
      "time" : "2015-04-21",
      "goods" : 0
    } ]
  }, {
    "helpId" : 12,
    "helpContent" : "zhu ti tie shi yong hu fa de",
    "helpTime" : "2015-04-10",
    "userName" : "l1",
    "userAvatar" : "",
    "commentSize" : 3,
    "commentData" : [ {
      "commentId" : "179",
      "doctorId" : "1",
      "doctorName" : "六1",
      "hospitalId" : "",
      "hospitalName" : "",
      "doctorAvatar" : "http://7xi893.com2.z0.glb.clouddn.com/doctor/d1_201504141828000011",
      "hospitalAvatar" : "",
      "content" : "1554",
      "time" : "2015-04-10",
      "goods" : 0
    }, {
      "commentId" : "178",
      "doctorId" : "1",
      "doctorName" : "六1",
      "hospitalId" : "",
      "hospitalName" : "",
      "doctorAvatar" : "http://7xi893.com2.z0.glb.clouddn.com/doctor/d1_201504141828000011",
      "hospitalAvatar" : "",
      "content" : "刚回来",
      "time" : "2015-04-10",
      "goods" : 0
    } ]
  }, {
    "helpId" : 11,
    "helpContent" : "no commet test",
    "helpTime" : "2015-04-10",
    "userName" : "l1",
    "userAvatar" : "",
    "commentSize" : 1,
    "commentData" : [ {
      "commentId" : "177",
      "doctorId" : "1",
      "doctorName" : "六1",
      "hospitalId" : "",
      "hospitalName" : "",
      "doctorAvatar" : "http://7xi893.com2.z0.glb.clouddn.com/doctor/d1_201504141828000011",
      "hospitalAvatar" : "",
      "content" : "啊啊啊",
      "time" : "2015-04-10",
      "goods" : 0
    } ]
  }, {
    "helpId" : 9,
    "helpContent" : "no comment",
    "helpTime" : "2015-04-08",
    "userName" : "783621",
    "userAvatar" : "http://7xi893.com2.z0.glb.clouddn.com/user/u1_201503301327000014",
    "commentSize" : 1,
    "commentData" : [ {
      "commentId" : "176",
      "doctorId" : "1",
      "doctorName" : "六1",
      "hospitalId" : "",
      "hospitalName" : "",
      "doctorAvatar" : "http://7xi893.com2.z0.glb.clouddn.com/doctor/d1_201504141828000011",
      "hospitalAvatar" : "",
      "content" : "111",
      "time" : "2015-04-10",
      "goods" : 0
    } ]
  } ],
  "more" : false
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标示 |
| hcGoods | long | 好评数量 |
| hcIds | String | 好评id |
| helpId | String | 求助id |
| helpContent | String | 求助内容 |
| helpTime | long | 求助时间 |
| userName | String | 用户名称 |
| userAvatar | String | 用户头像 |
| commentSize | String | 评论数量 |
| doctorName | String | 医师名称 |
| doctorAvatar | String | 医师头像 |
| hospitalAvatar | String | 诊所头像 |
| content | String | 求助评论内容 |
| time | String | 求助评论时间 |
| goods | String | 点赞数 |
| more | String | 更多 |

