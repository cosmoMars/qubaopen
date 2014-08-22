package com.qubaopen.survey.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "questionnire_type")
@Audited
public class QuestionnaireType extends AbstractPersistable<Long>{

	private static final long serialVersionUID = -5097071965518888512L;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 备注
	 */
	private String remark;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
