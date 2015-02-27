/**
 * Created by Knight on 2014/12/1.
 */


/**
 * 事件
 * */
$(document).ready(function () {

    $("#btn-saveInfo").click(function(){
        modifyInfo();
    });


});


/**
 * 请求
 */

/*获取个人信息*/
function getInfo(){
    $.ajax({
        url: ContextUrl+"/hospitalInfo/retrieveHospitalInfo",
        type: "GET",
        async:false,
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {

                updateProfileView(data);
            }else if (result == 0) {
                if(data.message=="err000"){
                    backToSignIn();
                }

            }
        }
    });
}






/*修改个人信息*/
function modifyInfo(){

    $("#uploadPhotoForm").ajaxForm();

    $('#uploadPhotoForm').submit(function () {

        var jsonSent={};
        var recordJson={};
        jsonSent.name=$("#clinicName").val();
        jsonSent.phone=$("#contactPhone").val();
        jsonSent.urgentPhone=$("#urgentPhone").val();
        jsonSent.address=$("#address").val();
        jsonSent.qq=$("#qq").val();


        jsonSent.introduce=$("#introduction").val();
        jsonSent.minCharge=$("#minCharge").val();
        jsonSent.maxCharge=$("#maxCharge").val();

        //jsonSent.time=$("#freeTime").val();
        jsonSent.quick=$("#urgentConsult label:eq(0)").hasClass("active");
        jsonSent.wordsConsult=$("#wordsConsult label:eq(0)").hasClass("active");
        //jsonSent.phoneConsult=$("#phoneConsult label:eq(0)").hasClass("active");
        jsonSent.timeExp=$("#btn-pick").attr("data-time");

        function JsonToString(o) {
            var arr = [];
            var fmt = function(s) {
                if (typeof s == 'object' && s != null) return JsonToStr(s);
                return /^(string|number)$/.test(typeof s) ? '"' + s + '"' : s;
            }
            for (var i in o)
                arr.push('"' + i + '":' + fmt(o[i]));
            return "{" + arr.join(",") + "}";
        }

        jsonSent.recordJson=JsonToString(recordJson);


        //jsonSent.record=$("#certificate").val();
        //jsonSent.avatar=$("#avatar").val();


        console.log(jsonSent);


        $(this).ajaxSubmit({
            url: ContextUrl+"/hospitalInfo/modifyHosptialInfo",
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

                }else if (result == 0) {
                    if(data.message=="err000"){
                        backToSignIn();
                    }
                }
            }
        });

    });
}





/**
 * 更新页面
 * */
/*更新个人信息*/
function updateProfileView(obj){
    var data={};


    data=obj;
    $("#clinicName").val(data.name);
    $("#email").val(data.email);
    $("#contactPhone").val(data.phone);
    $("#urgentPhone").val(data.urgentPhone);
    $("#address").val(data.address);

    $("#qq").val(data.qq);


    $("#minCharge").val(data.minCharge);
    $("#maxCharge").val(data.maxCharge);
    $("#urgentConsult").val(data.quick);
    $("#btn-pick").attr("data-time",data.bookingTime);



    if(data.wordsConsult){
        $("#wordsConsult label").eq(0).click();
    }else{
        $("#wordsConsult label").eq(1).click();
    }

    $("#introduction").val(data.introduce);

}

/**
 * 其他逻辑处理
 */