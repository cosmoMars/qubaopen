/**
 * Created by Knight on 2014/12/1.
 */


/**
 * 事件
 * */
$(document).ready(function () {
    var status,helpId;



    $('#myModal').on('show.bs.modal', showModal);
    $('#myModal').on('hidden.bs.modal', hideModal);
    $(window).on("resize", function () {
        $('.modal:visible').each(showModal);
    });

    /*模态框 显示时 执行的方法*/
    function showModal() {
        /*模态框居中*/
        //$(this).css('display', 'block');
        //var $dialog = $(this).find(".modal-dialog");
        //var offset = ($(window).height() - $dialog.height()) / 2;
        //// Center modal vertically in window
        //$dialog.css("margin-top", offset);


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
            case 7:
                $oTitleLabel.html("确认接受此订单？");
                $("#warningBody").removeClass("hide");
                break;
            case 8:
                $oTitleLabel.html("请选择改约时间");
                $("#warningBody").removeClass("hide");
                $(".conflict").addClass("hide");
                break;
            case 10:
                $oTitleLabel.html("确认用户未前来进行咨询？");
                break;
            case 11:
                $oTitleLabel.html("确认用户已前来完成咨询？");
                break;
            case 12:
                $oTitleLabel.html("请填写与用户电话确认好的预约时间");
                $("#timeBody").removeClass("hide");
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
        $("#warningBody").addClass("hide");
        $("#timeBody").addClass("hide");
        $(".conflict").removeClass("hide");
        $("#changeTime").val("");
        $("#conflictId").attr("data-id","");
    }

    //订单按钮 点击 诊断记录
    $(document).on("click",".diagnosisRecord",function(event){

        helpId=$(this).closest("tr").attr("bookingId");

        alert(helpId);

        event.stopPropagation();
    });


    //订单按钮 点击 修改订单状态
    $(document).on("click",".change-status",function(event){
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

    $(document).on("click",".checkConflict",function(){
        getChangeDateBooking(helpId);
    });
    $(document).on("click",".checkScheduler",function(){
        window.open("knightScheduler.html");
    });



    //订单修改确认框
    //确认框  确定
    $("#modal-ok").on("click",function(){
        if(status==1 || status==2 || status==5){
            modifyBookingStatus(helpId,status);
        }else if(status==7){
            //接受改约
            var conflictId=$("#conflictId").attr("data-id");
            if(conflictId==""){
                alert("请先获取订单");
                return;
            }else if(conflictId==0){
                modifyBookingStatus(helpId,status);
            }else{
                var changeTime=$("#changeTime").val();
                if(changeTime==""){
                    alert("请选择时间");
                    return;
                }
                changeBookingDate(conflictId,changeTime);
                modifyBookingStatus(helpId,status);
            }
        }else if(status==8){
            //改约
            var changeTime=$("#changeTime").val();
            changeBookingDate(helpId,changeTime);

        }else if(status==10){
            cofirmBookingStatus(helpId,0);
        }else if(status==11){
            cofirmBookingStatus(helpId,1);
        }else if(status==12){
            modifyBookingStatus(helpId,status);
        }
        $('#myModal').modal("hide");
    });


});


/**
 * 请求
 */
/*获取订单列表*/
function getOrderList(){

    $.ajax({
        url: ContextUrl+"/hospitalBooking/retrieveHospitalBooking",
        type: "post",
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
    if(index==12){
        jsonSent.bookingTime=$("#bookingTime").val();
    }
    console.log(jsonSent);

    $.ajax({
        url: ContextUrl+"/hospitalBooking/modifyBookingStatus",
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

/*改约订单时间*/
function changeBookingDate(id,date){
    var jsonSent={};
    jsonSent.id=id;
    jsonSent.date=date;
    console.log(jsonSent);

    return;
    $.ajax({
        url: ContextUrl+"/booking/changeDateForBooking",
        type: "POST",
        data:jsonSent,
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {

                getOrderList();

            }else if (result == 0) {
                if(data.message=="err000"){
                    backToSignIn();
                }
            }
        }
    });
}


/*获取冲突订单*/
function getChangeDateBooking(qBookingId){
    var jsonSent={};
    jsonSent.qBookingId=qBookingId;
    console.log(jsonSent);

    $.ajax({
        url: ContextUrl+"/booking/retrieveChangeDateBooking",
        type: "POST",
        data:jsonSent,
        async:false,
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {
                if(data.data.length>0){
                    $("#conflictId").attr("data-id",data.data[0].id);
                    window.open("orderDetail.html?book="+data.data[0].id);
                }else{
                    alert("没有找到冲突订单,可以点确认直接接受订单");
                    $("#conflictId").attr("data-id",0);
                }

            }else if (result == 0) {
                if(data.message=="err000"){
                    backToSignIn();
                }
            }
        }
    });
}

/*医师确认订单*/
function cofirmBookingStatus(bookingId,idx){
    var jsonSent={};
    jsonSent.bookingId=bookingId;
    jsonSent.idx=idx;
    console.log(jsonSent);

    return;
    $.ajax({
        url: ContextUrl+"/booking/confirmDoctorBookingStatus",
        type: "POST",
        data:jsonSent,
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {

                getOrderList();

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
    var data={bookingId:"",consultType:"",helpReason:"",money:"",quick:"",falserefusalReason:"",status:"",time:"",userId:"",userName:"",userStatus:"",doctorStatus:""};
    oMainView.empty();
    for(var i=0;i<aData.length;i++){
        data=aData[i];
        var appendHtml='<tr class="booking" bookingId="'+data.bookingId+'">' +
            '<td>'+data.time+'</td>' +
            '<td>'+data.userName+'</td>' +
            '<td>'+consultType2Html(data)+'</td>' +
            '<td>'+data.money+'</td>' +
            '<td>'+data.helpReason+'</td>' +
            '<td>'+status2Html(data)+'</td>' +
            '</tr>';
        oMainView.append(appendHtml);
    }


}

/**
 * 其他逻辑处理
 */
/*根据订单状态来确定页面元素*/
function status2Html(data){
    var returnHtml="";
    var status=data.status;
    //0 预约，1 订单接受，2 拒绝，3 已咨询，4 未咨询， 5已约下次，6已付款，7付款接受（医师）， 8 改约
    switch(status){
        case 0:
            returnHtml+='' +
            '<button type="button" class="btn btn-default btn-xs btn-zhixin book-status change-status gray" help-status="2">婉拒</button>' +
            '<button type="button" class="btn btn-default btn-xs btn-zhixin book-status change-status orange" help-status="1">接受</button>';

            break;
        case 1:
            returnHtml+='<button type="button" class="btn btn-default btn-xs btn-zhixin book-status change-status orange" help-status="12">选择时间</button>';
            break;
        case 2:
            returnHtml+='<div class="text-status gray">已婉拒</div>';
            break;
        case 3:
            returnHtml+=
                '<button type="button" class="btn btn-default btn-xs btn-zhixin book-status gray diagnosisRecord" help-status="">诊断记录</button>' +
                '<button type="button" class="btn btn-default btn-xs btn-zhixin book-status change-status orange" help-status="5">已约下次</button>';
            break;
        case 4:
            returnHtml+='<div class="text-status gray">未咨询</div>';
            break;
        case 5:
            returnHtml+=
                '<button type="button" class="btn btn-default btn-xs btn-zhixin book-status gray diagnosisRecord" help-status="">诊断记录</button>' +
                '<div class="text-status orange">已约下次</div>';
            break;
        case 6:
            returnHtml+=
                '<button type="button" class="btn btn-default btn-xs btn-zhixin book-status change-status gray" help-status="8">改约</button>' +
                '<button type="button" class="btn btn-default btn-xs btn-zhixin book-status change-status gray" help-status="2">婉拒</button>' +
                '<button type="button" class="btn btn-default btn-xs btn-zhixin book-status change-status orange" help-status="7">接受</button>';
            break;
        case 7:
            if(data.userStatus == "" && data.doctorStatus == ""){
                returnHtml+=
                    '<button type="button" class="btn btn-default btn-xs btn-zhixin book-status change-status gray" help-status="8">改约</button>' +
                    '<button type="button" class="btn btn-default btn-xs btn-zhixin book-status change-status gray" help-status="10">未咨询</button>' +
                    '<button type="button" class="btn btn-default btn-xs btn-zhixin book-status change-status orange" help-status="11">已咨询</button>';
            }else if(data.userStatus != "" && data.doctorStatus == ""){
                returnHtml+=
                    '<button type="button" class="btn btn-default btn-xs btn-zhixin book-status change-status gray" help-status="10">未咨询</button>' +
                    '<button type="button" class="btn btn-default btn-xs btn-zhixin book-status change-status orange" help-status="11">已咨询</button>';
            }else if(data.doctorStatus != ""){
                returnHtml+=
                    '<div class="text-status orange">等待确认</div>';
            }

            break;
        case 8:
            returnHtml+='<div class="text-status orange">正在改约</div>';
            break;
        case 9:
            returnHtml+='<div class="text-status gray">退款中</div>';
            break;
        case 10:
            returnHtml+='<div class="text-status gray">已退款</div>';
            break;
        case 11:
            returnHtml+='<div class="text-status gray">订单关闭</div>';
            break;
        case 12:
            returnHtml+='<div class="text-status gray">付款中</div>';
            break;
        default:
            returnHtml+="默认";
            break;
    }
    return returnHtml;
}

function consultType2Html(data){
    var returnHtml="";
    var consultType=data.consultType;
    var quick=data.quick;

    if(consultType==0){
        returnHtml+='<img height="25" width="25" src="../../img/face02.png"/>';
    }else if(consultType==1){
        returnHtml+='<img height="25" width="25" src="../../img/web01.png"/>';
    }


    if(quick==1){
        returnHtml+='<img height="25" width="25" src="../../img/ji03.png"/>';
    }

    return returnHtml;
}




