package com.qubaopen.survey.entity.booking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.doctor.Assistant;

import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 * Created by mars on 15/3/26.
 */
@Entity
@Table(name = "booking_process_log")
@Audited
public class BookingProcessLog extends AbstractBaseEntity<Long> {

    private static final long serialVersionUID = -7997385061385792740L;
    /**
     * 订单
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Booking booking;

    /**
     * 助手
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Assistant assistant;

    /**
     * 备注
     */
    private String remark;

    /**
     * 订单
     */
    @Enumerated
    private ResolveType resolveType;

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Assistant getAssistant() {
        return assistant;
    }

    public void setAssistant(Assistant assistant) {
        this.assistant = assistant;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ResolveType getResolveType() {
        return resolveType;
    }

    public void setResolveType(ResolveType resolveType) {
        this.resolveType = resolveType;
    }
}
