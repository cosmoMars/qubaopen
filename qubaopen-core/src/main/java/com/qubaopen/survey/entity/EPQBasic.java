package com.qubaopen.survey.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * EPQ 基础表
 * @author mars
 *
 */
@Entity
@Table(name = "epq_basic")
@Audited
public class EPQBasic extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -5162750496260340143L;

	/**
	 * 性别
	 */
	private Sex sex;

	private enum Sex {
		MALE, FEMALE
	}

	/**
	 * 最小年龄
	 */
	private Integer minAge;

	/**
	 * 最大年龄
	 */
	private Integer maxAge;

	/**
	 * 量表名称
	 */
	private String name;

	/**
	 * m值
	 */
	private String mValue;

	/**
	 * sd值
	 */
	private String sdValue;

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public Integer getMinAge() {
		return minAge;
	}

	public void setMinAge(Integer minAge) {
		this.minAge = minAge;
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getmValue() {
		return mValue;
	}

	public void setmValue(String mValue) {
		this.mValue = mValue;
	}

	public String getSdValue() {
		return sdValue;
	}

	public void setSdValue(String sdValue) {
		this.sdValue = sdValue;
	}


}
