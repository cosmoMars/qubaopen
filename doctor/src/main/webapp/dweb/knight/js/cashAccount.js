/**
 * Created by Knight on 2014/12/30.
 */




/**
 * 事件
 * */
$(document).ready(function () {

    $("#btn-bank").on("click",function(){
        self.location="cashBank.html";
    });

    $("#btn-alipay").on("click",function(){
        self.location="cashAlipay.html";
    });
});


/**
 * 请求
 */

/*获取账户信息*/
function retrieveCashInfo(page,type){

    var params="?page="+page+"&typeIndex="+type;
    $.ajax({
        url: ContextUrl+"/doctorCash/retrieveCashInfo"+params,
        type: "GET",
        async:false,
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);
            if (result == 1) {
                updateView(data);
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
function updateView(data){

    var currentCash=!!data.currentCash?data.currentCash:0;
    var inCash=!!data.inCash?data.inCash:0;
    var outCash=!!data.outCash?data.outCash:0;
    $("#currentCash").empty().append(currentCash);
    $("#inCash").empty().append(inCash);
    $("#outCash").empty().append(outCash);

}


/**
 * 其他逻辑处理
 */