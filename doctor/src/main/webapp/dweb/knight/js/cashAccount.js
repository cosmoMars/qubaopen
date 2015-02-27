/**
 * Created by Knight on 2014/12/30.
 */




/**
 * 事件
 * */


var bMoreIn=false;
var bMoreOut=false;
$(document).ready(function () {



    $("#btn-bank").on("click",function(){
        self.location="cashBank.html";
    });

    $("#btn-alipay").on("click",function(){
        self.location="cashAlipay.html";
    });


    var iPageIn=0;
    var iPageOut=0;

    $("#btn-more-in").on("click",function(){
        if(bMoreIn){
            iPageIn++;
            retrieveCashInfo(iPageIn,0);
        }
    });

    $("#btn-more-out").on("click",function(){
        if(bMoreOut){
            iPageOut++;
            retrieveCashInfo(iPageOut,1);
        }

    });



});


/**
 * 请求
 */

/*获取账户信息*/
function retrieveCashInfo(page,type){

    var params="?page="+page+"&typeIdx="+type;
    console.log(params);
    $.ajax({
        url: ContextUrl+"/doctorCash/retrieveCashInfo"+params,
        type: "GET",
        async:false,
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);
            if (result == 1) {
                if(page==0 && type==0){
                    updateMainView(data);
                    bMoreIn=data.more;
                    if(!bMoreIn){
                        $("#btn-more-in").addClass("disabled");
                    }
                }else if(type ==0){
                    updateInCashList(data.inDetail);
                    bMoreIn=data.more;
                    if(!bMoreIn){
                        $("#btn-more-in").addClass("disabled");
                    }
                }else if(type ==1){
                    updateOutCashList(data.outDetail);
                    bMoreOut=data.more;
                    if(!bMoreOut){
                        $("#btn-more-out").addClass("disabled");
                    }
                }
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
function updateMainView(data){

    var currentCash=!!data.currentCash?data.currentCash:0;
    var inCash=!!data.inCash?data.inCash:0;
    var outCash=!!data.outCash?data.outCash:0;
    $("#currentCash").empty().append(currentCash);
    $("#inCash").empty().append(inCash);
    $("#outCash").empty().append(outCash);
    updateInCashList(data.inDetail);
}

function updateInCashList(aData){

    var oMainView=$("#inCashList");
    var data={cash:"",time:"",userName:""};
    //oMainView.empty();
    for(var i=0;i<aData.length;i++){
        data=aData[i];
        var appendHtml='<tr>' +
            '<td>'+data.time+'</td>' +
            '<td>完成预约'+data.userName+'</td>' +
            '<td>'+data.cash+'</td>' +
            '</tr>';
        oMainView.append(appendHtml);
    }

}


function updateOutCashList(aData){

    var oMainView=$("#outCashList");
    var data={cash:"",time:"",userName:""};
    //oMainView.empty();
    for(var i=0;i<aData.length;i++){
        data=aData[i];
        var appendHtml='<tr>' +
            '<td>'+data.time+'</td>' +
            '<td>完成预约</td>' +
            '<td>'+data.cash+'</td>' +
            '</tr>';
        oMainView.append(appendHtml);
    }

}
/**
 * 其他逻辑处理
 */