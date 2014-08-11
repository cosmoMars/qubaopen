package com.qubaopen.survey.entity.customer;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * 客户金币日志 Created by duel on 2014/6/27.
 */
@Entity
@Table(name = "CUSTOMER_GOLD_LOG")
@Audited
public class CustomerGoldLog extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -6673332455622206796L;

	/**
	 * 客户
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "customer_id")
	private Customer customer;

	/**
	 * 客户金币日志类型
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "customer_gold_log_type_id")
	private CustomerGoldLogType customerGoldLogType;

	/**
	 * 正负 0加 1扣
	 */
	@Enumerated
	private Type type;

	/**
	 * 正负 ADD 0 加, MINUS 1 扣
	 */
	private enum Type {
		ADD, MINUS
	}

	/**
	 * 变动数额
	 */
	private int amount;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 详情 根据日志类型 详情可能记录不同的信息
	 */
	private String detail;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public CustomerGoldLogType getCustomerGoldLogType() {
		return customerGoldLogType;
	}

	public void setCustomerGoldLogType(CustomerGoldLogType customerGoldLogType) {
		this.customerGoldLogType = customerGoldLogType;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
