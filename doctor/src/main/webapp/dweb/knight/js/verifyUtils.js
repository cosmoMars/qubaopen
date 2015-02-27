/**
 * Created by Knight on 2015/2/2.
 */
/**
 * 各种验证工具方法 */


function isEmail(email){
    var re= /\w@\w*\.\w/;

    return re.test(email);
}


function isPhone(phone){
    var re= /^(1[0-9]{10})$/;

    return re.test(phone);
}