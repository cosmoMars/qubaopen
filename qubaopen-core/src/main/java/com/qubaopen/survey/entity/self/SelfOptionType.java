package com.qubaopen.survey.entity.self;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars 自测问卷选项类型表
 */
@Entity
@Table(name = "self_option_type")
@Audited
public class SelfOptionType extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 170054800212077850L;
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
