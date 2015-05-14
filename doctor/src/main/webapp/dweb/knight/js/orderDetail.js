/**
 * Created by Knight on 2014/12/4.
 */


/**
 * 事件
 * */
$(document).ready(function () {

    //获取详细信息 没有则返回列表
    var book=getParam("book");
    if(!book){
        self.location="orderList.html"
    }else{
        getOrderDetail(book);
    }

    //查看历史订单
    $("#btnOrderHistory").on("click",function(){

        self.location="orderHistoryList.html?userId="+$("#userName").attr("user-id");


    });

    //查看心理档案
    $("#btnMindRecord").on("click",function(){

        self.location="mindRecord.html?userId="+$("#userName").attr("user-id");


    });
});


/**
 * 请求
 */


/*获取订单详情*/
function getOrderDetail(book){

    $.ajax({
        url: ContextUrl+"/booking/retrieveDetailInfo/"+book,
        type: "GET",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {
                //setCookie("cookie1",JSON.stringify(data),new Date() );
                //self.location = location;
                updateOrderDetailView(data);
            }else if (result == 0) {
                if(data.message=="err000"){
                    backToSignIn();
                }
            }
        }
    });

}

/**
 * 更新页面
 * */

function updateOrderDetailView(obj){

    var data={
        "success" : "1",
        "userId" : 1,
        "userName" : "",
        "userSex" : 1,
        "birthday" : "",
        "profession" : "",
        "city" : "",
        "married" : false,
        "haveChildren" : false,
        "helpReason" : "",
        "otherProblem" : "",
        "treatmented" : false,
        "haveConsulted" : false
    };
    data=obj;
    var content="";
    $("#userName").attr("user-id",data.userId);
    $("#userName").empty().append(data.userName);
    $("#userPhone").empty().append(data.phone);



    content=data.userSex?"男":"女";
    $("#userSex").empty().append(content);

    content=data.birthday?data.birthday:"保密";
    $("#birthday").empty().append(content);

    content=data.profession?data.profession:"保密";
    $("#profession").empty().append(content);

    content=data.city?data.city:"保密";
    $("#city").empty().append(content);

    content=data.married?"已婚":"未婚";
    $("#married").empty().append(content);

    content=data.haveChildren?"有":"无";
    $("#haveChildren").empty().append(content);

    content=data.treatmented?"有":"无";
    $("#treatment").empty().append(content);

    content=data.haveConsulted?"有":"无";
    $("#haveConsulted").empty().append(content);

    content=data.helpReason?data.helpReason:"-";
    $("#helpReason").empty().append(content);

    content=data.otherProblem?data.otherProblem:"无";
    $("#otherProblem").empty().append(content);



}

/**
 * 其他逻辑处理
 */

