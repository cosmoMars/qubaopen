package com.qubaopen.survey.entity.survey;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.QuestionnaireTagType;
import com.qubaopen.survey.entity.customer.Customer;
import com.qubaopen.survey.entity.manager.Manager;

/**
 * 调研问卷 Created by duel on 2014/6/25.
 */

@Entity
@Table(name = "SURVEY")
@Audited
public class Survey extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 4360638005113172816L;

	/**
	 * 问卷类型
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "questionnaire_tag_type_id")
	private QuestionnaireTagType questionnaireTagType;

	/**
	 * 调研问卷标题
	 */
	private String title;

	/**
	 * 调研成功获得的金币
	 */
	private int coin;

	/**
	 * 问卷状态 0 初始状态 1 审核中 2 审核通过 3 审核未通过 4上线状态 5关闭状态
	 */
	@Enumerated
	private Status status;

	/**
	 * 问卷状态: INITIAL 0 初始状态,AUDITING 1 审核中,PASSAUDIT 2 审核通过,NOTAUDIT 3
	 * 审核未通过,ONLINE 4上线状态,CLOSED 5 关闭状态
	 */
	private enum Status {
		INITIAL, AUDITING, PASSAUDIT, NOTAUDIT, ONLINE, CLOSED
	}

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 调研参与人数
	 */
	private int answerCount;

	/**
	 * 优先级 数值越高越优先
	 */
	private int priority;

	/**
	 * 推荐值 数值越高越推荐
	 */
	private int recommendation;

	/**
	 * 需求数量
	 */
	private int requireCount;

	/**
	 * 完成数量
	 */
	private int completeCount;

	/**
	 * 问卷额外预留比例
	 */
	private Float extraProportion;

	/**
	 * 审核时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date aduitTime;

	/**
	 * 发布方式 0 趣宝盆发布 1 匿名发布
	 */
	@Enumerated
	private Type publishType;

	/**
	 * SYSTEM 0 趣宝盆发布, ANONYMOUS 1 匿名发布
	 */
	private enum Type {
		SYSTEM, ANONYMOUS
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "survey_type_id")
	private SurveyType surveyType;

	/**
	 * 图片
	 */
	private String picPath;

	/**
	 * 开始时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	/**
	 * 结束时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	/**
	 * 人数限制
	 */
	private int limitCount;

	/**
	 * 审核人
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "aduit_user_id")
	private Manager aduitUser;

	/**
	 * 客户
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@OneToMany(mappedBy = "survey")
	private Set<SurveyQuota> quotas = new HashSet<>();

	/**
	 * 地图区间最大值
	 */
	private Integer mapMax;

	public QuestionnaireTagType getQuestionnaireTagType() {
		return questionnaireTagType;
	}

	public void setQuestionnaireTagType(QuestionnaireTagType questionnaireTagType) {
		this.questionnaireTagType = questionnaireTagType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
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

	public int getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(int answerCount) {
		this.answerCount = answerCount;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(int recommendation) {
		this.recommendation = recommendation;
	}

	public int getRequireCount() {
		return requireCount;
	}

	public void setRequireCount(int requireCount) {
		this.requireCount = requireCount;
	}

	public int getCompleteCount() {
		return completeCount;
	}

	public void setCompleteCount(int completeCount) {
		this.completeCount = completeCount;
	}

	public Float getExtraProportion() {
		return extraProportion;
	}

	public void setExtraProportion(Float extraProportion) {
		this.extraProportion = extraProportion;
	}

	public Date getAduitTime() {
		return aduitTime;
	}

	public void setAduitTime(Date aduitTime) {
		this.aduitTime = aduitTime;
	}

	public Type getPublishType() {
		return publishType;
	}

	public void setPublishType(Type publishType) {
		this.publishType = publishType;
	}

	public SurveyType getSurveyType() {
		return surveyType;
	}

	public void setSurveyType(SurveyType surveyType) {
		this.surveyType = surveyType;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getLimitCount() {
		return limitCount;
	}

	public void setLimitCount(int limitCount) {
		this.limitCount = limitCount;
	}

	public Manager getAduitUser() {
		return aduitUser;
	}

	public void setAduitUser(Manager aduitUser) {
		this.aduitUser = aduitUser;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<SurveyQuota> getQuotas() {
		return quotas;
	}

	public void setQuotas(Set<SurveyQuota> quotas) {
		this.quotas = quotas;
	}

	public Integer getMapMax() {
		return mapMax;
	}

	public void setMapMax(Integer mapMax) {
		this.mapMax = mapMax;
	}

}
