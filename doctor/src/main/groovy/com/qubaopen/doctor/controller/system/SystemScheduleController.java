package com.qubaopen.doctor.controller.system;

import com.qubaopen.doctor.repository.doctor.BookingRepository;
import com.qubaopen.doctor.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


   /* @Transactional
//    @Scheduled(fixedRate = 1000l)
//    @Scheduled(cron = "0/5 * * * * ?")
    @Scheduled(cron = "0 0/10 * * * ?")
    public void execute() {

        System.out.println(new Date());

        Map<String, Object> filters = new HashMap<>();
        filters.put("status_equal", Booking.Status.Paid);
        filters.put("sendUser_equal", false);
        List<Booking> bookings = bookingRepository.findAll(filters);

        Date now = new Date();
        List<Booking> sendBookings = new ArrayList<>();

        for (Booking booking : bookings) {

            int diffMins = (int) ((booking.getOutDated().getTime() - now.getTime()) / 1000 / 60);
            if (diffMins <= 120) {
                String param = "{\"param1\" : \"http://zhixin.me/smsRedirectDr.html\"}";

                smsService.sendSmsMessage(booking.getPhone(), 6, param);
                booking.setSendUser(true);
                sendBookings.add(booking);
            }
        }
        bookingRepository.save(sendBookings);

    }*/

}
