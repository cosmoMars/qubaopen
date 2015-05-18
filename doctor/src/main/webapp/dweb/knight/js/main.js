/**
 * Created by Knight on 2014/11/26.
 */


ContextUrl=window.location.protocol+"//"+window.location.host;
//ContextUrl=window.location.protocol+"//"+window.location.host+"/doctor";
//ContextUrl="http://zhixin.me:8080/doctor";
//
//if(!(location.hostname).match("www.qubaopen.com.cn")){
//    location.hostname="www.qubaopen.com.cn";
//}
function getCookie(c_name)
{
    if (document.cookie.length>0)
    {
        c_start=document.cookie.indexOf(c_name + "=")
        if (c_start!=-1)
        {
            c_start=c_start + c_name.length+1
            c_end=document.cookie.indexOf(";",c_start)
            if (c_end==-1) c_end=document.cookie.length
            return decodeURI(document.cookie.substring(c_start,c_end))
        }
    }
    return ""
}

function setCookie(c_name,value,expiredays)
{
    var exdate=new Date()
    exdate.setDate(exdate.getDate()+expiredays)
    document.cookie=c_name+ "=" +encodeURI(value)+
    ((expiredays==null) ? "" : ";expires="+exdate.toGMTString())
}

function checkCookie()
{
    username=getCookie('username')
    if (username!=null && username!="")
    {alert('Welcome again '+username+'!')}
    else
    {
        username=prompt('Please enter your name:',"")
        if (username!=null && username!="")
        {
            setCookie('username',username,365)
        }
    }
}


/*获取 get方式的参数*/
function getParam(param){
    var SearchString = window.location.search.substring(1);
    var VariableArray = SearchString.split('&');
    for(var i = 0; i < VariableArray.length; i++){
        var KeyValuePair = VariableArray[i].split('=');
        if(KeyValuePair[0] == param){
            return KeyValuePair[1];
        }
    }
}


/*回到登录页*/
function backToSignIn(){
    if(location.pathname.indexOf("clinic")>-1){
        self.location="../signin.html";
        return;
    }
    self.location = "signin.html";
}

function autoSignIn(){
    var jsonSent=eval("("+getCookie("doctor")+")");

    $.ajax({
        url: ContextUrl+"/uDoctor/login",
        type: "POST",
        dataType: "json",
        data: jsonSent,
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {
                //setCookie("cookie1",JSON.stringify(data),new Date() );

                if(data.loginStatus==3){
                    setCookie("phone",data.phone);
                    self.location = "menu.html";
                }else{
                    self.location = "profile.html";
                }
            }
            if (result == 0) {
                var msg = data.message;
                $("#help").html(returnMsgText(msg));
            }
        }
    });
}

/* new date IE浏览器不支持NEW DATE()带参数的解决方法
 * str 格式 yyyy-MM-dd hh-ii-ss */
function newDate(str){
    if(str.length==10){
        str+=" 00:00:00"
    }else if(str.length==13){
        str+=":00:00"
    }else if(str.length==16){
        str+=":00"
    }

    if(str.length!=19){
        alert("date格式有误");
        console.log("date格式有误");
        return;
    }
    var date = new Date();
    date.setUTCFullYear(parseInt(str.substring(0,4)), parseInt(str.substring(5,7))-1, parseInt(str.substring(8,10)));
    //由于是美国时间 所以-8小时
    date.setUTCHours(parseInt(str.substring(11,13))-8, 0, 0, 0);
    return date;
}


function returnMsgText(message) {
    var json = {};
    json.err000="登录失败";
    json.err001="亲，没有该用户呦！";
    json.err002="亲，密码错误，可不能登录呦！";
    json.err003="亲，您的手机号没有填对呢！";
    json.err004="密码需要至少8位的字母或数字哦！";
    json.err005="亲，您的邮箱没有填对呢！";
    json.err006="亲，此号码已注册过咯！如有问题，麻烦联系我们微信客服哦！";
    json.err007="您输的验证码不对哦！";
    json.err008="您还没获取过验证码呦！";
    json.err009="亲，请休息一会再验证呦！";
    json.err010="亲，您让人家发了太多次验证码了啦";
    json.err011="亲，短信被怪兽吃掉了~麻烦您联系我们微信客服吧~";
    json.err012="亲，验证码都没填，我们还怎么愉快的玩耍呀~";
    json.err013="亲，请重新申请验证码吧~";
    json.err014="亲，您的信息有误，请检查有无遗漏哦";
    json.err015="原密码格式不正确";
    json.err016="原密码不匹配";
    json.err018="亲，用户名或密码不正确哦~";
    json.err019="亲，该用户不存在哦~";
    json.err020="亲，该用户已经注册了哦~";
    json.err021="亲，验证码平台开小差了，联系我们微信客服有奖励哦~~";

    if(json[message]){
        return json[message];
    }else{
        return message;
    }

}


