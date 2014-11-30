/**
 * Created by Knight on 2014/11/26.
 */


/**
 * 事件
 * */


$(document).ready(function () {


    $("#btn-signin").click(function(){
        signin();

    });


    $("#btn-info").click(function(){
        //$("#sign-form2").submit();
        getInfo();
    });



    $("#btn-saveInfo").click(function(){
        modifyInfo();
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
        async:false,
        dataType: "json",
        data: jsonSent,
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {
                //setCookie("cookie1",JSON.stringify(data),new Date() );
                self.location = "index.html";
            }
            if (result == 0) {
                //msg = data.message;

            }
        }
    });
}

/*获取个人信息*/
function getInfo(){
    $.ajax({
        url: ContextUrl+"/doctorInfo/retrieveDoctorInfo",
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

/*登出*/
function logout(){
    $.ajax({
        url: ContextUrl+"/user/logout.htm",
        type: "POST",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            var result = data.success;
            if (result == 1) {
                setCookie("cookie1","");
                self.location = "index.html";
            }
            if (result == 0) {
                msg = data.message;

            }
            ////console.log(result);

        }
    });

}

/*修改个人信息*/
function modifyInfo(){
    var jsonSent={};
    jsonSent.name=$("#realname").val();
    jsonSent.sex=$("#sex").val();
    jsonSent.birthday=$("#birthday").val();
    jsonSent.experience=$("#quality").val();
    jsonSent.field=$("#goodArea").val();
    jsonSent.qq=$("#qq").val();
    jsonSent.consultType=$("#consultType").val();
    jsonSent.targetUser=$("#goodObject").val();
    jsonSent.genre=$("#cureMethod").val();
    jsonSent.time=$("#freeTime").val();
    jsonSent.quick=$("#urgentConsult").val();
    jsonSent.introduce=$("#introduction").val();
    jsonSent.record=$("#certificate").val();


    console.log(jsonSent);
    return;
    //jsonSent.avatar=$("#avatar").val();
    $.ajax({
        url: ContextUrl+"/doctorInfo/modifyDoctorInfo",
        type: "POST",
        async:false,
        dataType: "json",
        data: jsonSent,
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {
                //setCookie("cookie1",JSON.stringify(data),new Date() );
                //self.location = "index.html";
            }
            if (result == 0) {
                //msg = data.message;

            }
        }
    });
}


/*获取手机验证码*/
function getCaptcha(){
    var phone=$("#phone").val();

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
        async:false,
        dataType: "json",
        data: jsonSent,
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {
                //setCookie("cookie1",JSON.stringify(data),new Date() );
                //self.location = "index.html";
            }
            if (result == 0) {
                //msg = data.message;

            }
        }
    });
}


/*诊所注册*/