package com.qubaopen.survey.entity.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars 省份代码
 */
@Entity
@Table(name = "province_code")
@Audited
public class ProvinceCode extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -1757625723191983902L;

	/**
	 * 省份代码
	 */
	@Column(unique = true)
	private String code;

	/**
	 * 名称
	 */
	private String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
