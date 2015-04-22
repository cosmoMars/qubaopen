# 获取每日发现

**请求地址：** discovery/retrieveDiscoveryContent

**HTTP请求方式：** GET

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| time | String | true | 时间 |

---

注：

time yyyy-MM-dd

>响应结果：


```json
{
  	"success" : "1",
  	"exerciseId" : 1,
  	"exerciseName" : "抑郁练习",
  	"exerciseContent" : "抑郁练习备注",
  	"exerciseNumber" : 1,
  	"exerciseCount" : 2,
  	"userExerciseId" : 2,
  	"exerciseComplete" : "",
  	"messionComplete" : false,
  	"exercisePic" : "",
  	"selfId" : 43,
  	"selfName" : "社会都和谐了，你跟你自己和谐了吗？",
  	"selfContent" : "       自我和谐…",
  	"selfPic" : "",
  	"selfDone" : true,
  	"topicId" : 7,
  	"topicName" : "专栏11113123",
  	"topicContent" : "123123阿斯达111啊啊",
  	"topicPic" : "",
  	"isLogin" : false,
  	"time" : "2015-03-21 ",
  	"more" : false
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标志位 |
| exerciseId | long | 练习id |
| exerciseName | String | 练习名称 |
| exerciseContent | String | 练习内容 |
| exerciseNumber | int | 练习题号 |
| exerciseCount | int | 练习总数 |
| userExerciseId | long | 用户完成id |
| exerciseComplete | int | 连续天数 |
| messionComplete | boolean | 今日是否完成 |
| exercisePic | String | 练习图片 |
| selfId | String | 自测id |
| selfName | String | 自测名称 |
| selfContent | String | 自测内容 |
| selfPic | String | 自测图片地址 |
| selfDone | boolean | 自测是否完成 |
| topicId | long | 专题id |
| topicName | String | 专题名称 |
| topicContent | String | 专题内容 |
| topicPic | String | 专题图片地址 |
| isLogin | boolean | 是否登陆 |
| time | Date | 发现日期 |
| more | boolean | 更多 |
