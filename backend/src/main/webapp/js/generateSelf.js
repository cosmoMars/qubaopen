/**
 * Created by Knight on 2015/5/28.
 */


/*转换 选项的得分 顺序 例如12345 变为54321*/
$(document).on("click",".transferPointOrder",function(e){

    var o=$(this).closest("tbody").find("tr.choiceData").find(".choicePoint");

    for(var i=0;i < Math.floor(o.length/2);i++) {
        o.eq(i).val(parseInt(o.eq(i).val())+ parseInt(o.eq(o.length-i-1).val()));
        o.eq(o.length-i-1).val(parseInt(o.eq(i).val()) - parseInt(o.eq(o.length-i-1).val()));
        o.eq(i).val(parseInt(o.eq(i).val())-parseInt(o.eq(o.length-i-1).val()));
    }

});
