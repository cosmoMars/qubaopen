/**
 * Created by Knight on 2014/11/26.
 */


ContextUrl=window.location.protocol+"//"+window.location.host+"/doctor";
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