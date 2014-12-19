package com.qubaopen.survey.entity.payment;

import java.util.List;
import java.util.Map;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.booking.Booking;

public class PayBean extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -1130396639616081902L;
	/** 订单ID. */
	private Booking booking;
	/** 可用的支付方式选项. */
	private List<Map<String, Object>> payTypes;

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public List<Map<String, Object>> getPayTypes() {
		return payTypes;
	}

	public void setPayTypes(List<Map<String, Object>> payTypes) {
		this.payTypes = payTypes;
	}

}
