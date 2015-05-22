# 获取情绪列表

**请求地址：** mapStatistics/retrieveSpecialMap

**HTTP请求方式：** GET


>响应结果：

```json
{
"success": "1",
"message": "成功",
"specialId": 15,
"mapTitle": "正负情绪情感量表",
"userId": 1,
"data": [
{
"groupId": 10,
"chart": [
{
"timeChart": [
1409716800000,
1409716800000,
1409889600000,
1409889600000,
1409889600000,
1409889600000,
1409889600000,
1410321600000,
1410321600000,
1410494400000,
1411358400000,
1411358400000,
1411444800000,
1411444800000,
1411617600000,
1411704000000,
1431748800000,
1432267200000
],
"midChart": [
-19,
-14,
-9,
-2,
10,
22,
-1,
2,
-1,
-10,
-2,
25,
-1,
-5,
0,
2,
0,
0
],
"paChartC": [
27.887421673112605,
-2.0382597250874825,
-2.8476709142507755,
0.42887902322458726
],
"naChartC": [
-27.238946019188578,
3.0762004831958856,
7.6172963059220775,
0.4288790238341064
],
"midChartC": [
0.4831851574290611,
-3.0017163800821836,
5.125812234705092,
0.428879020478639
]
}
],
"mapMax": 10,
"resultName": "",
"resultScore": "",
"resultContent": "您处于情绪周期的高潮期\ 目前，您可能会有“春风得意马蹄疾，一日看尽长安花”的感觉，浑身充满着强烈的生命活力，对人和蔼可亲，感情丰富，做事认真，容易接受别人的规劝，具有心旷神怡之感。在此期间，您的工作和学习效率都会提高，建议您安排一些难度大、较繁琐的任务，一鼓作气，消灭困难。\ ",
"managementType": 2,
"recommendedValue": 9999,
"graphicsType": 4,
"special": true,
"lock": false
}
]
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标示 |
| message | String | 信息 |
| userId | long | 用户 |
| data | json | 数据 |
| chart | json | 图形 |
| mapTitle | String | 地图标题名称 |
| mapMax | int | 地图范围大小 |
| resultName | String | 结果名称 |
| resultScore | int | 结果分数 |
| resultContent | String | 结果内容 |
| managementType | String | 自测类型 |
| recommendedValue | String | 推荐值，优先级 |
| graphicsType | String | 图形类型 |
| special | boolean | 是否特殊题 |
| lock | boolean | 是否有锁 |
| tips | String | 提示 |
| picPath | String | 图片地址 |

---

managementType 1性格，2情绪，3个人

graphicsType 1蛛网图，2折线图，3饼图，4组合折线图，5坐标图

