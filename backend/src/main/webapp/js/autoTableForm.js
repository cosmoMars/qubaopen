/**
 * Created by Knight on 2015/3/20.
 */


var tableJson,formJson,optionsJson;
/**
 * 初始化
 * @param data1
 * @param data2
 */
function initAutoTF(data1,data2){
    createTable(data1);
    createForm(data2);

}

/**
 * 创建tableView
 * @param data
 */
function createTable(data){


    /*获取主view*/
    var oMainView=$("#mainView");

    /*顶部操作按钮*/
    var topBtnHTML='<div class="row"><div class="col-sm-3">' +
        '<button type="button" id="btn-add" class="btn btn-primary">新增</button></div>' +
        '<div class="col-sm-9">' +
        '<button type="button" id="btn-refresh" class="btn btn-primary">刷新列表</button>' +
        '<label><input type="checkbox" id="checkboxWarning" value="option1"></label></div></div>';


    /*主tableView*/
    var tableHTML='<div class="tableParent"><table class="table table-striped"><thead><tr>';

    for(var i=0;i<data.length;i++){
        tableHTML+='<th width="'+data[i].tSize+'%">'+data[i].tName+'</th>';


    }

    tableHTML+='</tr></thead><tbody id="tableView"></tbody></table></div>';


    /*分页按钮*/
    var pageHTML='<nav><ul class="pager">' +
        '<li id="prev" class="disabled"><a href="#">上一页</a></li>' +
        ' <li id="next" class="disabled"><a href="#">下一页</a></li></ul></nav>';


    /*加载*/
    oMainView.append(topBtnHTML).append(tableHTML).append(pageHTML);

}


/**
 * 创建form
 * @param data
 */
function createForm(data){}


/**
 * 更新 主表view
 * @param data
 */
function updateTableList(data){

    var oTableView=$("#tableView");

    oTableView.empty();

    var appendHTML;
    for(var i=0;i<data.length;i++){
        appendHTML="";
        appendHTML+= '<tr>';
        //使用反射 只能获取参数名
        var tData= tableJson.data;
        for(var j=0;j<tData.length;j++){
            if(tData[j].tClass=="action"){
                //如果是操作类
                appendHTML+= '<td><span class="check">查看</span> <span class="delete">删除</span></td></tr>';
                continue;
            }
            appendHTML+='<td class="'+tData[j].tClass+'">'+data[i][tData[j].tClass]+'</td>';
        }



        oTableView.append(appendHTML);
    }


}

/*调用主table列表接口*/
function getTableList(page,url){
    var p="";
    if(page){
        p=page;
    }
    $.ajax({
        url:url+p,
        type: "GET",
        async:true,
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            var result = data.success;

            console.log(data);

            if (result == 1) {
                updateTableList(data[optionsJson.listName]);
                if(data[optionsJson.listName].length>=10){
                    $("#next").removeClass("disabled");
                }else{
                    $("#next").addClass("disabled");
                }
                if(page>0){
                    $("#prev").removeClass("disabled");
                }else{
                    $("#prev").addClass("disabled");
                }
            }else if (result == 0) {

                alert("请求错误");
            }
        }
    });
}