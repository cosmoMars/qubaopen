package com.qubaopen.survey.entity.topic;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * 练习
 */
@Entity
@Table(name = "exercise")
@Audited
public class Exercise extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -4231296994213087799L;
	
	/**
	 * 练习标题
	 */
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
