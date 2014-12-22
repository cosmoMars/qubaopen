/**
 * Created by Knight on 2014/12/4.
 */


/**
 * 事件
 * */
$(document).ready(function () {
    var book=getParam("userId");
    if(!book){
        self.location="orderList.html"
    }else{
        getOrderHistoryList(book);
    }


});


/**
 * 请求
 */
/*获取订单历史列表*/
function getOrderHistoryList(userId){
    var jsonSent={};
    jsonSent.userId=userId;
    $.ajax({
        url: ContextUrl+"/booking/retrieveHistory",
        type: "POST",
        data:jsonSent,
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            var result = data.success;
            console.log(data);

            if (result == 1) {
                updateOrderHistoryListView(data.data);
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

function updateOrderHistoryListView(aData){


    var oMainView=$("#main-view");
    var data={bookingId:"",consultType:"",helpReason:"",money:"",quick:"",falserefusalReason:"",status:"",time:"",userId:"",userName:""};
    oMainView.empty();
    for(var i=0;i<aData.length;i++){
        data=aData[i];
        var appendHtml='<tr class="booking" bookingId="'+data.bookingId+'">' +
            '<td>'+data.time+'</td>' +
            '<td>'+data.userName+'</td>' +
            '<td>'+data.consultType+'</td>' +
            '<td>'+data.money+'</td>' +
            '<td>'+data.helpReason+'</td>' +
            '<td>'+status2Html(data.status)+'</td>' +
            '</tr>';
        oMainView.append(appendHtml);
    }


}

/**
 * 其他逻辑处理
 */
/*根据订单状态来确定页面元素*/
function status2Html(status){
    var returnHtml="";
    switch(status){
        case 0:
            returnHtml+='待确认';

            break;
        case 1:
            returnHtml+='已接受';
            break;
        case 2:
            returnHtml+="已拒绝";
            break;
        case 3:
            returnHtml+="已咨询";
            break;
        case 4:
            returnHtml+="未咨询";
            break;
        case 5:
            returnHtml+="已约下次";
            break;
        default:
            returnHtml+="默认";
            break;
    }
    return returnHtml;
}





