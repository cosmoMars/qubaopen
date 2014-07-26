package com.qubaopen.survey.entity.interest;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars 兴趣问卷内容类型表
 */
@Entity
@Table(name = "interest_type")
@Audited
public class InterestType extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 3713754530541959350L;

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
