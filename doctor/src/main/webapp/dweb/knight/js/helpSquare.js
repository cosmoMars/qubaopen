/**
 * Created by Knight on 2014/12/3.
 */


/**
 * 事件
 * */
$(document).ready(function () {

    $(document).on("click",".child-view",function(){
        var helpId=$(this).attr("help-id");
        self.location="helpDetail.html?helpId="+helpId;
    });
});


/**
 * 请求
 */
/*获取求助列表*/
function getHelpListRequest(){
    var jsonSent={};
    jsonSent.userId=6;
    $.ajax({
        url: ContextUrl+"/help/retrieveHelpComment",
        type: "POST",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            var result = data.success;
            console.log(data);

            if (result == 1) {
                updateHelpListView(data.data);
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

function updateHelpListView(aData){

//<div class="panel panel-default ">
//    <div class="panel-body">
//    <p>
//    社交恐惧要怎么消除呢。在公共场合，特别是吃饭的地方总是会莫名地紧张，导致表情动作不自然，会不停地东张西望，就算和朋友在一起也会紧张地没话讲，然后自己心里默默觉得尴尬，想赶紧逃离现场。。。买东西的时候，跟收银员交流也会很紧张，有时候会脸红。。。总之，在公共场合，被要求作出反应或者做出某些动作的时候都会觉得很不好意思，反应迟钝，弄得大家都蛮尴尬。想改变这种现状啊。
//</p>
//    <p>已经有5人回答</p>
//    </div>
//    <div class="panel-footer"><a href="helpDetail.html">已回答</a></div>
//    </div>

    var oMainView=$("#main-view");
    var data={helpId:"",helpContent:"",commentData:[]};
    oMainView.empty();
    for(var i=0;i<aData.length;i++){
        data=aData[i];

        var answerHtml=data.commentData.length>=1? '已经有'+data.commentData.length+'人回答':'没有人回答' ;

        var appendHtml='<div class="panel panel-default child-view" help-id="'+data.helpId+'">' +
            '<div class="panel-body helpContent"><p>'+data.helpContent+'</p>' +
            '<p class="answerNumber">'+answerHtml+'</p>' +
            '</div></div>';
        oMainView.append(appendHtml);
    }


}

/**
 * 其他逻辑处理
*/