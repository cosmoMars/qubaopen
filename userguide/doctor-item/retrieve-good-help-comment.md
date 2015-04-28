# 医师获取点赞评论

**请求地址：** help/retrieveGoodHelpComment

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| ids | String | false | 点赞id集合 |

---

注：

ids ：1,2,3

>响应结果：

```json
{
  "success" : "1",
  "data" : [ {
    "userAvatar" : "http://7xi893.com2.z0.glb.clouddn.com/user/u1_201504231529000001",
    "userName" : "783621",
    "doctorName" : "六1",
    "commentContent" : "在和老板的关系上，小王可以尝试着勇敢面对老板。比如当老板走过来时，努力对老板微笑。如果这个过程也很艰难，先在家里练习，对着镜子，想象着镜子里的是老板，尝试着微笑快乐的样子。这样的练习可以通过行为上缓解恐惧，让自己放松。当面对老板微笑时，老板会因为他的反应而轻松。",
    "time" : 13,
    "type" : 1
  }, {
    "userAvatar" : "http://7xi893.com2.z0.glb.clouddn.com/user/u1_201504231529000001",
    "userName" : "783621",
    "doctorName" : "六1",
    "commentContent" : "在和老板的关系上，小王可以尝试着勇敢面对老板。比如当老板走过来时，努力对老板微笑。如果这个过程也很艰难，先在家里练习，对着镜子，想象着镜子里的是老板，尝试着微笑快乐的样子。这样的练习可以通过行为上缓解恐惧，让自己放松。当面对老板微笑时，老板会因为他的反应而轻松。",
    "time" : "2015-04-08 21:22:29",
    "type" : 2
  } ]
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标示 |
| userAvatar | String | 用户头像 |
| userName | String | 用户名称 |
| doctorName | long | 医师名称 |
| commentContent | String | 评论内容 |
| time | String | 评论时间 |
| type | String | 时间类型 |

---
type 1分钟，2日期

