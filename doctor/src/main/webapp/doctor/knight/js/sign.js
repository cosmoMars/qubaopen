/**
 * Created by Knight on 2014/11/26.
 */


/**
 * 事件
 * */


$(document).ready(function () {



    $.ajaxSetup({
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


    $("#btn-signin").click(function(){

        //signin();
        $("#sign-form").submit();
        //
        //
        //var phone = $("#phone").val();
        //var password = $("#password").val();
        //var form =
        //    "<form action='http://10.0.0.88:8080/know-heart/users/login' method='post'>" +
        //    "<input type='hidden' name='phone' value=''/> " +
        //    "<input type='hidden' name='password' value=''/> " +
        //    "</form> ";
        //$("#SMAL").remove();//清空节点
        //$("body").append("<iframe id='SMAL' name='SMAL' style='display: none'></iframe>");//载入iframe
        //$( "#SMAL" ).contents().find('body').html(form);//将form表单塞入iframe;
        //$( "#SMAL" ).contents().find("form input[name='phone']").val(phone);
        //$( "#SMAL" ).contents().find("form input[name='password']").val(password);
        //$( "#SMAL" ).contents().find('form').submit();//提交[ps,这里是提交到demo.com,跨域]
        //

            //setTimeout(function(){//如果需要可以跳转到demo.com
            //    window.location.href="http://www.demo.com/index.php";
            //},100)
    });


    $("#btn-info").click(function(){

        $("#sign-form2").submit();
        //getInfo();
    });

});


/**
 * 方法
 */
/*登录*/
function signin(){
    var jsonSent={};
    jsonSent.phone="13611845993";
    jsonSent.password="11111111";
    $.ajax({
        url: ContextUrl+"/users/login",
        type: "POST",
        async:false,
        dataType: "json",
        data: jsonSent
    });
}


function getInfo(){



    $.ajax({
        url: ContextUrl+"/userInfos/getIndexInfo",
        type: "GET",
        async:false,
        dataType: "json"
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

/*注册*/