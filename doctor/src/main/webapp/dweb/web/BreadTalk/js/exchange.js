/**
 * Created by Knight on 2015/1/23.
 */

$(document).ready(function () {


    $("#btn-submit").on("click",function(){
        checkCode();
    });

});
function checkCode(){
    var code=$("#code").val();
    if(code==""){
        alert("输入不能为空");
        return;
    }

    var jsonSent={};
    jsonSent.code=code;
    $("#message").empty();
    $.ajax({
        url: "http://zhixin.me:8080/know-heart/breakTalk/exchangeBreadTalk",
        type: "POST",
        dataType: "json",
        data: jsonSent,
        success: function (data, textStatus, jqXHR) {
            var result = data.success;

            console.log(data);
            if (result == 1) {
                $("#message").append(data.money+"元抵用券使用成功");
            }else if (result == 0) {
                $("#message").append(data.message);
            }

        }
    });
}