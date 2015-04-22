package com.knowheart3.order;

import com.knowheart3.repository.booking.BookingRepository;
import com.knowheart3.service.SmsService;
import com.pingplusplus.Pingpp;
import com.pingplusplus.model.Channel;
import com.pingplusplus.model.Charge;
import com.qubaopen.survey.entity.booking.Booking;
import com.qubaopen.survey.entity.user.User;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.*;

/**
 * Created by mars on 15/3/13.
 */
@RestController
@RequestMapping("pingpp")
@SessionAttributes("currentUser")
public class PingppResources {

    static {
//        Pingpp.apiKey = "sk_live_jX1mb908ern5r1qz9KGGiXfH";
        Pingpp.apiKey = "sk_test_SOujjTjTar5KeP4a9OvvD4CG";
    }

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    SmsService smsService;

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
    @Transactional
    @RequestMapping(value = "pingppOrder", method = RequestMethod.POST)
    public void pingppOrder(@RequestParam(required = false) Long bookingId,
            @RequestParam(required = false) Boolean quick,
            @RequestParam(required = false) Double money,
            @RequestParam(required = false) String time,
            @RequestParam(required = false) String type,
            HttpServletRequest req, HttpServletResponse resp,
            @ModelAttribute("currentUser") User user) throws IOException {

        PrintWriter out;
        if (null == user.getId()) {
            out = resp.getWriter();
            out.print("{\"success\" : \"0\", \"message\" : \"err000\"}");
            return;
        }

        Booking booking = bookingRepository.findOne(bookingId);
        resp.setContentType("application/json; charset=utf-8");

        if (booking.getStatus() != Booking.Status.Accept && booking.getStatus() != Booking.Status.Paying) {
            out = resp.getWriter();
            out.print("{\"success\" : \"0\", \"message\" : \"err811\"}");
            return;
        }
        if (null != booking.getDoctor() && booking.getStatus() == Booking.Status.Accept) {

            if (null == booking.getOutDated() || (new Date()).getTime() > booking.getOutDated().getTime()) {
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.HOUR_OF_DAY, 1);
                booking.setOutDated(c.getTime());
            }
        }
        if (null != booking.getDoctor()) {
            Map<String, Object> map = new HashMap<>();
            map.put("user.id_equal", user.getId());
//            map.put("doctor.id_equal", booking.getDoctor().getId());
            map.put("id_notEqual", bookingId);
//            map.put("status_equal", Booking.Status.Accept);
            map.put("status_equal", Booking.Status.Paying);
//            map.put("outDated_isNotNull", null);
            List<Booking> exist = bookingRepository.findAll(map);

            if (exist.size() > 0) {
                out = resp.getWriter();
                out.print("{\"success\" : \"0\", \"message\" : \"err809\"}");
                out.close();
                return;
            }
        }

        if (null != quick) {
            booking.setQuick(quick);
            if (quick) {
                String param = "{\"url\" : \"http://zhixin.me/smsRedirectDr.html\"}";
                smsService.sendSmsMessage(booking.getDoctor().getPhone(), 4, param);
            }
        }
        if (null != money) {
            double quickMoney = money - booking.getMoney();
            booking.setQuickMoney(quickMoney);
        }
        if (null != time) {
            try {
                booking.setTime(DateUtils.parseDate(time, "yyyy-MM-dd HH:mm"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
//        Pingpp.apiKey = "sk_test_SOujjTjTar5KeP4a9OvvD4CG";
        // 创建订单
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("order_no",  booking.getTradeNo());
        int amount = (int)(money * 100);
        chargeParams.put("amount", amount);
        Map<String, String> app = new HashMap<>();
        app.put("id", "app_KavHuL08GO8O4Wbn");
        chargeParams.put("app", app);
        String channel = null;
        switch (type) {
            case "1" :
                channel = Channel.ALIPAY;
                break;
            case "2" :
                channel = Channel.WECHAT;
                break;
            case "3" :
                channel = Channel.UPMP;
                break;
        }
        chargeParams.put("channel",  channel);
        chargeParams.put("currency", "cny");
//        chargeParams.put("client_ip",  req.getRemoteAddr());
        chargeParams.put("client_ip",  "127.0.0.1");
        chargeParams.put("subject", "知心心理咨询");

        StringBuffer content = new StringBuffer();
        content.append("下单时间：").append(DateFormatUtils.format(booking.getTime(), "yyyy-MM-dd"));
        content.append("，订单号：").append(booking.getTradeNo());
        chargeParams.put("body", content.toString());

        Charge ch;
        try {
            ch = Charge.create(chargeParams);
            out = resp.getWriter();
            out.print(ch);
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        booking.setChargeId(ch.getId());
        booking.setStatus(Booking.Status.Paying);
        bookingRepository.save(booking);
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

//        Pingpp.apiKey = "sk_test_SOujjTjTar5KeP4a9OvvD4CG";
        Booking booking = bookingRepository.findOne(id);
        Charge ch;
        try {
            ch = Charge.retrieve(booking.getChargeId());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        resp.setContentType("application/json; charset=utf-8");

        PrintWriter out;
        try {
            out = resp.getWriter();
            out.print(ch);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
