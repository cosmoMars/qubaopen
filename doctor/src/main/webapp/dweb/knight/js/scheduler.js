/**
 * Created by Knight on 2014/12/6.
 */


/**
 * 事件
 * */
$(document).ready(function () {
    //getOrderListByMonth();



});


/**
 * 请求
 */
/*获取日程(订单)列表*/
function getOrderListByMonth(time,iPage){
    $.ajax({
        url: ContextUrl+"/booking/retrieveWeeklySchedule?time="+time,
        type: "GET",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {
                updateTableData(data.result,iPage);
                //setCookie("cookie1",JSON.stringify(data),new Date() );
                //self.location = location;
            }else if (result == 0) {
                if(data.message=="err000"){
                    backToSignIn();
                }
            }
        },
        error: function(xhr,status,error){
           if(xhr.status=="500"){
               //self.location = "signin.html";
           }
        }
    });
}



/* 添加日程 */
function addSelfEvent(){
    var jsonSent={};
    var content,startTime,endTime,location;
    startTime=$("#starttime").val();
    endTime=$("#endtime").val();
    content=$("#content").val();
    location=$("#location").val();

    if(content==""){
        alert("活动内容不能为空");
        return;
    }
    if(newDate(startTime) > newDate(endTime)){
        alert("开始时间不能大于结束时间");
        return;
    }


    jsonSent.startTime=startTime;
    jsonSent.endTime=endTime;
    jsonSent.content=content;
    jsonSent.location=location;
    //console.log(jsonSent);
    $.ajax({
        url: ContextUrl+"/bookingTime/addSelfTime",
        type: "POST",
        dataType: "json",
        data:jsonSent,
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {
                refreshMainTable();
                //setCookie("cookie1",JSON.stringify(data),new Date() );
                //self.location = location;
            }else if (result == 0) {
                if(data.message=="err000"){
                    backToSignIn();
                }
            }
        },
        error: function(xhr,status,error){
            if(xhr.status=="500"){
                //self.location = "signin.html";
            }
        }
    });
}

/* 修改日程 */
function modifySelfEvent(){
    var jsonSent={};
    var content,startTime,endTime,location;
    startTime=$("#starttime").val();
    endTime=$("#endtime").val();
    content=$("#content").val();
    location=$("#location").val();

    if(content==""){
        alert("活动内容不能为空");
        return;
    }
    if(newDate(startTime) > newDate(endTime)){
        alert("开始时间不能大于结束时间");
        return;
    }


    jsonSent.id=$("#timeId").val();
    jsonSent.startTime=startTime;
    jsonSent.endTime=endTime;
    jsonSent.content=content;
    jsonSent.location=location;
    //console.log(jsonSent);
    $.ajax({
        url: ContextUrl+"/bookingTime/modifySelfTime",
        type: "POST",
        dataType: "json",
        data:jsonSent,
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {
                refreshMainTable();
                //setCookie("cookie1",JSON.stringify(data),new Date() );
                //self.location = location;
            }else if (result == 0) {
                if(data.message=="err000"){
                    backToSignIn();
                }
            }
        },
        error: function(xhr,status,error){
            if(xhr.status=="500"){
                //self.location = "signin.html";
            }
        }
    });
}

/* 删除日程 */
function removeSelfEvent(){
    var jsonSent={};
    jsonSent.id=$("#timeId").val();
    //console.log(jsonSent);
    $.ajax({
        url: ContextUrl+"/bookingTime/deleteSelfTime",
        type: "POST",
        dataType: "json",
        data:jsonSent,
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {
                refreshMainTable();
                //setCookie("cookie1",JSON.stringify(data),new Date() );
                //self.location = location;
            }else if (result == 0) {
                if(data.message=="err000"){
                    backToSignIn();
                }
            }
        },
        error: function(xhr,status,error){
            if(xhr.status=="500"){
                //self.location = "signin.html";
            }
        }
    });
}


/**
 * 更新页面
 * */
function updateTableData(aData,iPage){

    var aTime;
    var content="";
    for(var i=0;i<aData.length;i++){
        aTime=aData[i].timeData;
        for(var j=0;j<aTime.length;j++){
            var d,oTd;
            if(aTime[j].type==1){
                d=newDate(aData[i].day+" "+aTime[j].startTime);
                oTd=$('.main-table[data-page="'+iPage+'"]').find("td").eq((d.getDay()+1)+d.getHours()*8);
                oTd.addClass("color-self");
                oTd.attr("data-content",aTime[j].content);
                oTd.attr("data-timeId",aTime[j].timeId);
                oTd.attr("data-location",aTime[j].location);
                oTd.attr("data-startTime",aData[i].day+" "+aTime[j].startTime);
                oTd.attr("data-endTime",aData[i].day+" "+aTime[j].endTime);

                content= aTime[j].content;
                //日程安排太长的话 缩短内容
                if(content.length>15){
                    content=content.substr(0,15)+"...";
                }

                oTd.append(content);
            }else if(aTime[j].type==2){
                d=newDate(aData[i].day+" "+aTime[j].startTime);
                oTd=$('.main-table[data-page="'+iPage+'"]').find("td").eq((d.getDay()+1)+d.getHours()*8);
                oTd.addClass("color-work");
                oTd.append(aTime[j].helpReason);
            }else if(aTime[j].type==3){
                d=newDate(aData[i].day+" "+aTime[j].startTime);
                oTd=$('.main-table[data-page="'+iPage+'"]').find("td").eq((d.getDay()+1)+d.getHours()*8);
                oTd.addClass("color-default");
                oTd.append(aTime[j].helpReason);
            }
        }
    }

    $('table[data-page="'+iPage+'"]');

}


function updateModal(e){

    var sContent="",sLocattion="",sStartTime="",sEndTime="",sTimeId="";
    $("#btn-delete").addClass("gone");

    if($(".color-select").size()>0){
        sStartTime=$(".color-select").parent().attr("data-time");
        //var d=new Date(sStartTime+":00");
        var date = newDate(sStartTime);
        date.setHours(date.getHours()+1);
        sEndTime= date.Format("yyyy-MM-dd hh:00");
    }

    if(!!e){
        sTimeId= e.attr("data-timeId");
        sContent= e.attr("data-content");
        sLocattion= e.attr("data-location");
        sStartTime= e.attr("data-starttime");
        sEndTime= e.attr("data-endtime");
        $("#btn-delete").removeClass("gone");
    }

    $("#timeId").val(sTimeId);
    $("#content").val(sContent);
    $("#location").val(sLocattion);
    $("#starttime").val(sStartTime);
    $("#endtime").val(sEndTime);

}


//更新主表数据  刷新整张表
function refreshMainTable(iPage){
    //隐藏模态框
    $('#myModal').modal('hide');
    var oTable=$(".main-table.active");
    if(!iPage){
        iPage=oTable.attr("data-page");
    }

    var aDate=getDayOfOneWeek(iPage);
    var time=aDate[0].Format("yyyy-MM-dd");

    oTable.replaceWith(getOneWeekMainTable(aDate,iPage));
    getOrderListByMonth(time,iPage);

}


/**
 * 其他逻辑处理
 */