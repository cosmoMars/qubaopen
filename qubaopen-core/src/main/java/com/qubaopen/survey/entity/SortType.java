package com.qubaopen.survey.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "sort_type")
@Audited
public class SortType extends AbstractPersistable<Long>{

	private static final long serialVersionUID = -7255566931863815346L;

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
