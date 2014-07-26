package com.qubaopen.survey.entity.interest;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars 兴趣问卷选项类型表
 */
@Entity
@Table(name = "interest_option_type")
@Audited
public class InterestOptionType extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -6212418266335746157L;

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
