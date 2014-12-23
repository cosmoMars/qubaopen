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
        url: ContextUrl+"/booking/retrieveSelfFreeTime?time="+time,
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


/**
 * 更新页面
 * */
function updateTableData(aData,iPage){

    var aTime;
    for(var i=0;i<aData.length;i++){
        aTime=aData[i].timeData;
        for(var j=0;j<aTime.length;j++){
            var d,oTd;
            if(aTime[j].type==1){
                d=new Date(aTime[j].startTime);
                //console.log("1 "+aData[i].day+" "+aTime[j].startTime);
                //console.log("1 "+aData[i].day+" "+aTime[j].endTime);
                //var d=new Date("2014-12-19 05:00:00");
                //$('table[data-page="'+iPage+'"]').find("tr").eq(d.getHours()).find("td").eq(d.getDay()+1).addClass("color-work");
                oTd=$('.main-table[data-page="'+iPage+'"]').find("td").eq((d.getDay()+1)+d.getHours()*8);
                oTd.addClass("color-work");
                oTd.append(aTime[j].content);
            }else if(aTime[j].type==2){
                d=new Date(aTime[j].startTime);
                //console.log(aData[i].day+" "+aTime[j].startTime);
                //console.log(aData[i].day+" "+aTime[j].endTime);
                oTd=$('.main-table[data-page="'+iPage+'"]').find("td").eq((d.getDay()+1)+d.getHours()*8);
                oTd.addClass("color-work");
                oTd.append(aTime[j].helpReason);
            }
        }
    }

    $('table[data-page="'+iPage+'"]');

}



/**
 * 其他逻辑处理
 */