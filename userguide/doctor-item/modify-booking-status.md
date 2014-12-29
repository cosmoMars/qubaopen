# 修改订单状态

**请求地址：** booking/modifyBookingStatus

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| time | String | false | yyyy-MM-dd |
| id | long | true | 订单id |
| content | String | false | 拒绝内容 |
| index | String | false | 下标 |

---

注：

index ：预约0，接受1，拒绝2，已咨询3，未咨询4，已约下次5

>响应结果：

```json
{
   "success" : "1"
}
```
