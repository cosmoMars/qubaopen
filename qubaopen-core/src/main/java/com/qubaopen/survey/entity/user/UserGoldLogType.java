package com.qubaopen.survey.entity.user;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * 用户金币日志信息 Created by duel on 2014/6/30.
 */
@Entity
@Table(name = "user_gold_log_type")
@Audited
public class UserGoldLogType extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 2932699076385689268L;

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
