package com.qubaopen.survey.entity.customer;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity2;

/**
 * 客户金币 Created by duel on 2014/6/27.
 */

@Entity
@Table(name = "CUSTOMER_GOLD")
@Audited
public class CustomerGold extends AbstractBaseEntity2<Long> {

	private static final long serialVersionUID = -7239265439989417125L;

	/**
	 * 客户
	 */
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	private Customer customer;

	/**
	 * 总金币
	 */
	private int totalGold;

	/**
	 * 当前金币
	 */
	private int currentGold;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public int getTotalGold() {
		return totalGold;
	}

	public void setTotalGold(int totalGold) {
		this.totalGold = totalGold;
	}

	public int getCurrentGold() {
		return currentGold;
	}

	public void setCurrentGold(int currentGold) {
		this.currentGold = currentGold;
	}

}
