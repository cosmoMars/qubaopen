package com.qubaopen.survey.entity.cash;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * @author mars 银行
 */
@Entity
@Table(name = "bank")
@Audited
public class Bank extends AbstractBaseEntity<Long> {
	private static final long serialVersionUID = 8667063459728392885L;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
