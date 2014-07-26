package com.qubaopen.survey.entity.system;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * 滚动信息 Created by duel on 2014/6/27.
 */
@Entity
@Table(name = "SYSTEM_ROLLING_MESSAGE")
@Audited
public class SystemRollingMessage extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 5086721444941390671L;

	/**
	 * 内容
	 */
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
