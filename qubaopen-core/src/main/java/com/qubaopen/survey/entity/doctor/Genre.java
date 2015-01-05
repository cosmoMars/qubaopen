package com.qubaopen.survey.entity.doctor;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars
 * 流派
 */
@Entity
@Table(name = "genre")
@Audited
public class Genre extends AbstractPersistable<Long> {
	
	private static final long serialVersionUID = 5382561791556669144L;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
