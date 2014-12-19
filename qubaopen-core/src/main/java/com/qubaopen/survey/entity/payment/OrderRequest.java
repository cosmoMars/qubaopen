package com.qubaopen.survey.entity.payment;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.booking.Booking;

//@Entity
//@Table(name = "order_request")
//@Audited
public class OrderRequest extends AbstractBaseEntity<Long>{

	private static final long serialVersionUID = 1966499245160538294L;

	private Booking booking;
	
	private String name;
	
	private String phone;
	
	private Double payAmount;
	
	private String verificationCode;
	
}
