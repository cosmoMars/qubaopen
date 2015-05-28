package com.qubaopen.survey.entity.user;

import com.qubaopen.survey.entity.booking.Booking;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

/**
 * Created by mars on 15/5/28.
 */
@Entity
@Table
public class UserBookingData extends AbstractPersistable<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Booking booking;

    /**
     * 用户修改
     */
    private boolean userRefresh;

    /**
     * 医师，诊所修改
     */
    private boolean thirdRefresh;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public boolean isUserRefresh() {
        return userRefresh;
    }

    public void setUserRefresh(boolean userRefresh) {
        this.userRefresh = userRefresh;
    }

    public boolean isThirdRefresh() {
        return thirdRefresh;
    }

    public void setThirdRefresh(boolean thirdRefresh) {
        this.thirdRefresh = thirdRefresh;
    }
}
