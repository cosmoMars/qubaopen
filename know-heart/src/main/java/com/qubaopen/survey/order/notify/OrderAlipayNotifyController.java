package com.qubaopen.survey.order.notify;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.survey.alipay.util.AlipayNotify;
import com.qubaopen.survey.entity.booking.Booking;
import com.qubaopen.survey.entity.cash.DoctorCashLog;
import com.qubaopen.survey.entity.payment.RecordEntity;
import com.qubaopen.survey.repository.booking.BookingRepository;
import com.qubaopen.survey.repository.cash.DoctorCashLogRepository;
import com.qubaopen.survey.repository.payment.RecordEntityRepository;

/**
 * 支付宝回调入口.
 */
@RestController
@RequestMapping(value = "/notify/order/alipay")
public class OrderAlipayNotifyController {
	/** Log4j日志记录. */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private DoctorCashLogRepository doctorCashLogRepository;
	
	@Autowired
	private RecordEntityRepository recordEntityRepository;
	
    /**
     * 处理支付回调.
     */
    @RequestMapping
    public String notify(HttpServletRequest request) throws Exception {
    	
        // 获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>();
        @SuppressWarnings("rawtypes")
		Map requestParams = request.getParameterMap();
        for (@SuppressWarnings("rawtypes")
		Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }

        logger.debug("支付宝异步通知参数列表：{}", params);
        
        //商户订单号
        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
//        String otn[] = out_trade_no.split("_");

        //支付宝交易号
        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

        //交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
        // 交易金额
        String price = new String(request.getParameter("price").getBytes("ISO-8859-1"), "UTF-8");
        
        Booking booking = bookingRepository.findByTradeNo(out_trade_no);

        boolean verify = AlipayNotify.verify(params);

        logger.debug("交易号 {} 的校验结果：{}", trade_no, verify);

        recordNotify(params, out_trade_no, trade_status);

        if (verify) {//验证成功
        	try {
	            if (trade_status.equals("TRADE_FINISHED")) {
	            	booking.setPayTime(new Date());
	            	booking.setStatus(Booking.Status.Paid);
	            	bookingRepository.save(booking);
//	            	orderMan.pay(PaymentMode.ALIPAY, out_trade_no, price, trade_no);
//	                logger.info("购车订单 {} 接收到支付通知状态 {}", otn[0], trade_no);
	
	                //注意：
	                //该种交易状态只在两种情况下出现
	                //1、开通了普通即时到账，买家付款成功后。
	                //2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
	            } else if (trade_status.equals("TRADE_SUCCESS")) {
	            	booking.setPayTime(new Date());
	            	booking.setStatus(Booking.Status.Paid);
	            	bookingRepository.save(booking);
//	            	orderMan.pay(PaymentMode.ALIPAY, out_trade_no, price, trade_no);
//	                logger.info("购车订单 {} 接收到支付通知状态 {}", otn[0], trade_no);
	                //注意：
	                //该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。
	            }
	            
	            
        	} catch (Exception ex) {
				logger.error("支付宝支付完成后回调业务处理类发生异常，订单号："+out_trade_no, ex);
				return "fail";
			}
        	
        	DoctorCashLog cashLog = new DoctorCashLog();
        	cashLog.setCash(Double.valueOf(price));
        	cashLog.setUser(booking.getUser());
        	cashLog.setUserName(booking.getName());
        	cashLog.setDoctor(booking.getDoctor());
        	cashLog.setTime(new Date());
        	cashLog.setType(DoctorCashLog.Type.In);
        	cashLog.setPayType(DoctorCashLog.PayType.Alipay);
        	
        	doctorCashLogRepository.save(cashLog);
        	
            return "success";
        } else {//验证失败
            logger.error("交易号 {} 处理失败", trade_no);
            return "fail";
        }
    }
    
    private void recordNotify(Map<String, String> params, String out_trade_no, String trade_status) {
    	RecordEntity recordEntity = new RecordEntity();
    	recordEntity.setJson(params.toString());
    	recordEntity.setTradeNo(out_trade_no);
    	recordEntity.setTradeStatus(trade_status);
    	recordEntityRepository.save(recordEntity);
    }

    /**
     * 记录通知日志.
     */
//    private void recordNotify(Map<String, String> params, String out_trade_no, String trade_no, String trade_status) {
////        notifyRecordEntityManager.recordNotify(out_trade_no, trade_no, orderMan.getBusinessCode(), PaymentMode.ALIPAY.getCode(), trade_status, JSON.toJSONString(params));
//    }
}
