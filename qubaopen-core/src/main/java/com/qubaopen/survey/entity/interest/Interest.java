package com.qubaopen.survey.entity.interest;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.QuestionnaireTagType;
import com.qubaopen.survey.entity.QuestionnaireType;

/**
 * @author mars 兴趣问卷
 */
@Entity
@Table(name = "interest")
@Audited
public class Interest extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -1578765434385263230L;

	/**
	 * 问卷的答案类型
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "questionnaire_type_id")
	private QuestionnaireType questionnaireType;

	/**
	 * 问卷类型
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "interest_type_id")
	private InterestType interestType;

	/**
	 * 问卷标签
	 */
	@OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
	private Set<QuestionnaireTagType> questionnaireTagTypes;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 金币
	 */
	private int golds;

	/**
	 * 状态 问卷状态值: 0 初始状态 1 上线状态 2 关闭状态
	 */
	@Enumerated
	private Status status;

	/**
	 * INITIAL 0 初始状态, ONLINE, 1 上线状态, CLOSED 2 关闭状态
	 */
	private enum Status {
		INITIAL, ONLINE, CLOSED
	}

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 答题总人数
	 */
	private int totalRespondentsCount;

	/**
	 * 推荐值
	 */
	private int recommendedValue;

	/**
	 * 指导语
	 */
	private String guidanceSentence;

	/**
	 * 得分系数
	 */
	private int coefficient = 1;

	/**
	 * 问卷缩写
	 */
	private String abbreviation;

	/**
	 * 图片路径
	 */
	private String picPath;

	public Set<QuestionnaireTagType> getQuestionnaireTagTypes() {
		return questionnaireTagTypes;
	}

	public void setQuestionnaireTagTypes(Set<QuestionnaireTagType> questionnaireTagTypes) {
		this.questionnaireTagTypes = questionnaireTagTypes;
	}

	public InterestType getInterestType() {
		return interestType;
	}

	public void setInterestType(InterestType interestType) {
		this.interestType = interestType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getGolds() {
		return golds;
	}

	public void setGolds(int golds) {
		this.golds = golds;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getTotalRespondentsCount() {
		return totalRespondentsCount;
	}

	public void setTotalRespondentsCount(int totalRespondentsCount) {
		this.totalRespondentsCount = totalRespondentsCount;
	}

	public int getRecommendedValue() {
		return recommendedValue;
	}

	public void setRecommendedValue(int recommendedValue) {
		this.recommendedValue = recommendedValue;
	}

	public String getGuidanceSentence() {
		return guidanceSentence;
	}

	public void setGuidanceSentence(String guidanceSentence) {
		this.guidanceSentence = guidanceSentence;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public int getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(int coefficient) {
		this.coefficient = coefficient;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

}
