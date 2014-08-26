package com.qubaopen.survey.entity.self;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars 自测问卷内容类型表
 */
@Entity
@Table(name = "self_type")
@Audited
public class SelfType extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 2228206463003316322L;
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
