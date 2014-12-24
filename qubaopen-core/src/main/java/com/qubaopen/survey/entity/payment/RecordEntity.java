package com.qubaopen.survey.entity.payment;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

@Entity
@Table(name = "record_entity")
@Audited
public class RecordEntity extends AbstractBaseEntity<Long> {
	
	private static final long serialVersionUID = -3459813429053273456L;
	private String json;

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

}
