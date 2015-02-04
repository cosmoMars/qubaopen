package com.qubaopen.survey.entity.user;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "user_feed_back_type")
@Audited
public class UserFeedBackType extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 7968204710303669424L;
	/**
	 * 类型名称
	 */
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
