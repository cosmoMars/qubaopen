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
    jsonSent.page=0;

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
                updateHelpDetailView(data);
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

function updateHelpDetailView(data){

    var oMainView=$("#main-view");

    var appendHTML='<div class="panel panel-default "><div class="panel-body"><p>'+data.helpContent+'</p></div></div>';

    oMainView.empty().append(appendHTML);

    var aData=data.data;
    for(var i=0;i<aData.length;i++){
        appendHTML='<div class="panel panel-default "><div class="panel-body"><p><img class="img-circle" src="data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==" alt="Generic placeholder image" style="width: 40px; height: 40px;">' +
        ' '+aData[i].doctorName +
        ' 心理咨询师' +
        '</p><p>'+aData[i].doctorContent+'</p>' +
        '<p>回答于'+aData[i].doctorTime+'</p></div></div>';
        oMainView.append(appendHTML);
    }

}

/**
 * 其他逻辑处理
 */

