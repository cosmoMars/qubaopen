package com.knowheart3.controller.system;

import com.knowheart3.repository.booking.BookingRepository;
import com.knowheart3.service.SmsService;
import com.qubaopen.survey.entity.booking.Booking;
import org.apache.commons.lang3.StringUtils;
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
    @Scheduled(cron = "0 0/10 * * * ?")
    public void execute() {

        System.out.println(new Date());

        Map<String, Object> filters = new HashMap<>();
        filters.put("status_equal", Booking.Status.Paid);
        filters.put("sendUser_isFalse", null);
        List<Booking> bookings = bookingRepository.findAll(filters);

        Date now = new Date();
        List<Booking> sendBookings = new ArrayList<>();

        for (Booking booking : bookings) {
            if (booking.getOutDated() != null) {
                int diffMins = (int) ((booking.getOutDated().getTime() - now.getTime()) / 1000 / 60);
                if (diffMins <= 120) {
                    String address = "";
                    if (booking.getDoctor() != null && booking.getDoctor().getDoctorInfo() != null) {
                        if (booking.getDoctor().getDoctorInfo().getAddress() != null) {
                            address = booking.getDoctor().getDoctorInfo().getAddress();
                        }
                    }
//                    String param = "{\"param1\" : " + DateFormatUtils.format(booking.getTime(), "MM月dd日HH:mm") + ", \"param2\" : " + address + "}";

                    String param = "{\"param1\" : \"http://zhixin.me/smsRedirectDr.html\"}";

                    Map<String, Object> result = smsService.sendSmsMessage(booking.getPhone(), 6, param);

                    if (StringUtils.equals((String)result.get("resCode"), "0")) {
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
