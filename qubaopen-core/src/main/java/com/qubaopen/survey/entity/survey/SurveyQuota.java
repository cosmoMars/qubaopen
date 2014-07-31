package com.qubaopen.survey.entity.survey;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.qubaopen.survey.entity.base.ProvinceCode;

/**
 * 调研问卷 配额 Created by duel on 2014/6/25.
 */

@Entity
@Table(name = "SURVEY_QUOTA")
@Audited
public class SurveyQuota extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 4427676320593963672L;

	/**
	 * 所从属的调研问卷
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "survey_id")
	private Survey survey;

	/**
	 * 性别
	 */
	@Enumerated
	private Sex sex;

	private enum Sex {
		MALE, FEMALE, OTHER
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
	 * 该配额的需求数量
	 */
	private Integer requireNum;

	/**
	 * 该配额的完成数量
	 */
	private Integer completeNum;

	/**
	 * 省市区
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "province_code_id")
	private ProvinceCode provinceCode;

	/**
	 * 是否激活（控制是否启用） 1启用 0不启用
	 */
	private boolean activated;

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

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

	public ProvinceCode getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(ProvinceCode provinceCode) {
		this.provinceCode = provinceCode;
	}

	public Integer getRequireNum() {
		return requireNum;
	}

	public void setRequireNum(Integer requireNum) {
		this.requireNum = requireNum;
	}

	public Integer getCompleteNum() {
		return completeNum;
	}

	public void setCompleteNum(Integer completeNum) {
		this.completeNum = completeNum;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

}
