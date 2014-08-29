package com.qubaopen.survey.entity.self;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "graphics_type")
@Audited
public class GraphicsType extends AbstractPersistable<Long>{
	
	private static final long serialVersionUID = -8499692669341654799L;
	/**
	 * 图形名称
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
