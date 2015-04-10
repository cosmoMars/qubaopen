package com.qubaopen.backend.controller.system;

import com.qubaopen.backend.repository.booking.BookingRepository;
import com.qubaopen.backend.service.SmsService;
import com.qubaopen.backend.utils.CommonEmail;
import com.qubaopen.survey.entity.booking.Booking;
import com.qubaopen.survey.entity.booking.ResolveType;
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

    @Autowired
    private CommonEmail commonEmail;


    @Scheduled(cron = "0 0/2 * * * ?")
    public void execute() {

//        sendMessage();
        cycleBooking();

    }

    private void sendMessage() {

        Map<String, Object> filters = new HashMap<>();
        filters.put("status_equal", Booking.Status.Paid);
        filters.put("sendDoctor_isFalse", null);
        List<Booking> bookings = bookingRepository.findAll(filters);

        Date now = new Date();
        List<Booking> sendBookings = new ArrayList<>();

        for (Booking booking : bookings) {
            // 预约时间小于现在，超时时间大于现在
            if (booking.getTime() != null && booking.getTime().compareTo(now) < 0 && booking.getOutDated() != null && booking.getOutDated().compareTo(now) == 1) {
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


    // 关于订单处理
    @Transactional
    public void cycleBooking() {

        Map<String, Object> filters = new HashMap<>();

        Date now = new Date();

        // resolveType 1 预约
        filters.put("status_equal", Booking.Status.Booking);
        filters.put("resolveType_equal", ResolveType.None);
        filters.put("sendEmail_isFalse", null);
        List<Booking> bookings = bookingRepository.findAll(filters);

        List<Booking> sendBookings = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        for (Booking booking : bookings) {
            c.setTimeInMillis(booking.getCreatedDate().getMillis());
            c.getTime();
            int hour = c.get(Calendar.HOUR_OF_DAY);

            int diffSec = (int) ((now.getTime() - booking.getCreatedDate().getMillis()) / 1000);
            if (hour >= 9 && hour < 14) {
                if (diffSec > 14400 ) {
                    booking = doResolveType(ResolveType.DoctorNoResponse, booking);
                    sendBookings.add(booking);
                }
            } else {
                if (diffSec > 57600) {
                    booking = doResolveType(ResolveType.DoctorNoResponse, booking);
                    sendBookings.add(booking);
                }
            }
        }
        bookingRepository.save(sendBookings);

        // resolveType 2 婉拒
        filters.clear();
        filters.put("status_equal", Booking.Status.Refusal);
        filters.put("resolveType_equal", ResolveType.None);
        filters.put("sendEmail_isFalse", null);
        bookings = bookingRepository.findAll(filters);

        sendBookings.clear();
        for (Booking booking : bookings) {
            booking = doResolveType(ResolveType.DoctorRefusal, booking);
            sendBookings.add(booking);
        }
        bookingRepository.save(sendBookings);

        // resolveType 3 用户未响应
        filters.clear();
        filters.put("status_equal", Booking.Status.Accept);
        filters.put("resolveType_equal", ResolveType.None);
        filters.put("sendEmail_isFalse", null);
        bookings = bookingRepository.findAll(filters);

        sendBookings.clear();
        for (Booking booking : bookings) {
            int diffSec = (int) ((now.getTime() - booking.getLastModifiedDate().getMillis()) / 1000);
            if (diffSec > 7 * 24 * 3600) {
                booking = doResolveType(ResolveType.UserNoResponse, booking);
                sendBookings.add(booking);
            }
        }
        bookingRepository.save(sendBookings);

        // resolveType 4 加急
        filters.clear();
        filters.put("status_equal", Booking.Status.Paid);
        filters.put("quick_isTrue", null);
        filters.put("time_isNotNull", null);
        filters.put("sendEmail_isFalse", null);
        bookings = bookingRepository.findAll(filters);

        List<Booking> refundBookings = new ArrayList<>();
        sendBookings.clear();
        for (Booking booking : bookings) {

            int diffMins = (int) ((booking.getTime().getTime() - now.getTime()) / 1000 / 60);

            if (diffMins < 240) {
                booking.setStatus(Booking.Status.Refunding);
                refundBookings.add(booking);
            }
            int diffSec = (int) (now.getTime() - booking.getLastModifiedDate().getMillis()) / 1000;
            if (diffSec < 1.5 * 3600 && booking.getResolveType() == ResolveType.None) {
                booking = doResolveType(ResolveType.QuickConfirm, booking);
                sendBookings.add(booking);
            }
        }
        bookingRepository.save(sendBookings);
        bookingRepository.save(refundBookings);

        // resolveType 5 改约
        filters.clear();
        filters.put("status_equal", Booking.Status.ChangeDate);
        filters.put("resolveType_equal", ResolveType.None);
        filters.put("sendEmail_isFalse", null);
        bookings = bookingRepository.findAll(filters);

        sendBookings.clear();
        for (Booking booking : bookings) {
            booking = doResolveType(ResolveType.BookingChange, booking);
            sendBookings.add(booking);
        }

        bookingRepository.save(sendBookings);

        // resolveType 6 回访
        filters.clear();
        filters.put("resolveType_equal", ResolveType.None);
        filters.put("time_isNotNull", null);
        filters.put("sendEmail_isFalse", null);

        List<Booking> consultedBookings = new ArrayList<>();

        bookings = bookingRepository.findAll(filters);

        sendBookings.clear();
        for (Booking booking : bookings) {

            if (booking.getUserStatus() != null && booking.getDoctorStatus() != null && (booking.getUserStatus() == Booking.BookStatus.NoConsult ||  booking.getDoctorStatus() == Booking.BookStatus.NoConsult)) {
                booking =  doResolveType(ResolveType.BookingReview, booking);
                sendBookings.add(booking);
            }
            int diffSec = (int) (now.getTime() - booking.getTime().getTime()) / 1000;
            if (diffSec > 15 * 24 * 3600) {
                if (booking.getUserStatus() == null) {
                    booking.setUserStatus(Booking.BookStatus.Consulted);
                }
                if (booking.getDoctorStatus() == null) {
                    booking.setDoctorStatus(Booking.BookStatus.Consulted);
                }
                consultedBookings.add(booking);
            }
        }
        bookingRepository.save(sendBookings);
        bookingRepository.save(consultedBookings);
    }

    public Booking doResolveType(ResolveType type, Booking booking) {

        if (!booking.isSendEmail()) {
            booking.setResolveType(type);
            StringBuffer content = new StringBuffer();
            content.append("订单： ");
            content.append(booking.getId());
            content.append(" 处于 ");
            content.append(booking.getResolveType().toString());
            content.append(" 状态中，请及时处理");

            String url = "http://zhixin.com/dl?bookingId=" + booking.getId();
            commonEmail.sendTextMail(content.toString(), "349280576@qq.com", url);
            // todo 接受菲菲url

            booking.setSendEmail(true);
            return booking;
        }
        return booking;
    }

}
