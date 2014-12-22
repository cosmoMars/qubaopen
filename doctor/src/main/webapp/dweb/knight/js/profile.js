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
        url: ContextUrl+"/doctorInfo/retrieveDoctorInfo",
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
        jsonSent.name=$("#realname").val();
        jsonSent.contactPhone=$("#contactPhone").val();
        jsonSent.sex=$("#sex").val();
        jsonSent.birthday=$("#birthday").val();
        jsonSent.address=$("#address").val();
        jsonSent.experience=$("#quality").val();
        jsonSent.field=$("#goodArea").val();
        jsonSent.qq=$("#qq").val();
        jsonSent.targetUser=$("#goodObject").val();
        jsonSent.genre=$("#cureMethod").val();
        //jsonSent.time=$("#freeTime").val();
        jsonSent.quick=$("#urgentConsult").val();
        jsonSent.commentConsult=$("#commentConsult").val();
        jsonSent.phoneConsult=$("#phoneConsult").val();
        jsonSent.introduce=$("#introduction").val();
        //jsonSent.record=$("#certificate").val();
        //jsonSent.avatar=$("#avatar").val();


        console.log(jsonSent);

        $(this).ajaxSubmit({
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
//获取个人信息
function updateProfileView(obj){
    var data={
        "success" : "1",
        "doctorId" : 1,
        "name" : "",
        "infoName" : "测试",
        "contactPhone" : "",
        "sex" : 0,
        "birthday" : "1988-12-21",
        "experience" : "没有经历",
        "field" : "田野",
        "qq" : "12345",
        "faceToFace" : false,
        "video" : false,
        "targetUser" : "所有人",
        "genre" : "不知道",
        "time" : "2014-11-24 12:21:00",
        "introduce" : "么有介绍",
        "quick" : true,
        "email" : "",
        "address" : "",
        "IdCard" : "",
        "recordPath" : "/record/1_20141124-223402.png",
        "avatarPath" : "/doctor/1_20141124-223402.png"
    };


    data=obj;
    $("#realname").val(data.infoName);
    $("#contactPhone").val(data.contactPhone);
    //jsonSent.sex=$("#sex").val();
    $("#birthday").val(data.birthday);
    $("#address").val(data.address);
    $("#quality").val(data.experience);
    $("#goodArea").val(data.field);
    $("#qq").val(data.qq);
    //jsonSent.consultType=$("#consultType").val();
    $("#goodObject").val(data.targetUser);
    $("#cureMethod").val(data.genre);
    //jsonSent.time=$("#freeTime").val();
    $("#urgentConsult").val(data.quick);
    $("#commentConsult").val(data.commentConsult);
    $("#phoneConsult").val(data.phoneConsult);
    $("#introduction").val(data.introduce);
    //jsonSent.record=$("#certificate").val();
    //jsonSent.avatar=$("#avatar").val();

}

/**
 * 其他逻辑处理
 */