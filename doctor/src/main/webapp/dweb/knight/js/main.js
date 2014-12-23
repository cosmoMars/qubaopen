/**
 * Created by Knight on 2014/11/26.
 */


ContextUrl="http://10.0.0.3:8080";
//ContextUrl="http://115.28.176.74:8080/doctor";
//ContextUrl="http://10.0.0.88:8080/know-heart";
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


/**/
function backToSignIn(){
    self.location = "signin.html";
}