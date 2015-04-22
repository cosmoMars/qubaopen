# 完成练习

**请求地址：** exercise/confirmExerciseInfo

**HTTP请求方式：** POST

| 请求参数 | 类型 | 是否必选 | 参数说明 |
| -- | -- | -- | -- |
| id | long | true | 练习内容id |

>响应结果：

```json
{
  "success" : "1",
  "uExerciseId " : 1
}
```

**返回字段说明：**

| 参数名称 | 类型 | 参数说明 |
| -- | -- | -- |
| success | String | 成功标志位 |
| uExerciseId | long | 练习内容id |
