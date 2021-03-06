package com.qubaopen.doctor.controller.system;

import com.qubaopen.doctor.repository.doctor.BookingRepository;
import com.qubaopen.doctor.service.SmsService;
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

//        System.out.println(new Date());
        sendMessage();


    }
   private void sendMessage() {

       Map<String, Object> filters = new HashMap<>();
       filters.put("status_equal", Booking.Status.Paid);
       filters.put("sendDoctor_isFalse", null);
       List<Booking> bookings = bookingRepository.findAll(filters);

       Date now = new Date();
       List<Booking> sendBookings = new ArrayList<>();

       for (Booking booking : bookings) {
           // 超时时间大于现在
           if (booking.getTime() != null && booking.getOutDated() != null && booking.getOutDated().compareTo(now) == 1) {
               int diffMins = (int) ((booking.getOutDated().getTime() - now.getTime()) / 1000 / 60);
               // 小于两小时提醒
               if (diffMins <= 120) {

                   String param = "{\"url\" : \"http://zhixin.me/smsRedirectDr.html\"}";

                   Map<String, Object> result = smsService.sendSmsMessage(booking.getDoctor().getPhone(), 6, param);
                   if (StringUtils.equals((String) result.get("resCode"), "0")) {
                       booking.setSendDoctor(true);
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
