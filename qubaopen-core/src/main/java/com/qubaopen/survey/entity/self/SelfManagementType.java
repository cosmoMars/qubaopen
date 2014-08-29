package com.qubaopen.survey.entity.self;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * 自测管理类型
 * @author mars
 */
@Entity
@Table(name = "self_management_type")
@Audited
public class SelfManagementType extends AbstractPersistable<Long>{
	
	private static final long serialVersionUID = 6600789524204272919L;
	
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
