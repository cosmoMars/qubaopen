package com.qubaopen.survey.entity.self;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars 问卷结果类型
 */
@Entity
@Table(name = "self_result_type")
@Audited
public class SelfResultType extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 4768706240089063427L;
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
