# 根据自测获取心理地图

**请求地址：** mapStatistics/retrieveMapByResult

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| selfId | long | true | 自测id |

>响应结果：

```json
{
  "success" : "1",
  "message" : "成功",
  "userId" : 1,
  "data" : [ {
    "groupId" : 2,
    "mapTitle" : "EPQ测试结果图",
    "chart" : [ ],
    "mapMax" : 100,
    "resultName" : "艾森克人格测试（EPQ）",
    "resultScore" : "",
    "resultContent" : "根据您对EPQ量表的测试，经过量表的标准分数换算得到你的性格偏向，如上图。\n您的性格属于典型情绪不稳定性格和典型友善性格，表示情绪不稳定—焦虑紧张、易怒、抑郁、睡眠不好、情绪反应强烈。而且能与人相处，较好地适应环境，态度温和，不粗暴，善从人意。\n同时您还兼具一些E型性格（外向），爱社交、渴望兴奋、冒险、易冲动、情绪失控、反应快、乐观、做事欠踏实。",
    "managementType" : 1,
    "recommendedValue" : 100,
    "graphicsType" : 5,
    "special" : false,
    "lock" : false,
    "point" : {
      "E" : 68.19,
      "N" : 73.43
    },
    "level" : 3
  } ]
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

