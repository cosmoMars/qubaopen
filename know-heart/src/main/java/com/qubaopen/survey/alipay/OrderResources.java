package com.qubaopen.survey.alipay.util.httpClient;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.qubaopen.survey.alipay.config.AlipayConfig;
import com.qubaopen.survey.alipay.sign.RSA;
import com.qubaopen.survey.entity.booking.Booking;
import com.qubaopen.survey.entity.user.User;
import com.qubaopen.survey.repository.booking.BookingRepository;

@RestController
@RequestMapping("orderResources")
@SessionAttributes("currentUser")
public class OrderResources {
	
	@Autowired
	private BookingRepository bookingRepository;
	
	
	@RequestMapping(value = "pay", method = RequestMethod.POST)
	public Map<String, Object> pay(@RequestParam(required = false) Long bookingId, @ModelAttribute("currentUser") User user) {
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		Booking booking = bookingRepository.findOne(bookingId);
		
		StringBuilder alipayText = new StringBuilder();
        alipayText.append("partner=\"").append(AlipayConfig.partner); // 商家名
        alipayText.append("\"&seller_id=\"").append("qubaopen@163.com"); // 支付宝账号
        alipayText.append("\"&out_trade_no=\"").append(booking.getId() + new Date().getTime()); // 订单号（订单号+"_"+支付ID+"_"++系统当前时间）
        alipayText.append("\"&subject=\""); // 商品名("购车订金:"+汽车品牌+车型名称+车款名称)
        alipayText.append(booking.getName());
		
        alipayText.append("\"&body=\""); // 商品描述
        alipayText.append("下单时间：").append(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
        alipayText.append("，订单号：").append(booking.getId());

        // 如果系统没有工作在产品模式时只需要支付0.01元
        alipayText.append("\"&total_fee=\"").append(booking.getMoney()); // 支付金额
        alipayText.append("\"&notify_url=\"").append("http://www.zhixin.me"); // 支付完成回调url
        alipayText.append("\"&service=\"mobile.securitypay.pay"); // 默认填写，@"mobile.securitypay.pay"
        alipayText.append("\"&_input_charset=\"UTF-8"); // 默认填写，@"utf-8"
        alipayText.append("\"&payment_type=\"1"); // 默认填写，@"1"
        alipayText.append("\"&it_b_pay=\"1m\""); // 超时时间
        
        String sign = RSA.sign(alipayText.toString(), AlipayConfig.private_key, AlipayConfig.input_charset);
        String message = alipayText.toString() + "&sign=\"" + sign + "\"&sign_type=\"RSA\"";

        map.put("name", "支付宝");
        map.put("payData", message);
		
		return map;
		
	}

}
