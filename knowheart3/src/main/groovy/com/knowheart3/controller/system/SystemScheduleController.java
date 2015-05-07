package com.knowheart3.controller.system;

import com.knowheart3.repository.booking.BookingRepository;
import com.knowheart3.service.SmsService;
import com.qubaopen.survey.entity.booking.Booking;
import com.qubaopen.survey.entity.doctor.DoctorInfo;
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
    @Scheduled(cron = "0 0/10 * * * ?")
    public void execute() {

//        System.out.println(new Date());
        sendMessage();

    }


    private void sendMessage() {

        Map<String, Object> filters = new HashMap<>();
        filters.put("status_equal", Booking.Status.Paid);
        filters.put("sendUser_isFalse", null);
        List<Booking> bookings = bookingRepository.findAll(filters);

        filters.clear();
        filters.put("status_equal", Booking.Status.Paid);
        filters.put("send15Mins_isFalse", null);
        filters.put("consultType_equal", Booking.ConsultType.Video);
        List<Booking> minsBookings = bookingRepository.findAll(filters);

        Date now = new Date();
        List<Booking> sendBookings = new ArrayList<>();

        for (Booking booking : bookings) {
            // 超时时间大于现在
//            && booking.getTime().compareTo(now) < 0
            if (booking.getTime() != null && booking.getOutDated() != null && booking.getOutDated().compareTo(now) == 1) {
                int diffMins = (int) ((booking.getOutDated().getTime() - now.getTime()) / 1000 / 60);
                // 小于两小时提醒
                if (diffMins <= 120) {
                    StringBuffer address = new StringBuffer();
                    StringBuffer param = new StringBuffer();
                    String name;
                    DoctorInfo di = null;
                    if (booking.getDoctor() != null && booking.getDoctor().getDoctorInfo() != null) {

                        di = booking.getDoctor().getDoctorInfo();

                        address.append(di.getAddress() != null ? di.getAddress() : "");
                        name = di != null ? di.getName() : "";
                    } else {
                        name = booking.getDoctorName();
                    }

                    Map<String, Object> result;

                    // 视频咨询
                    if (booking.getConsultType() == Booking.ConsultType.Video) {
                        param.append("{\"name\" : \"" + name + "\"");
                        param.append(", \"qq\" : \"" + (di != null ? di.getQq() : "") + "\"");
                        param.append(", \"time\" : \"" + DateFormatUtils.format(booking.getTime(), "yyyy-MM-dd HH:mm:ss") + "\"}");

                        result = smsService.sendSmsMessage(booking.getPhone(), 10, param.toString());
                    } else {
                        param.append("{\"time\" : \"" + DateFormatUtils.format(booking.getTime(), "MM月dd日HH:mm") + "\"");
                        param.append(", \"address\" : \"" + address + "\"}");
                        result = smsService.sendSmsMessage(booking.getPhone(), 7, param.toString());
                    }

                    if (StringUtils.equals((String) result.get("resCode"), "0")) {
                        booking.setSendUser(true);
                        sendBookings.add(booking);
                    }
                }
            }
        }

        for (Booking minsBooking : minsBookings) {
            //&& minsBooking.getTime().compareTo(now) < 0
            if (minsBooking.getTime() != null && minsBooking.getOutDated() != null && minsBooking.getOutDated().compareTo(now) == 1) {
                int diffMins = (int) ((minsBooking.getOutDated().getTime() - now.getTime()) / 1000 / 60);
                if (diffMins < 25) {
                    StringBuffer param = new StringBuffer();
                    String name;
                    String qq = "";
                    DoctorInfo di = null;
                    Map<String, Object> result;
                    if (minsBooking.getDoctor() != null && minsBooking.getDoctor().getDoctorInfo() != null) {
                        di = minsBooking.getDoctor().getDoctorInfo();
                        name = di != null ? di.getName() : "";
                    } else {
                        name = minsBooking.getDoctorName();
                    }
                    param.append("{\"name\" : \"" + name + "\"");
                    param.append(", \"qq\" : \"" + (di != null ? di.getQq() : "") + "\"");
                    param.append(", \"time\" : \"" + DateFormatUtils.format(minsBooking.getTime(), "yyyy-MM-dd HH:mm:ss") + "\"}");

                    result = smsService.sendSmsMessage(minsBooking.getPhone(), 9, param.toString());
                    if (StringUtils.equals((String) result.get("resCode"), "0")) {
                        minsBooking.setSend15Mins(true);
                        sendBookings.add(minsBooking);
                    }
                }
            }

        }
        bookingRepository.save(sendBookings);
    }

}
