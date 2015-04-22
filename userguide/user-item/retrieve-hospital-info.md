# 获取诊所详情

**请求地址：** hospital/retrieveHosptialDetial/{id}

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| id | String | false | 诊所id |

>响应结果：

```json
{
  "success" : "1",
  "name" : "wwwfsss",
  "address" : "aad",
  "establishTime" : "",
  "phone" : "13232323232",
  "urgentPhone" : "",
  "qq" : "232323",
  "introduce" : "eeee",
  "wordsConsult" : false,
  "minCharge" : 0,
  "maxCharge" : 0,
  "records" : 9
}

```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标志位 |
| name | long | 诊所名称 |
| address | String | 诊所地址 |
| establishTime | String | 创建时间 |
| phone | String | 电话 |
| urgentPhone | String | 紧急联系电话 |
| qq | String | 扣扣 |
| introduce | String | 诊所介绍 |
| wordsConsult | boolean | 是否文字咨询 |
| minCharge | String | 最低价钱 |
| maxCharge | String | 最高价钱 |
| records | String | 医师数量 |

