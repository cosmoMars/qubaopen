package com.qubaopen.survey.entity.survey;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.qubaopen.survey.entity.base.AreaCode;

/**
 * 调研问卷 配额 Created by duel on 2014/6/25.
 */

@Entity
@Table(name = "SURVEY_QUOTA")
@Audited
public class SurveyQuota extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 9056945125158860414L;

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
	private int minAge;

	/**
	 * 最大年龄
	 */
	private int maxAge;

	/**
	 * 该配额的需求数量
	 */
	private int requireNum;

	/**
	 * 该配额的完成数量
	 */
	private int completeNum;

//	/**
//	 * 省市区
//	 */
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "province_code_id")
//	private ProvinceCode provinceCode;

	/**
	 * 地区代码
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "area_code_id")
	private AreaCode areaCode;

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

	public int getMinAge() {
		return minAge;
	}

	public void setMinAge(int minAge) {
		this.minAge = minAge;
	}

	public int getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}

	public int getRequireNum() {
		return requireNum;
	}

	public void setRequireNum(int requireNum) {
		this.requireNum = requireNum;
	}

	public int getCompleteNum() {
		return completeNum;
	}

	public void setCompleteNum(int completeNum) {
		this.completeNum = completeNum;
	}

	public AreaCode getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(AreaCode areaCode) {
		this.areaCode = areaCode;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

}
