# 获取点赞评论

**请求地址：** help/retrieveGoodComment

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| ids | String | false | 点赞id集合 |

>响应结果：

```json
{
  "success" : "1",
  "data" : [ {
    "helpId" : 2,
    "helpContent" : "身边一些三十五六岁的女友至今未婚，也没有男友，她们都很着急，甚至找不到活着的意义了。她们的工作稳定，收入也不错，就是在谈恋爱上不积极。我经常被她们问到活着的意义是什么？这是个哲学问题，我自己也曾问过一些人，但都不是从哲学的角度回复我的，所以我还是疑惑，想问问你。谢谢！",
    "helpTime" : "2014-12-09 10:46:14",
    "commentData" : [ {
      "commentId" : 4,
      "commentContent" : "“身边一些女友”这个句子的主语，应该是“我”吧，“我身边一些三十五六岁的女友……”在这里，“我”是外于这些女友的，与她们有不同的地方，那么不同是什么呢？三十五六岁？女？未婚？我猜测前两项大约相同，不同点在于，你已经结婚，或者至少有了关系稳定的男友。",
      "commentTime" : "2014-12-09 11:01:06",
      "commentSize" : 1
    } ]
  }, {
    "helpId" : 3,
    "helpContent" : "我平时很怕去人多、拥挤的地方，比如商场、地铁。只要身边有人跟我靠得太近，哪怕没有身体接触，我都会心里不舒服，心慌、胸闷。请问这是病吗？我该怎么办？",
    "helpTime" : "2014-12-09 10:47:02",
    "commentData" : [ {
      "commentId" : 6,
      "commentContent" : "第一，采取逐步脱敏法。可在亲友的陪同下，从人少的地方逐步向人多的地方接近。产生紧张情绪时，进行深呼吸，并想像美好轻松的情景。反复训练就能克服这种心理问题",
      "commentTime" : "2014-12-09 11:02:22",
      "commentSize" : 1
    }, {
      "commentId" : 32,
      "commentContent" : "213123",
      "commentTime" : "2015-01-26 15:34:58",
      "commentSize" : 2
    } ]
  } ]
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标示 |
| helpId | String | 求助id |
| helpContent | String | 求助内容 |
| helpTime | long | 求助时间 |
| commentId | String | 评论id |
| commentContent | String | 评论内容 |
| commentTime | String | 评论时间 |
| commentSize | String | 点赞数量 |
