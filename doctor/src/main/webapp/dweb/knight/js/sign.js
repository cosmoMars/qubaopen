/**
 * Created by Knight on 2014/11/26.
 */


/**
 * 事件
 * */


$(document).ready(function () {


    $("#btn-signin").click(function(){
        //clinicSignIn();
        signin();

    });

    $("#btn-info").click(function(){
        //$("#sign-form2").submit();
        getInfo();
    });


    $("#btn-captcha").click(function(){
        getCaptcha();
    });

    //医师注册
    $("#btn-signup-doctor").click(function(){
        signupDoctor();
    });

    //诊所注册
    $("#btn-signup-clinic").click(function(){
        signupClinic();
    });


    //focus
    $("#phone,#password,#oldPwd,#newPwd,#newPwd2").on("focus",function(){
        $("#help").empty();
    });
});


/**
 * 方法
 */
/*登录*/
function signin(){
    var jsonSent={};
    /*13621673989*/
    jsonSent.password=$("#password").val();
    var name=$("#phone").val();

    if(isPhone(name)){
        jsonSent.phone=name;
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
    }else if(isEmail(name)){
        jsonSent.email  =name;
        console.log(jsonSent);

        $.ajax({
            url: ContextUrl+"/uHospital/login",
            type: "POST",
            dataType: "json",
            data: jsonSent,
            success: function (data, textStatus, jqXHR) {

                var result = data.success;

                console.log(data);

                if (result == 1) {
                    //setCookie("cookie1",JSON.stringify(data),new Date() );
                    setCookie("email",data.email);
                    if(data.loginStatus==3){
                        setCookie("email",data.email);
                        self.location = "clinic/menu.html";
                    }else{
                        self.location = "clinic/profile.html";
                    }
                    return;
                }
                if (result == 0) {
                    var msg = data.message;
                    $("#help").html(returnMsgText(msg));
                }
            }
        });
    }else{
        alert("请填写正确的手机号码或邮箱");
    }


}



/*获取手机验证码*/
function getCaptcha(){
    var phone = $("#phone").val();
    var regPartton=/1[3-8]+\d{9}/;
    if(!phone || !regPartton.test(phone)){
        //alert("手机号码格式不正确！");
        $("#myModal").modal("show");
        return false;
    }

    var params="?phone="+phone;



    $.ajax({
        url: ContextUrl+"/uDoctor/sendCaptcha"+params,
        type: "GET",
        async:false,
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {
                //setCookie("cookie1",JSON.stringify(data),new Date() );
                //self.location = location;
            }
            if (result == 0) {
                var msg = data.message;
                $("#help").html(returnMsgText(msg));
            }
        }
    });
}


/*医师注册*/
function signupDoctor(){
    var jsonSent={};
    /*13621673989*/
    jsonSent.phone=$("#phone").val();
    jsonSent.password=$("#password").val();
    jsonSent.captcha=$("#captcha").val();
    $.ajax({
        url: ContextUrl+"/uDoctor/register",
        type: "POST",
        dataType: "json",
        data: jsonSent,
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {
                setCookie("doctor",JSON.stringify(jsonSent),new Date() );
                self.location = "profile.html";
            }
            if (result == 0) {
                var msg = data.message;
                $("#help").html(returnMsgText(msg));
            }
        }
    });
}



/*诊所注册*/
function signupClinic(){
    var jsonSent={};
    /*13621673989*/
    jsonSent.email=$("#email").val();
    jsonSent.password=$("#password2").val();
    console.log(jsonSent);
    $.ajax({
        url: ContextUrl+"/uHospital/register",
        type: "POST",
        dataType: "json",
        data: jsonSent,
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {
                //setCookie("cookie1",JSON.stringify(data),new Date() );
                self.location = "clinic/profile.html";
            }
            if (result == 0) {
                var msg = data.message;
                $("#help").html(returnMsgText(msg));
            }
        }
    });
}


//修改密码 0咨询师  1诊所
function modifyPwd(type){
    var oldPwd=$("#oldPwd").val();
    var newPwd=$("#newPwd").val();
    var newPwd2=$("#newPwd2").val();

    if(newPwd!=newPwd2){
        $("#help").html("新密码两次输入不一致!");
        return;
    }

    var jsonSent={};
    jsonSent.oldPwd=oldPwd;
    jsonSent.newPwd=newPwd;
    if(type==0){
        //咨询师
        $.ajax({
            url: ContextUrl+"/uDoctor/modifyPassword",
            type: "POST",
            dataType: "json",
            data: jsonSent,
            success: function (data, textStatus, jqXHR) {

                var result = data.success;

                if (result == 1) {
                    alert("密码修改成功");
                    self.location = "profile.html";
                }
                if (result == 0) {
                    var msg = data.message;
                    $("#help").html(returnMsgText(msg));
                }
            }
        });

    }else{
        //诊所
        $.ajax({
            url: ContextUrl+"/uHospital/modifyPassword",
            type: "POST",
            dataType: "json",
            data: jsonSent,
            success: function (data, textStatus, jqXHR) {

                var result = data.success;

                if (result == 1) {
                    alert("密码修改成功");
                    self.location = "profile.html";
                }
                if (result == 0) {
                    var msg = data.message;
                    $("#help").html(returnMsgText(msg));
                }
            }
        });
    }
}
