package com.qubaopen.survey.entity.interest;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.QuestionnaireTagType;

/**
 * @author mars 兴趣问卷
 */
@Entity
@Table(name = "interest")
@Audited
public class Interest extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 2686784453446880940L;

	/**
	 * 问卷类型
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "interest_type_id")
	private InterestType interestType;

	/**
	 * 问卷标签
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "questionnaire_tag_type_id")
	private QuestionnaireTagType questionnaireTagType;

	/**
	 * 问卷的答案方式  乱序 DISOREDER, 得分 SORCE, 乱序得分 DISORDERSCORE
	 */
	@Enumerated
	private Type type;

	private enum Type {
		DISOREDER, SORCE, DISORDERSCORE
	}

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 金币
	 */
	private Integer golds;

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
	private Integer totalRespondentsCount;

	/**
	 * 推荐值
	 */
	private Integer recommendedValue;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private byte[] pic;

	public InterestType getInterestType() {
		return interestType;
	}

	public void setInterestType(InterestType interestType) {
		this.interestType = interestType;
	}

	public QuestionnaireTagType getQuestionnaireTagType() {
		return questionnaireTagType;
	}

	public void setQuestionnaireTagType(QuestionnaireTagType questionnaireTagType) {
		this.questionnaireTagType = questionnaireTagType;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getGolds() {
		return golds;
	}

	public void setGolds(Integer golds) {
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

	public Integer getTotalRespondentsCount() {
		return totalRespondentsCount;
	}

	public void setTotalRespondentsCount(Integer totalRespondentsCount) {
		this.totalRespondentsCount = totalRespondentsCount;
	}

	public Integer getRecommendedValue() {
		return recommendedValue;
	}

	public void setRecommendedValue(Integer recommendedValue) {
		this.recommendedValue = recommendedValue;
	}

	public byte[] getPic() {
		return pic;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}

}
