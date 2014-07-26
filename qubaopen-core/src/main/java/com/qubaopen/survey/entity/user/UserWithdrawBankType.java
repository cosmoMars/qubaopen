package com.qubaopen.survey.entity.user;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * 用户提现 银行表 Created by duel on 2014/6/30.
 */

@Entity
@Table(name = "user_withdraw_bank_type")
@Audited
public class UserWithdrawBankType extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -3102994902231943387L;

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
