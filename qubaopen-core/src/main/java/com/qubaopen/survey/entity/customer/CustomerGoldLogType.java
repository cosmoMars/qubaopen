package com.qubaopen.survey.entity.customer;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * 客户金币日志类型 Created by duel on 2014/6/27.
 */
@Entity
@Table(name = "CUSTOMER_GOLD_LOG_TYPE")
@Audited
public class CustomerGoldLogType extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -6217722598880429512L;

	/**
	 * 名称
	 */
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}