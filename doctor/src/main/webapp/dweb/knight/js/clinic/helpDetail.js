/**
 * Created by Knight on 2014/12/3.
 */


/**
 * 事件
 * */

var more=false;
var page=0;


$(document).ready(function () {

    //获取详细信息 没有则返回列表
    help=getParam("helpId");
    if(!help){
        self.location="helpSquare.html"
    }else{
        getHelpDetail(help,page);
    }

    $(document).on("scroll",function(){
        if(getScrollTop() + getClientHeight() == getScrollHeight() && more){
            console.log("到达底部");
            page++;
            getHelpDetail(help,page);
        }
    });
});

/**
 * 请求
 */
/*获取订单详情*/
function getHelpDetail(help,page){
    var jsonSent={};
    jsonSent.helpId=help;
    jsonSent.page=page;

    $.ajax({
        url: ContextUrl+"/hospitalHelp/retrieveDetailHelp",
        type: "POST",
        data: jsonSent,
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {
                //setCookie("cookie1",JSON.stringify(data),new Date() );
                //self.location = location;
                more=data.more;
                updateHelpDetailView(data,page);
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
        url: ContextUrl+"/hospitalComment/addComment",
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

function updateHelpDetailView(data,page){


    var oMainView=$("#main-view");

    //加载更多
    if(page>0){
        var oLastView=oMainView.find(".panel").last();
        var aData=data.data;
        for(var i=0;i<aData.length;i++){
           oLastView.before(replyView(aData[i]));
        }

        return;
    }

    //var appendHTML='<div class="panel panel-default "><div class="panel-body"><p>'+data.helpContent+'</p></div></div>';
    var appendHTML='<div class="panel panel-warning "><div class="panel-body"><p><img class="img-circle" src="'+data.userAvatar+'" style="width: 40px; height: 40px;"  onerror="defaultImg(this)";>'+
        data.userName+'</p><p>'+data.helpContent+'</p>' +
        '<p class="label-time">发表于 '+data.helpTime+'</p></div></div>';


    oMainView.empty().append(appendHTML);

    var aData=data.data;
    for(var i=0;i<aData.length;i++){

        oMainView.append(replyView(aData[i]));
    }
    addReplyView();
}


/* 追加评论框*/
function addReplyView(){
    var oMainView=$("#main-view");
    var appendHTML='<div class="panel panel-default "><div class="panel-body"><textarea id="txt-reply" class="form-control" rows="4" style="max-width: 100%;"></textarea>' +
        '<button type="button" style="margin-top: 10px;" id="btn-commit" class="btn btn-zhixin btn-block">提交</button></div></div>';
    oMainView.append(appendHTML);
    autoResizeReplyView();
}

/* 已有评论框*/
function replyView(aData){
    var nameHTML="";
    if(aData.type==0){
        nameHTML=aData.name + ' 心理咨询师';
    }else{
        nameHTML=aData.name + ' 心理诊所';
    }
    var appendHTML='<div class="panel panel-default "><div class="panel-body"><p><img class="img-circle" src="'+aData.avatar+'" ' +
        'alt="e" style="width: 40px; height: 40px;" onerror="defaultImg(this)";>' +
    ' '+nameHTML +
    '</p><p>'+aData.content+'</p>' +
    '<p class="label-time">回答于 '+aData.time+'<span class="pull-right"><span>'+aData.goods+' </span><span class="color-orange glyphicon glyphicon-thumbs-up"></span></span></p></div></div>';

    return appendHTML;
}

/* 头像没加载出来 用默认图片 */
function defaultImg(e){
    e.src= "data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==";
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

//获取滚动条当前的位置
function getScrollTop() {
    var scrollTop = 0;
    if (document.documentElement && document.documentElement.scrollTop) {
        scrollTop = document.documentElement.scrollTop;
    }
    else if (document.body) {
        scrollTop = document.body.scrollTop;
    }
    return scrollTop;
}

//获取当前可是范围的高度
function getClientHeight() {
    var clientHeight = 0;
    if (document.body.clientHeight && document.documentElement.clientHeight) {
        clientHeight = Math.min(document.body.clientHeight, document.documentElement.clientHeight);
    }
    else {
        clientHeight = Math.max(document.body.clientHeight, document.documentElement.clientHeight);
    }
    return clientHeight;
}

//获取文档完整的高度
function getScrollHeight() {
    return Math.max(document.body.scrollHeight, document.documentElement.scrollHeight);
}


