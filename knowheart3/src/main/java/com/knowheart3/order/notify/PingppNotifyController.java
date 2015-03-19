package com.knowheart3.order.notify;

import com.knowheart3.repository.booking.BookingRepository;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Notify;
import com.pingplusplus.model.Refund;
import com.qubaopen.survey.entity.booking.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 * Created by mars on 15/3/16.
 */
@RestController
@RequestMapping("/notify/order/pingpp")
public class PingppNotifyController {


    @Autowired
    private BookingRepository bookingRepository;

    @RequestMapping
	public void notify(HttpServletRequest request, HttpServletResponse response) throws Exception {

        StringBuffer stringBuffer = new StringBuffer();
        String line;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                stringBuffer.append(line);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        String result;
        Object obj = Notify.parseNotify(stringBuffer.toString());

        // 对异步通知做处理
        if (obj instanceof Charge) {
            Charge charge = (Charge) obj;
            // 开发者在此处加入对支付异步通知的处理代码
            if (true == charge.getPaid()) {
                Booking booking = bookingRepository.findByChargeId(charge.getId());

                if (null != booking.getHospital()) {
                    booking.setStatus(Booking.Status.PayAccept);
                } else if (null != booking.getDoctor()) {
                    if (false == booking.isQuick()) {
                        booking.setStatus(Booking.Status.PayAccept);
                    } else {
                        booking.setStatus(Booking.Status.Paid);
                    }
                }
                booking.setOutDated(null);
                bookingRepository.save(booking);
            }
            // 处理成功返回success
            result = "success";
        } else {
            result = "fail";
        }

        if (obj instanceof Refund) {
            Refund refund = (Refund) obj;
            // 开发者在此处加入对退款异步通知的处理代码

            // 处理成功返回success
            result = "success";
        } else {
            result = "fail";
        }
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(result);
        out.close();
	}

}
