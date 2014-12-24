package com.qubaopen.survey.entity.payment;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.booking.Booking;

@Entity
@Table(name = "pay_entity")
@Audited
public class PayEntity extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 7174898733629692651L;
	/** 订单ID. */
	@ManyToOne(fetch = FetchType.LAZY)
	private Booking booking;
	/** 支付时间. */
	private Date payTime;
	/** 支付方式. */
	private String payment;
	/** 支付状态. */
	@Enumerated
	private PayStatus payStatus;
	/** 支付金额. */
	private Double payAmount;
	/** 支付流水号. */
	private String paySerialNumber;
	/** 退款时间. */
	private Date refundTime;
	/** 退款流水号. */
	private String refundSerialNumber;
	/** 退款金额. */
	private Double refundAmount;

	public Date getPayTime() {
		return payTime;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public PayStatus getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(PayStatus payStatus) {
		this.payStatus = payStatus;
	}

	public Double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	public String getPaySerialNumber() {
		return paySerialNumber;
	}

	public void setPaySerialNumber(String paySerialNumber) {
		this.paySerialNumber = paySerialNumber;
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}

	public String getRefundSerialNumber() {
		return refundSerialNumber;
	}

	public void setRefundSerialNumber(String refundSerialNumber) {
		this.refundSerialNumber = refundSerialNumber;
	}

	public Double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(Double refundAmount) {
		this.refundAmount = refundAmount;
	}

}
