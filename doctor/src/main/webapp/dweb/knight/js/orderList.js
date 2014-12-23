/**
 * Created by Knight on 2014/12/1.
 */


/**
 * 事件
 * */
$(document).ready(function () {
    getOrderList();

    var status,helpId;



    $('#myModal').on('show.bs.modal', showModal);
    $('#myModal').on('hide.bs.modal', hideModal);
    $(window).on("resize", function () {
        $('.modal:visible').each(showModal);
    });

    /*模态框 显示时 执行的方法*/
    function showModal() {
        /*模态框居中*/
        $(this).css('display', 'block');
        var $dialog = $(this).find(".modal-dialog");
        var offset = ($(window).height() - $dialog.height()) / 2;
        // Center modal vertically in window
        $dialog.css("margin-top", offset);


        /*显示标题 拒绝输入框*/
        var $oTitleLabel=$("#myModalLabel");
        switch (status){
            case 1:
                $oTitleLabel.html("确定接受订单？");
                break;
            case 2:
                $oTitleLabel.html("确定拒绝订单？");
                $("#refuseBody").removeClass("hide");
                break;
            default:
                $oTitleLabel.html("确定修改订单状态？");
                break;
        }
    }

    /*模态框 隐藏时 执行的方法*/
    function hideModal(){
        /*清除数据 重置输入框*/
        status="";
        helpId="";
        $("#refuseBody").addClass("hide").find("input").val("");
    }




    //订单按钮 点击 修改订单状态
    $(document).on("click",".help-status",function(event){
        status=parseInt($(this).attr("help-status"));
        helpId=$(this).closest("tr").attr("bookingId");
        $('#myModal').modal("show");
        event.stopPropagation();
    });

    //订单bar 点击事件 进入详情
    $(document).on("click",".booking",function(){
        var bookingId=$(this).attr("bookingId");
        self.location="orderDetail.html?book="+bookingId;

    });



    //订单修改确认框
    //确认框  确定
    $("#modal-ok").on("click",function(){
        modifyBookingStatus(helpId,status);
        $('#myModal').modal("hide");
    });


});


/**
 * 请求
 */
/*获取订单列表*/
function getOrderList(){

    $.ajax({
        url: ContextUrl+"/booking/retrieveBookingInfo",
        type: "POST",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {
                //setCookie("cookie1",JSON.stringify(data),new Date() );
                //self.location = location;
                updateOrderListView(data.data);
            }else if (result == 0) {
                if(data.message=="err000"){
                    backToSignIn();
                }
            }
        }
    });

}


/*更改订单状态*/
function modifyBookingStatus(id,index){
    var jsonSent={};
    jsonSent.id=id;
    jsonSent.index=index;
    //拒绝时的拒绝理由
    if(index==2){
        jsonSent.content=$("#refuseContent").val();
    }
    console.log(jsonSent);

    $.ajax({
        url: ContextUrl+"/booking/modifyBookingStatus",
        type: "POST",
        data:jsonSent,
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {
                //alert("成功!");
                getOrderList();
                //setCookie("cookie1",JSON.stringify(data),new Date() );
                //self.location = location;
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

function updateOrderListView(aData){

//<tr>
//    <th class="dropup">预约时间<span class="caret"></span></th>
//    <th>预约人姓名</th>
//    <th>预约方式</th>
//    <th>金额</th>
//    <th>求助原因</th>
//    <th>预约状态</th>
//    </tr>
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
            returnHtml+='待确认' +
            '<button type="button" class="btn btn-default btn-xs btn-zhixin help-status" help-status="1">接受</button>' +
            '<button type="button" class="btn btn-default btn-xs btn-zhixin help-status" help-status="2">婉拒</button>';

            break;
        case 1:
            returnHtml+='已接受' +
            '<button type="button" class="btn btn-default btn-xs btn-zhixin help-status" help-status="3">已咨询</button>' +
            '<button type="button" class="btn btn-default btn-xs btn-zhixin help-status" help-status="4">未咨询</button>';
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





