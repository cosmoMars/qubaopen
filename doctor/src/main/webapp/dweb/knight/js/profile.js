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


    $("#btn-education").on("click",function(){
        $("#modal1").modal("show");
    });
    $("#btn-train").on("click",function(){
        $("#modal2").modal("show");
    });
    $("#btn-supervisor").on("click",function(){
        $("#modal3").modal("show");
    });
    $("#btn-experience").on("click",function(){
        $("#modal4").modal("show");
    });

    $("#btn-confirm1,#btn-confirm2,#btn-confirm3,#btn-confirm4").on("click",function(){

        var recordJson=getDataJson();

        if(recordJson.educationalStart!=$("#educationalStart").val() || recordJson.educationalEnd!=$("#educationalEnd").val() || recordJson.school!=$("#school").val()
        || recordJson.profession!=$("#profession").val() || recordJson.degree!=$("#degree").val() || recordJson.educationalIntroduction!=$("#educationalIntroduction").val()){
            $("#btn-education .icon4btn").removeClass("glyphicon-chevron-right").addClass("glyphicon-ok");
            $("#btn-education .txt4btn").empty().append("已修改");
        }

        if(recordJson.trainStart!=$("#trainStart").val() || recordJson.trainEnd!=$("#trainEnd").val() || recordJson.course!=$("#course").val()
            || recordJson.organization!=$("#organization").val() || recordJson.trainIntroduction!=$("#trainIntroduction").val()){
            $("#btn-train .icon4btn").removeClass("glyphicon-chevron-right").addClass("glyphicon-ok");
            $("#btn-train .txt4btn").empty().append("已修改");
        }

        if(recordJson.supervise!=$("#supervise").val() || recordJson.orientation!=$("#orientation").val() || recordJson.superviseHour!=$("#superviseHour").val()
            || recordJson.contactMethod!=$("#contactMethod").val() || recordJson.superviseIntroduction!=$("#superviseIntroduction").val()){
            $("#btn-supervisor .icon4btn").removeClass("glyphicon-chevron-right").addClass("glyphicon-ok");
            $("#btn-supervisor .txt4btn").empty().append("已修改");
        }

        if(recordJson.selfStart!=$("#selfStart").val() || recordJson.selfEnd!=$("#selfEnd").val() || recordJson.totalHour!=$("#totalHour").val()
            || recordJson.selfIntroduction!=$("#selfIntroduction").val()){
            $("#btn-experience .icon4btn").removeClass("glyphicon-chevron-right").addClass("glyphicon-ok");
            $("#btn-experience .txt4btn").empty().append("已修改");
        }
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


/*获取医师资质信息*/
function retrieveDoctorRecord(){
    $.ajax({
        url: ContextUrl+"/doctorRecord/retrieveDoctorRecord",
        type: "GET",
        async:false,
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {
                updateDoctorRecord(data);
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
        jsonSent.infoName=$("#realname").val();
        jsonSent.email=$("#email").val();
        jsonSent.contactPhone=$("#contactPhone").val();
        jsonSent.sex=$("#sex label:eq(0)").hasClass("active")?"0":"1";
        jsonSent.birthday=$("#birthday").val();
        jsonSent.address=$("#address").val();
        jsonSent.experience=$("#quality").val();
        jsonSent.field=$("#goodArea").val();
        jsonSent.qq=$("#qq").val();
        jsonSent.targetUser=$("#goodObject").val();
        jsonSent.genre=$("#cureMethod").val();
        //jsonSent.time=$("#freeTime").val();
        jsonSent.quick=$("#urgentConsult label:eq(0)").hasClass("active");
        jsonSent.commentConsult=$("#commentConsult label:eq(0)").hasClass("active");
        jsonSent.phoneConsult=$("#phoneConsult label:eq(0)").hasClass("active");
        //jsonSent.introduce=$("#introduction").val();
        jsonSent.bookingTime=$("#btn-pick").attr("data-time");
        jsonSent.video=$("#video").is(":checked");
        jsonSent.faceToFace=$("#faceToFace").is(":checked");
        if(jsonSent.video){
            jsonSent.onlineFee=$("#onlineFee").val();
        }
        if(jsonSent.faceToFace){
            jsonSent.offlineFee=$("#offlineFee").val();
        }


        recordJson.educationalStart=$("#educationalStart").val();
        recordJson.educationalEnd=$("#educationalEnd").val();
        recordJson.school=$("#school").val();
        recordJson.profession=$("#profession").val();
        recordJson.degree=$("#degree").val();
        recordJson.educationalIntroduction=$("#educationalIntroduction").val();
        recordJson.trainStart=$("#trainStart").val();
        recordJson.trainEnd=$("#trainEnd").val();
        recordJson.course=$("#course").val();
        recordJson.organization=$("#organization").val();
        recordJson.trainIntroduction=$("#trainIntroduction").val();
        recordJson.supervise=$("#supervise").val();
        recordJson.orientation=$("#orientation").val();
        recordJson.superviseHour=$("#superviseHour").val();
        recordJson.contactMethod=$("#contactMethod").val();
        recordJson.superviseIntroduction=$("#superviseIntroduction").val();
        recordJson.selfStart=$("#selfStart").val();
        recordJson.selfEnd=$("#selfEnd").val();
        recordJson.totalHour=$("#totalHour").val();
        recordJson.selfIntroduction=$("#selfIntroduction").val();
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

        jsonSent.recordJson=JsonToString(jsonFilter(recordJson));


        //jsonSent.record=$("#certificate").val();
        //jsonSent.avatar=$("#avatar").val();


        console.log(jsonFilter(jsonSent));

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
/*更新个人信息*/
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
        "IdCard" : ""
    };


    data=obj;
    $("#realname").val(data.infoName);
    $("#email").val(data.email);
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
    $("#btn-pick").attr("data-time",data.time);

    if(data.quick){
        $("#urgentConsult label").eq(0).click();
    }else{
        $("#urgentConsult label").eq(1).click();
    }


    if(data.commentConsult){
        $("#commentConsult label").eq(0).click();
    }else{
        $("#commentConsult label").eq(1).click();
    }


    if(data.phoneConsult){
        $("#phoneConsult label").eq(0).click();
    }else{
        $("#phoneConsult label").eq(1).click();
    }

    $("#video").attr("checked", data.video);
    $("#faceToFace").attr("checked", data.faceToFace);
    $("#onlineFee").val(data.onlineFee);
    $("#offlineFee").val(data.offlineFee);

    $("#introduction").val(data.introduce);
    //jsonSent.record=$("#certificate").val();
    //jsonSent.avatar=$("#avatar").val();

}

/* 更新医师资质界面 */
function updateDoctorRecord(data){


    setDataJson(data);
    /* 学历和学位 */
    if(data.educationalStart!="" || data.educationalEnd!="" || data.school!="" || data.profession!="" || data.degree!="" || data.educationalIntroduction!=""){
        $("#btn-education .txt4btn").empty().append("已填写");
    }
    $("#educationalStart").val(data.educationalStart);
    $("#educationalEnd").val(data.educationalEnd);
    $("#school").val(data.school);
    $("#profession").val(data.profession);
    $("#degree").val(data.degree);
    $("#educationalIntroduction").val(data.educationalIntroduction);

    /* 培训经历 */
    if(data.trainStart!="" || data.trainEnd!="" || data.course!="" || data.organization!="" || data.trainIntroduction!=""){
        $("#btn-train .txt4btn").empty().append("已填写");
    }
    $("#trainStart").val(data.trainStart);
    $("#trainEnd").val(data.trainEnd);
    $("#course").val(data.course);
    $("#organization").val(data.organization);
    $("#trainIntroduction").val(data.trainIntroduction);

    /* 督导经历 */
    if(data.supervise!="" || data.orientation!="" || data.superviseHour!="" || data.contactMethod!="" || data.superviseIntroduction!=""){
        $("#btn-supervisor .txt4btn").empty().append("已填写");
    }
    $("#supervise").val(data.supervise);
    $("#orientation").val( data.orientation);
    $("#superviseHour").val(data.superviseHour);
    $("#contactMethod").val(data.contactMethod);
    $("#superviseIntroduction").val(data.superviseIntroduction);

    /* 自我经历 */
    if(data.selfStart!="" || data.selfEnd!="" || data.totalHour!="" || data.selfIntroduction!=""){
        $("#btn-experience .txt4btn").empty().append("已填写");
    }
    $("#selfStart").val(data.selfStart);
    $("#selfEnd").val(data.selfEnd);
    $("#totalHour").val(data.totalHour);
    $("#selfIntroduction").val(data.selfIntroduction);
}

/**
 * 其他逻辑处理
 */
var dataJson={};

function setDataJson(data){
    dataJson=data;
}

function getDataJson(){
    return dataJson;
}

//把空的json去掉
function jsonFilter(json){

    for (var key in json){
        if(!json[key] && typeof  json[key]!="boolean"){
            delete json[key];
        }
    }
    return json;
}