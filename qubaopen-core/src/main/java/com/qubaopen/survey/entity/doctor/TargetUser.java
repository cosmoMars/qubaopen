package com.qubaopen.survey.entity.doctor;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

//@Entity
//@Table(name = "target_user")
//@Audited
public class TargetUser extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 6739204207771092795L;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
