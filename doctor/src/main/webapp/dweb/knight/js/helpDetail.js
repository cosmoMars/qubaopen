/**
 * Created by Knight on 2014/12/3.
 */


/**
 * 事件
 * */
$(document).ready(function () {

    //获取详细信息 没有则返回列表
    help=getParam("helpId");
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

/* 添加评论 */
function commentHelp(help){
    var jsonSent={};
    var content=$("#txt-reply").val();
    if(content==""){
        alert("评论不能为空");
        return;
    }
    jsonSent.helpId=help;
    jsonSent.content=content;

    $.ajax({
        url: ContextUrl+"/helpCommnet/commentHelp",
        type: "POST",
        data: jsonSent,
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {
                //setCookie("cookie1",JSON.stringify(data),new Date() );
                self.location = location;
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
    addReplyView();
}


/* 添加回复框*/
function addReplyView(){
    var oMainView=$("#main-view");
    var appendHTML='<div class="panel panel-default "><div class="panel-body"><textarea id="txt-reply" class="form-control" rows="4" style="max-width: 100%;"></textarea>' +
        '<button type="button" style="margin-top: 10px;" id="btn-commit" class="btn btn-zhixin btn-block">提交</button></div></div>';
    oMainView.append(appendHTML);
    autoResizeReplyView();
}

/**
 * 其他逻辑处理
 */
/*文本框自动撑高*/
function autoResizeReplyView(){
    (function($){
        $.fn.autoTextarea = function(options) {
            var defaults={
                maxHeight:null,//文本框是否自动撑高，默认：null，不自动撑高；如果自动撑高必须输入数值，该值作为文本框自动撑高的最大高度
                minHeight:$(this).height() //默认最小高度，也就是文本框最初的高度，当内容高度小于这个高度的时候，文本以这个高度显示
            };
            var opts = $.extend({},defaults,options);
            return $(this).each(function() {
                $(this).bind("paste cut keydown keyup focus blur",function(){
                    var height,style=this.style;
                    this.style.height =  opts.minHeight + 'px';
                    if (this.scrollHeight > opts.minHeight) {
                        if (opts.maxHeight && this.scrollHeight > opts.maxHeight) {
                            height = opts.maxHeight;
                            style.overflowY = 'scroll';
                        } else {
                            height = this.scrollHeight;
                            style.overflowY = 'hidden';
                        }
                        style.height = height  + 'px';
                    }
                });
            });
        };
    })(jQuery);
    $("#txt-reply").autoTextarea({
        maxHeight:220//文本框是否自动撑高，默认：null，不自动撑高；如果自动撑高必须输入数值，该值作为文本框自动撑高的最大高度
    });

    $("#btn-commit").on("click",function(){
        commentHelp(help);
    });
}


