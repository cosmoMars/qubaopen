package com.qubaopen.survey.order.notify;

import java.io.IOException;
import java.util.Date;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.survey.entity.booking.Booking;
import com.qubaopen.survey.entity.payment.PaymentMode;
import com.qubaopen.survey.repository.booking.BookingRepository;
import com.qubaopen.survey.tenpay.RequestHandler;
import com.qubaopen.survey.tenpay.ResponseHandler;
import com.qubaopen.survey.tenpay.client.ClientResponseHandler;
import com.qubaopen.survey.tenpay.client.TenpayHttpClient;
import com.qubaopen.survey.tenpay.util.ConstantUtil;

@RestController
@RequestMapping(value = "/notify/order/wxpay")
public class OrderWeixinNotifyController {

	@Autowired
	private BookingRepository bookingRepository;
	
	private Logger logger = LoggerFactory.getLogger(OrderWeixinNotifyController.class);
	
	@RequestMapping
	public void notify(HttpServletRequest request, HttpServletResponse response) throws Exception {

        //---------------------------------------------------------
        //财付通支付通知（后台通知）示例，商户按照此文档进行开发即可
        //---------------------------------------------------------
        //创建支付应答对象
        ResponseHandler resHandler = new ResponseHandler(request, response);
        resHandler.setKey(ConstantUtil.PARTNER_KEY);

        //判断签名
        if (resHandler.isTenpaySign()) {
            //通知id
            String notify_id = resHandler.getParameter("notify_id");

            //创建请求对象
            RequestHandler queryReq = new RequestHandler();
            //通信对象
            TenpayHttpClient httpClient = new TenpayHttpClient();
            //应答对象
            ClientResponseHandler queryRes = new ClientResponseHandler();

            //通过通知ID查询，确保通知来至财付通
            queryReq.init();
            queryReq.setKey(ConstantUtil.PARTNER_KEY);
            queryReq.setGateUrl("https://gw.tenpay.com/gateway/verifynotifyid.xml");
            queryReq.setParameter("partner", ConstantUtil.PARTNER);
            queryReq.setParameter("notify_id", notify_id);

            //通信对象
            httpClient.setTimeOut(5);
            //设置请求内容
            httpClient.setReqContent(queryReq.getRequestURL());
            logger.debug("queryReq:" + queryReq.getRequestURL());
            //后台调用
            if (httpClient.call()) {
                String returnContent;

                //设置结果参数
                queryRes.setContent(httpClient.getResContent());
                logger.debug("queryRes:" + httpClient.getResContent());
                queryRes.setKey(ConstantUtil.PARTNER_KEY);

                //获取返回参数
                String retcode = queryRes.getParameter("retcode");
                String trade_state = queryRes.getParameter("trade_state");

                String trade_mode = queryRes.getParameter("trade_mode");

                // 记录通知报文
                String out_trade_no = queryRes.getParameter("out_trade_no");
                String otn[] = out_trade_no.split("_");
                String transaction_id = queryRes.getParameter("transaction_id");
                @SuppressWarnings("unchecked")
				SortedMap<String, String> allParameters = queryRes.getAllParameters();
                logger.debug("订单接收到微信支付回调, params={}", allParameters);
                
                Booking booking = bookingRepository.findByTradeNo(out_trade_no);
                
//                PaymentNotifyRecordEntity recordEntity = recordNotify(allParameters, otn[0], transaction_id, trade_state);

                //判断签名及结果
                if (queryRes.isTenpaySign() && "0".equals(retcode) && "0".equals(trade_state) && "1".equals(trade_mode)) {
                    logger.debug("购车订单微信支付订单查询成功 {}", transaction_id);

                    //取结果参数做业务处理
                    logger.debug("out_trade_no:" + out_trade_no +
                            " transaction_id:" + transaction_id);

                    String total_fee = queryRes.getParameter("total_fee");
                    logger.debug("trade_state:" + queryRes.getParameter("trade_state") +
                            " total_fee:" + total_fee);
                    //如果有使用折扣券，discount有值，total_fee+discount=原请求的total_fee
                    logger.debug("discount:" + queryRes.getParameter("discount") + " time_end:" + queryRes.getParameter("time_end"));
                    //------------------------------
                    //处理业务开始
                    //------------------------------

                    // 用户实际记住的金额
                    Float price = new Float(total_fee)/100;
//                    orderMan.pay(PaymentMode.WXPAY, out_trade_no, price.toString(), transaction_id);

                    //处理数据库逻辑
                    //注意交易单不要重复处理
                    //注意判断返回金额

                    //------------------------------
                    //处理业务完毕
                    //------------------------------
                    returnContent = "Success";
                    resHandler.sendToCFT("Success");
                } else {
                    returnContent = "Fail";
                    //错误时，返回结果未签名，记录retcode、retmsg看失败详情。
                    logger.debug("购车订单微信支付查询验证签名失败或业务错误, retcode: {}, retmsg: {}", queryRes.getParameter("retcode"), queryRes.getParameter("retmsg"));
                }

                logger.debug("购车订单微信支付回调：orderNumber={}, trade_no={}, returnContent={}", otn[0], transaction_id, returnContent);
            
                booking.setPayTime(new Date());
            	booking.setStatus(Booking.Status.Paid);
            	bookingRepository.save(booking);
                
//                recordEntity.setReturnContent(returnContent);
//                notifyRecordEntityManager.save(recordEntity);
            } else {
                //有可能因为网络原因，请求已经处理，但未收到应答。
                logger.warn("订单微信支付后台调用通信失败, responseCode={}, errorInfo={}", httpClient.getResponseCode(), httpClient.getErrInfo());
            }
        } else {
            logger.debug("订单微信支付通知签名验证失败, params={}", resHandler.getAllParameters());
        }
    
	}
}
