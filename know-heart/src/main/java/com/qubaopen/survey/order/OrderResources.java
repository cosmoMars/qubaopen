package com.qubaopen.survey.order;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
import com.qubaopen.survey.service.creator.WeixinContentCreator;

@RestController
@RequestMapping("orderResources")
@SessionAttributes("currentUser")
public class OrderResources {
	
	private static Logger logger = LoggerFactory.getLogger(OrderResources.class);
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private PayEntityRepository payEntityRepository;
	
	@Autowired
	private WeixinContentCreator weixinContentCreator;
	
	
	@Transactional
	@RequestMapping(value = "alipayOrder", method = RequestMethod.POST)
	public Map<String, Object> alipayOrder(@RequestParam(required = false) Long bookingId,
			@RequestParam(required = false) Boolean quick,
			@RequestParam(required = false) Double money,
			@RequestParam(required = false) String time,
			@ModelAttribute("currentUser") User user) {
		
		logger.trace("====== {}", bookingId);
		Booking booking = bookingRepository.findOne(bookingId);
		if (quick != null) {
			booking.setQuick(quick);
		}
		if (money != null) {
			booking.setMoney(money);
		}
		if (time != null) {
			try {
				booking.setTime(DateUtils.parseDate(time, "yyyy-MM-dd HH:mm"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		bookingRepository.save(booking);
		
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
	
	
	/**
	 * @param bookingId
	 * @param quick
	 * @param money
	 * @param time
	 * @param user
	 * @return
	 * 确认订单
	 */
	@Transactional
	@RequestMapping(value = "confirmBooking", method = RequestMethod.POST)
	public Map<String, Object> confirmBooking(@RequestParam(required = false) Long bookingId,
			@RequestParam(required = false) Boolean quick,
			@RequestParam(required = false) Double money,
			@RequestParam(required = false) String time,
			@ModelAttribute("currentUser") User user) {
		
		logger.trace("====== {}", bookingId);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		Booking booking = bookingRepository.findOne(bookingId);
		
		if (money == null) {
			map.put("success", "0");
			map.put("message", "err802");
			return map;
		}
		
		if (quick != null) {
			booking.setQuick(quick);
			
			if (Booking.ConsultType.Facetoface == booking.getConsultType()) {
				double faceFee = booking.getDoctor().getDoctorInfo().getOfflineFee() * 1.5;
				if (money < faceFee) {
					map.put("success", "0");
					map.put("message", "err803");
					return map;
				}
			}
			if (Booking.ConsultType.Video == booking.getConsultType()) {
				double videoFee = booking.getDoctor().getDoctorInfo().getOnlineFee() * 1.5;
				if (money < videoFee) {
					map.put("success", "0");
					map.put("message", "err803");
					return map;
				}
			}
			booking.setMoney(money);
		}
		
		
		if (time != null) {
			try {
				booking.setTime(DateUtils.parseDate(time, "yyyy-MM-dd HH:mm"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		bookingRepository.save(booking);
		
		PayEntity payEntity = new PayEntity();
		
		payEntity.setBooking(booking);
		payEntity.setPayTime(booking.getTime());
		payEntity.setPayment("支付宝");
		payEntity.setPayStatus(PayStatus.WAITING_PAYMENT);
		payEntity.setPayAmount(booking.getMoney());
		payEntityRepository.save(payEntity);
		
		/*StringBuilder alipayText = new StringBuilder();
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
        String message = alipayText.toString() + "&sign=\"" + sign + "\"&sign_type=\"RSA\"";*/
		
		map.put("success", "1");
        map.put("id", booking.getId());
        String doctorName = "";
        if (booking.getDoctor() != null &&  booking.getDoctor().getDoctorInfo() != null && booking.getDoctor().getDoctorInfo().getName() != null) {
        	doctorName = booking.getDoctor().getDoctorInfo().getName();
        }
        map.put("doctor", doctorName);
        String hospitalName = "";
        if (booking.getHospital() != null && booking.getHospital().getHospitalInfo() != null && booking.getHospital().getHospitalInfo().getName() != null) {
        	hospitalName = booking.getHospital().getHospitalInfo().getName();
        }
        map.put("hospital", hospitalName);
		map.put("tradeNo", booking.getTradeNo());
		map.put("name", booking.getName());
		map.put("phone", booking.getPhone());
		
		if (booking.getSex() != null) {
			map.put("sex", booking.getSex().ordinal());
		} else {
			map.put("sex", "");
		}
		
		map.put("birthday", booking.getBirthday());
		map.put("profession", booking.getProfession());
		map.put("city", booking.getCity());
		map.put("married", booking.isMarried());
		map.put("haveChildren", booking.isHaveChildren());
		map.put("helpReason", booking.getHelpReason());
		map.put("otherProblem", booking.getOtherProblem());
		map.put("treatmented", booking.isTreatmented());
		map.put("haveConsulted", booking.isHaveConsulted());
		map.put("refusalReason", booking.getRefusalReason());
		map.put("time", booking.getTime());
		map.put("payTime", booking.getPayTime());
		map.put("quick", booking.isQuick());
		if (booking.getConsultType() != null) {
			map.put("consultType", booking.getConsultType().ordinal());
		} else {
			map.put("consultType", "");
		}
		if (booking.getStatus() != null) {
			map.put("status", booking.getStatus().ordinal());
		} else {
			map.put("status", "");
		}
		
		map.put("money", booking.getMoney());
		return map;
	}
	
	@RequestMapping(value = "wenxinOrder", method = RequestMethod.POST)
	private Map<String, Object> wenxinOrder(@RequestParam(required = false) Long bookingId,
			@RequestParam(required = false) Boolean quick,
			@RequestParam(required = false) Double money,
			@RequestParam(required = false) String time,
			@ModelAttribute("currentUser") User user) {
		
		logger.trace("====== {}", bookingId);
		Booking booking = bookingRepository.findOne(bookingId);
		if (quick != null) {
			booking.setQuick(quick);
		}
		if (money != null) {
			booking.setMoney(money);
		}
		if (time != null) {
			try {
				booking.setTime(DateUtils.parseDate(time, "yyyy-MM-dd HH:mm"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		bookingRepository.save(booking);
		
		PayEntity payEntity = new PayEntity();
		
		payEntity.setBooking(booking);
		payEntity.setPayTime(booking.getTime());
		payEntity.setPayment("微信");
		payEntity.setPayStatus(PayStatus.WAITING_PAYMENT);
		payEntity.setPayAmount(booking.getMoney());
		payEntityRepository.save(payEntity);
		Object buildContent = null;
		try {
			buildContent = weixinContentCreator.buildContent(booking);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", "1");
		map.put("weixinData", buildContent);
		
		return map;
	}
}
