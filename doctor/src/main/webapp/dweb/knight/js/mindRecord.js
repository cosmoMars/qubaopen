/**
 * Created by Knight on 2014/12/9.
 */


/**
 * 事件
 * */
$(document).ready(function () {

    //获取详细信息 没有则返回列表
    var book=getParam("userId");
    if(!book){
        self.location="orderList.html"
    }else{
        getMindRecord(book);
    }

});


/**
 * 请求
 */


/*获取订单详情*/
function getMindRecord(userId){

    var jsonSent={};
    jsonSent.userId=6;
    $.ajax({
        url: ContextUrl+"/selfUserQuestionnaire/retrieveSelfResult?userId="+userId,
        type: "GET",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {
                //setCookie("cookie1",JSON.stringify(data),new Date() );
                //self.location = location;
                //updateHelpDetailView(data.data);
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

function updateHelpDetailView(aData){


}

/**
 * 其他逻辑处理
 */

