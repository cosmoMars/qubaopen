package com.qubaopen.survey.entity.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars 地区表
 */
@Entity
@Table(name = "region_code")
@Audited
public class RegionCode extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -5836136461102037096L;

	/**
	 * 城市代码
	 */
	@Column(unique = true)
	private String code;

	/**
	 * 名称
	 */
	private String name;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "city_code_id")
	private CityCode cityCode;

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

	public CityCode getCityCode() {
		return cityCode;
	}

	public void setCityCode(CityCode cityCode) {
		this.cityCode = cityCode;
	}

}
