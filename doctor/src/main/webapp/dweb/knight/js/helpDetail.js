/**
 * Created by Knight on 2014/12/3.
 */


/**
 * 事件
 * */
$(document).ready(function () {

    //获取详细信息 没有则返回列表
    var help=getParam("helpId");
    if(!help){
        self.location="helpSquare.html"
    }else{
        getHelpDetail(help);
    }

});


/**
 * 请求
 */


/*获取订单详情*/
function getHelpDetail(help){
    var jsonSent={};
    jsonSent.helpId=help;
    jsonSent.page=2;

    $.ajax({
        url: ContextUrl+"/help/retrieveDetailHelp",
        type: "POST",
        data: jsonSent,
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {
                //setCookie("cookie1",JSON.stringify(data),new Date() );
                //self.location = location;
                updateHelpDetailView(data.data);
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

