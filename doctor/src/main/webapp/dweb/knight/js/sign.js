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

    });
});


/**
 * 方法
 */
/*登录*/
function signin(){
    var jsonSent={};
    /*13621673989*/
    jsonSent.phone=$("#phone").val();
    jsonSent.password=$("#password").val();
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
                //msg = data.message;

            }
        }
    });
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

    var parmas="?phone="+phone;
    $.ajax({
        url: ContextUrl+"/uDoctor/sendCaptcha"+parmas,
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
                //msg = data.message;

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
                //setCookie("cookie1",JSON.stringify(data),new Date() );
                self.location = "profile.html";
            }
            if (result == 0) {
                //msg = data.message;

            }
        }
    });
}


/*诊所登陆*/
function clinicSignIn(){
    self.location="clinic/menu.html"
}

/*诊所注册*/