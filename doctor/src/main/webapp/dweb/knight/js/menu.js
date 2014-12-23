/**
 * Created by Knight on 2014/12/5.
 */


/**
 * Created by Knight on 2014/12/4.
 */


/**
 * 事件
 * */
$(document).ready(function () {

    $(window).resize(function() {
        resizeMenu();
        resizeNavBar();
    });

    resizeMenu();
    resizeNavBar();
    $(".menu-parent").removeClass("invisible");



    $(".menu-item").on("click",function(){
        switch ($(this).attr("id")){
            case "help":
                self.location="helpSquare.html";
                break;
            case "order":
                self.location="orderList.html";
                break;
            case "schedule":
                self.location="knightScheduler.html";
                break;
            case "cash":
                self.location="cashAccount.html";
                break;
            case "profile":
                self.location="profile.html";
                break;
            default :
                break;
        }
    });

});



/**
 * 请求
 */

/*登出*/
function logout(){
    $.ajax({
        url: ContextUrl+"/uDoctor/logout",
        type: "GET",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            var result = data.success;
            if (result == 1) {
                //setCookie("cookie1","");
                alert("退出成功");
                self.location = "signin.html";
            }else if (result == 0) {
                if(data.message=="err000"){
                    backToSignIn();
                }
            }

        },
        error: function(xhr,status,error){
            if(xhr.status=="500"){
                self.location = "signin.html";
            }
        }
    });

}




/**
 * 更新页面
 * */

/**
 * 其他逻辑处理
 */

/*动态更新 菜单界面*/
function resizeMenu(){
    var windowWidth=$(window).width();
    var width = windowWidth/2-35;
    //console.log(width);

    //每个菜单的大小
    $(".menu-item").width(width);
    $(".menu-bottom").width(width*2+2);
    $(".menu-item").height(width);

    //整个菜单的 位置
    var mp=$(".menu-parent").outerWidth();
    if(windowWidth>970){
        var iMarginLeft=(mp-400)/2;
        $(".menu-parent").css("padding-left",iMarginLeft);
    }else if(windowWidth<700){
        var iMarginLeft=(mp- $(".menu-item").width()*2)/2;
        $(".menu-parent").css("padding-left",iMarginLeft);
    }else{
        var iMarginLeft=(mp-200*3)/2;
        $(".menu-parent").css("padding-left",iMarginLeft);
    }

    //每项菜单的文字 格式
    if(width<=200){
        $(".menu-item p").css("margin-top",width/2-10);
    }else{
        $(".menu-item p").css("margin-top",200/2-10);
    }
}


/*动态更新 导航栏标题的居中*/
function resizeNavBar(){

    //根据标题文字的长度和 窗口的宽度来改变 标题位置居中
    var oBar=$(".navbar-brand");
    var iMarginLeft=$(window).width()/2-parseInt(oBar.css("font-size"))*oBar.html().length/2-15;

    oBar.css("margin-left",iMarginLeft);

}