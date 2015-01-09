package com.qubaopen.survey.service.creator;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.qubaopen.survey.entity.booking.Booking;
import com.qubaopen.survey.tenpay.AccessTokenRequestHandler;
import com.qubaopen.survey.tenpay.ClientRequestHandler;
import com.qubaopen.survey.tenpay.PackageRequestHandler;
import com.qubaopen.survey.tenpay.PrepayIdRequestHandler;
import com.qubaopen.survey.tenpay.util.ConstantUtil;
import com.qubaopen.survey.tenpay.util.WXUtil;

@Service
public class WeixinContentCreator {
	
	private Logger logger = LoggerFactory.getLogger(WeixinContentCreator.class);

	public Object buildContent(Booking booking) throws UnsupportedEncodingException {

		Map<String, Object> result = new HashMap<>();
		
		String notify_url = "notify_url";
		PackageRequestHandler packageReqHandler = new PackageRequestHandler();// 生成package的请求类
		PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler();// 获取prepayid的请求类
		ClientRequestHandler clientHandler = new ClientRequestHandler();// 返回客户端支付参数的请求类
		packageReqHandler.setKey(ConstantUtil.PARTNER_KEY);

		int retcode;
		String retmsg;
		// 获取token值
		String token = AccessTokenRequestHandler.getAccessToken();
		
		if (!"".equals(token)) {
            //设置package订单参数
            packageReqHandler.setParameter("bank_type", "WX");//银行渠道
            packageReqHandler.setParameter("body", "知心心理咨询"); //商品描述
            packageReqHandler.setParameter("notify_url", notify_url); //接收财付通通知的URL
            packageReqHandler.setParameter("partner", ConstantUtil.PARTNER); //商户号
            packageReqHandler.setParameter("out_trade_no", booking.getTradeNo()); //商家订单号
            packageReqHandler.setParameter("total_fee", String.valueOf(booking.getMoney() * 100)); //商品金额,以分为单位
            packageReqHandler.setParameter("spbill_create_ip", "app-srv"); //订单生成的机器IP，指用户浏览器端IP
            packageReqHandler.setParameter("fee_type", "1"); //币种，1人民币   66
            packageReqHandler.setParameter("input_charset", "UTF-8"); //字符编码
            
            //获取package包
            String packageValue = packageReqHandler.getRequestURL();
            logger.debug("订单生成的package: {}", packageValue);

            String noncestr = WXUtil.getNonceStr();
            String timestamp = WXUtil.getTimeStamp();
            String traceid = "";
            ////设置获取prepayid支付参数
            prepayReqHandler.setParameter("appid", ConstantUtil.APP_ID);
            prepayReqHandler.setParameter("appkey", ConstantUtil.APP_KEY);
            prepayReqHandler.setParameter("noncestr", noncestr);
            prepayReqHandler.setParameter("package", packageValue);
            prepayReqHandler.setParameter("timestamp", timestamp);
            prepayReqHandler.setParameter("traceid", traceid);

            //生成获取预支付签名
            String sign = prepayReqHandler.createSHA1Sign();
            //增加非参与签名的额外参数
            prepayReqHandler.setParameter("app_signature", sign);
            prepayReqHandler.setParameter("sign_method", ConstantUtil.SIGN_METHOD);
            String gateUrl = ConstantUtil.GATEURL + token;
            prepayReqHandler.setGateUrl(gateUrl);

            //获取prepayId
            String prepayid = prepayReqHandler.sendPrepay();
            //吐回给客户端的参数
            if (null != prepayid && !"".equals(prepayid)) {
                //输出参数列表
                clientHandler.setParameter("appid", ConstantUtil.APP_ID);
                clientHandler.setParameter("appkey", ConstantUtil.APP_KEY);
                clientHandler.setParameter("noncestr", noncestr);
                clientHandler.setParameter("package", "Sign=WXPay");
                clientHandler.setParameter("partnerid", ConstantUtil.PARTNER);
                clientHandler.setParameter("prepayid", prepayid);
                clientHandler.setParameter("timestamp", timestamp);

                //生成签名
                sign = clientHandler.createSHA1Sign();
                clientHandler.setParameter("sign", sign);

                result = clientHandler.getJsonBody();
                retcode = 0;
                retmsg = "OK";
            } else {
                retcode = -2;
                retmsg = "错误：获取prepayId失败";
            }
        } else {
            retcode = -1;
            retmsg = "错误：获取不到Token";
        }
		result.put("retcode", retcode);
        result.put("retmsg", retmsg);
        return result;
	}

}
