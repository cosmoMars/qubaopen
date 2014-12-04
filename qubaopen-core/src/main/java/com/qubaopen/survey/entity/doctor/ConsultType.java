package com.qubaopen.survey.entity.doctor;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

@Entity
@Table(name = "consult_type")
@Audited
public class ConsultType extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -8614840398797506271L;
	/**
	 * 方式
	 */
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
