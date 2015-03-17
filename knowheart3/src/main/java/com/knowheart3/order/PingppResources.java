package com.knowheart3.order;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.knowheart3.repository.booking.BookingRepository;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Channel;
import com.pingplusplus.model.Charge;
import com.qubaopen.survey.entity.booking.Booking;
import com.qubaopen.survey.entity.user.User;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mars on 15/3/13.
 */
@RestController
@RequestMapping("pingpp")
@SessionAttributes("currentUser")
public class PingppResources {

    @Autowired
    BookingRepository bookingRepository;

    /**
     * ping++ 发起支付
     * @param bookingId
     * @param quick
     * @param money
     * @param time
     * @param type
     * @param req
     * @param resp
     */
    @RequestMapping(value = "pingppOrder", method = RequestMethod.POST)
    public void pingppOrder(@RequestParam(required = false) Long bookingId,
            @RequestParam(required = false) Boolean quick,
            @RequestParam(required = false) Double money,
            @RequestParam(required = false) String time,
            @RequestParam(required = false) String type,
            HttpServletRequest req, HttpServletResponse resp,
            @ModelAttribute("currentUser") User user) {

//        if (null == user.getId()) {
//            return "{\"success\" : \"0\", \"message\" : \"err000\"}";
//        }

        Booking booking = bookingRepository.findOne(bookingId);
        if (null != quick) {
            booking.setQuick(quick);
        }
        if (null != money) {
            booking.setMoney(money);
        }
        if (null != time) {
            try {
                booking.setTime(DateUtils.parseDate(time, "yyyy-MM-dd HH:mm"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Pingpp.apiKey = "sk_test_SOujjTjTar5KeP4a9OvvD4CG";

        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("order_no",  booking.getTradeNo());
        int amount = (int)(booking.getMoney() * 100);
        chargeParams.put("amount", amount);
        Map<String, String> app = new HashMap<String, String>();
        app.put("id", "app_KavHuL08GO8O4Wbn");
        chargeParams.put("app", app);
        String channel = null;
        switch (type) {
            case "1" :
                channel = Channel.ALIPAY;
                break;
            case "2" :
                channel = Channel.ALIPAY;
                break;
            case "3" :
                channel = Channel.UPMP;
                break;
        }
        chargeParams.put("channel",  channel);
        chargeParams.put("currency", "cny");
        chargeParams.put("client_ip",  req.getRemoteAddr());
        chargeParams.put("subject",  "知心心理咨询");

        StringBuffer content = new StringBuffer();
        content.append("下单时间：").append(DateFormatUtils.format(booking.getTime(), "yyyy-MM-dd"));
        content.append("，订单号：").append(booking.getTradeNo());
        chargeParams.put("body",  content.toString());
        Charge ch = null;
        try {
            ch = Charge.create(chargeParams);
        } catch (PingppException e) {
            e.printStackTrace();
        }
        resp.setContentType("application/json; charset=utf-8");

        booking.setChargeId(ch.getId());
        bookingRepository.save(booking);
        PrintWriter out = null;
        try {
            out = resp.getWriter();
            out.print(ch);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Pingpp.apiKey = "sk_test_SOujjTjTar5KeP4a9OvvD4CG";
//        Map<String, Object> chargeMap = new HashMap<String, Object>();
//        int amount = (int)(booking.getMoney() * 100);
//        chargeMap.put("amount", amount);
//        chargeMap.put("currency", "cny");
//        chargeMap.put("subject", "知心心理咨询");
//        StringBuffer content = new StringBuffer();
//        content.append("下单时间：").append(DateFormatUtils.format(booking.getTime(), "yyyy-MM-dd"));
//        content.append("，订单号：").append(booking.getTradeNo());
////
//        chargeMap.put("body", content.toString());
//        chargeMap.put("order_no", booking.getTradeNo()); // 商户系统自己生成的订单号
//
//        String channel = null;
//        switch (type) {
//            case "1" :
//                channel = Channel.ALIPAY;
//                break;
//            case "2" :
//                channel = Channel.ALIPAY;
//                break;
//            case "3" :
//                channel = Channel.UPMP;
//                break;
//        }
//
//        chargeMap.put("channel", channel);
////        chargeMap.put("client_ip", "101.231.124.8");
//        chargeMap.put("client_ip", req.getRemoteAddr());
//        //115.28.176.74
//        Map<String, String> app = new HashMap<String, String>();
//        app.put("id", "app_KavHuL08GO8O4Wbn");
//        chargeMap.put("app", app);
//        Map<String, String> extra = new HashMap<String, String>();
//
//        chargeMap.put("extra", extra);
//
//        try {
//            ch = Charge.create(chargeMap);
//        } catch (PingppException e) {
//            e.printStackTrace();
//        }

//        booking.setChargeId(ch.getId());
//        bookingRepository.save(booking);

//        resp.setContentType("application/json; charset=utf-8");
//
//        PrintWriter out = null;
//        try {
//            out = resp.getWriter();
//            out.print(ch);
//            out.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return ch;
    }

    /**
     * 查询charge
     * @param id
     * @param req
     * @param resp
     */
    @RequestMapping(value = "queryCharge/{id}", method = RequestMethod.GET)
    public void queryCharge(@PathVariable Long id,
            HttpServletRequest req,
            HttpServletResponse resp,
            @ModelAttribute("currentUser") User user) {

//        if (null == user.getId()) {
//            return "{\"success\" : \"0\", \"message\" : \"err000\"}";
//        }

        Pingpp.apiKey = "sk_test_SOujjTjTar5KeP4a9OvvD4CG";
        Booking booking = bookingRepository.findOne(id);
        Charge ch = null;
        try {
            ch = Charge.retrieve(booking.getChargeId());

        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        }
        resp.setContentType("application/json; charset=utf-8");

        PrintWriter out = null;
        try {
            out = resp.getWriter();
            out.print(ch);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return ch;
    }

}
