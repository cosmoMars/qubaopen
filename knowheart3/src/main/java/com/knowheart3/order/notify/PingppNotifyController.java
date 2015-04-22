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

        request.setCharacterEncoding("UTF-8");

        // 读取异步通知数据
        BufferedReader reader = request.getReader();
        StringBuffer buffer = new StringBuffer();
        String string;
        while ((string = reader.readLine()) != null) {
            buffer.append(string);
        }
        reader.close();

        // 解析异步通知数据
        String notifyJson = new String(buffer);
        Object o = Notify.parseNotify(notifyJson);

        PrintWriter out = response.getWriter();

        System.out.println("开始回调接口");
        // 对异步通知做处理
        if (o instanceof Charge) {
            Charge charge = (Charge) o;
            // 开发者在此处加入对支付异步通知的处理代码
            if (charge.getPaid()) {
                Booking booking = bookingRepository.findByChargeId(charge.getId());

                System.out.println("订单id   " + booking.getId() + "  订单号  " + booking.getTradeNo());

                if (!booking.isNotify()) {
                    if (null != booking.getHospital()) {
                        System.out.println("回调诊所");
                        booking.setStatus(Booking.Status.PayAccept);
                    } else if (null != booking.getDoctor()) {
                        System.out.println("回调医师");
                        if (!booking.isQuick()) {
                            booking.setStatus(Booking.Status.PayAccept);
                        } else {
                            booking.setStatus(Booking.Status.Paid);
                        }
                    }
                    System.out.println("回调状态 ＝ " + booking.getStatus().ordinal());
                    booking.setNotify(true);
                    booking.setOutDated(null);
                    bookingRepository.save(booking);
                }
            }
            // 处理成功返回success
            out.println("success");
            return;
        }

        if (o instanceof Refund) {
            Refund refund = (Refund) o;
            // 开发者在此处加入对退款异步通知的处理代码

            // 处理成功返回success
            out.println("success");
            return;
        }

        out.println("fail");
        return;
    }

}
