package com.knowheart3.controller.system;

import com.knowheart3.repository.booking.BookingRepository;
import com.knowheart3.service.SmsService;
import com.qubaopen.survey.entity.booking.Booking;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by mars on 15/3/24.
 */
@EnableScheduling
@RestController
@RequestMapping("systemSchedule")
public class SystemScheduleController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private BookingRepository bookingRepository;


    @Transactional
//    @Scheduled(fixedRate = 1000l)
//    @Scheduled(cron = "0/5 * * * * ?")
    @Scheduled(cron = "0 0/1 * * * ?")
    public void execute() {

        System.out.println(new Date());
        sendMessage();

    }

    private void sendMessage() {

        Map<String, Object> filters = new HashMap<>();
        filters.put("status_equal", Booking.Status.Paid);
        filters.put("sendUser_isFalse", null);
        List<Booking> bookings = bookingRepository.findAll(filters);

        Date now = new Date();
        List<Booking> sendBookings = new ArrayList<>();

        for (Booking booking : bookings) {
            // 预约时间小于现在，超时时间大于现在
            if (booking.getTime() != null && booking.getTime().compareTo(now) < 0 && booking.getOutDated() != null && booking.getOutDated().compareTo(now) == 1) {
                int diffMins = (int) ((booking.getOutDated().getTime() - now.getTime()) / 1000 / 60);
                // 小于两小时提醒
                if (diffMins <= 120) {
                    String address = "";
                    if (booking.getDoctor() != null && booking.getDoctor().getDoctorInfo() != null) {
                       address = booking.getDoctor().getDoctorInfo().getAddress() != null ? booking.getDoctor().getDoctorInfo().getAddress() : "";
                    }
                    String param = "{\"time\" : \"" + DateFormatUtils.format(booking.getTime(), "MM月dd日HH:mm") + "\", \"address\" : \"" + address + "\"}";
                    System.out.println(param);

//                    String param = "{\"url\" : \"http://zhixin.me/smsRedirectDr.html\"}";

                    Map<String, Object> result = smsService.sendSmsMessage(booking.getPhone(), 7, param);
                    if (StringUtils.equals((String) result.get("resCode"), "0")) {
                        booking.setSendUser(true);
                        sendBookings.add(booking);
                    }
                }
            }
        }
        if (sendBookings.size() > 0) {
            bookingRepository.save(sendBookings);
        }
    }

}
