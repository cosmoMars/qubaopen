# 修改医师注册状态

**请求地址：** uDoctor/modifyLoginStatus

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| id | long | true | 医师id |
| index | int | true | 下标 |
| content | String | false | 拒绝内容 |

----

说明：

index : 0未审核，1审核中，2拒绝，3已审核

>响应结果：

```json
{
    "success" : "1"
}
```
