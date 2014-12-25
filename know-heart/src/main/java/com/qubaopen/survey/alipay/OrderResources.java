package com.qubaopen.survey.alipay;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.qubaopen.survey.entity.payment.PayEntity;
import com.qubaopen.survey.entity.payment.PayStatus;
import com.qubaopen.survey.entity.user.User;
import com.qubaopen.survey.repository.booking.BookingRepository;
import com.qubaopen.survey.repository.payment.PayEntityRepository;

@RestController
@RequestMapping("orderResources")
@SessionAttributes("currentUser")
public class OrderResources {
	
	private static Logger logger = LoggerFactory.getLogger(OrderResources.class);
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private PayEntityRepository payEntityRepository;
	
	
	@RequestMapping(value = "payBookingOrder", method = RequestMethod.POST)
	public Map<String, Object> payBookingOrder(@RequestParam(required = false) Long bookingId, @ModelAttribute("currentUser") User user) {
		
		logger.trace("====== {}", bookingId);
		Booking booking = bookingRepository.findOne(bookingId);
		
		PayEntity payEntity = new PayEntity();
		
		payEntity.setBooking(booking);
		payEntity.setPayTime(booking.getTime());
		payEntity.setPayment("支付宝");
		payEntity.setPayStatus(PayStatus.WAITING_PAYMENT);
		payEntity.setPayAmount(booking.getMoney());
		payEntityRepository.save(payEntity);
		
		Map<String, Object> map = new HashMap<String, Object>();

		StringBuilder alipayText = new StringBuilder();
        alipayText.append("partner=\"").append(AlipayConfig.partner); // 商家名
        alipayText.append("\"&seller_id=\"").append("qubaopen@163.com"); // 支付宝账号
        alipayText.append("\"&out_trade_no=\"").append(booking.getTradeNo()); // 订单号（订单号+"_"+支付ID+"_"++系统当前时间）
        alipayText.append("\"&subject=\""); // 商品名("购车订金:"+汽车品牌+车型名称+车款名称)
        alipayText.append("知心心理咨询");
		
        alipayText.append("\"&body=\""); // 商品描述
        alipayText.append("下单时间：").append(DateFormatUtils.format(booking.getTime(), "yyyy-MM-dd"));
        alipayText.append("，订单号：").append(booking.getTradeNo());

        // 如果系统没有工作在产品模式时只需要支付0.01元
        alipayText.append("\"&total_fee=\"").append(booking.getMoney()); // 支付金额
        alipayText.append("\"&notify_url=\"").append("http://www.zhixin.me"); // 支付完成回调url
        alipayText.append("\"&service=\"mobile.securitypay.pay"); // 默认填写，@"mobile.securitypay.pay"
        alipayText.append("\"&_input_charset=\"UTF-8"); // 默认填写，@"utf-8"
        alipayText.append("\"&payment_type=\"1"); // 默认填写，@"1"
        alipayText.append("\"&it_b_pay=\"30m\""); // 超时时间
        
        String sign = RSA.sign(alipayText.toString(), AlipayConfig.private_key, AlipayConfig.input_charset);
        String message = alipayText.toString() + "&sign=\"" + sign + "\"&sign_type=\"RSA\"";

        map.put("payType", 1);
        map.put("name", "支付宝");
        map.put("payData", message);
		
		return map;
		
	}

}