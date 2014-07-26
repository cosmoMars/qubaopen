package com.qubaopen.survey.entity.survey;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.customer.Customer;
import com.qubaopen.survey.entity.manager.Manager;

/**
 * 调研问卷 Created by duel on 2014/6/25.
 */

@Entity
@Table(name = "SURVEY")
@Audited
public class Survey extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -4910589050975466729L;

	/**
	 * 调研问卷标题
	 */
	private String title;

	/**
	 * 调研成功获得的金币
	 */
	private Integer coin;

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
	private Integer answerCount;

	/**
	 * 优先级 数值越高越优先
	 */
	private Integer priority;

	/**
	 * 推荐值 数值越高越推荐
	 */
	private Integer recommendation;

	/**
	 * 需求数量
	 */
	private Integer requireCount;

	/**
	 * 完成数量
	 */
	private Integer completeCount;

	/**
	 * 问卷额外预留比例
	 */
	private Float extraProportion;

	/**
	 * 审核时间
	 */
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
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private byte[] pic;

	/**
	 * 开始时间
	 */
	private Date startTime;

	/**
	 * 结束时间
	 */
	private Date endTime;

	/**
	 * 人数限制
	 */
	private Integer limitCount;

	/**
	 * 审核人
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "aduit_user_id")
	private Manager aduitUser;

	/**
	 * 客户
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@OneToMany(mappedBy = "survey")
	private Set<SurveyQuota> quotas = new HashSet<>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getCoin() {
		return coin;
	}

	public void setCoin(Integer coin) {
		this.coin = coin;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(Integer answerCount) {
		this.answerCount = answerCount;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(Integer recommendation) {
		this.recommendation = recommendation;
	}

	public Integer getRequireCount() {
		return requireCount;
	}

	public void setRequireCount(Integer requireCount) {
		this.requireCount = requireCount;
	}

	public Integer getCompleteCount() {
		return completeCount;
	}

	public void setCompleteCount(Integer completeCount) {
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

	public SurveyType getSurveyType() {
		return surveyType;
	}

	public void setSurveyType(SurveyType surveyType) {
		this.surveyType = surveyType;
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

	public Integer getLimitCount() {
		return limitCount;
	}

	public void setLimitCount(Integer limitCount) {
		this.limitCount = limitCount;
	}

	public Manager getAduitUser() {
		return aduitUser;
	}

	public void setAduitUser(Manager aduitUser) {
		this.aduitUser = aduitUser;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Type getPublishType() {
		return publishType;
	}

	public void setPublishType(Type publishType) {
		this.publishType = publishType;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public byte[] getPic() {
		return pic;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}

	public Set<SurveyQuota> getQuotas() {
		return quotas;
	}

	public void setQuotas(Set<SurveyQuota> quotas) {
		this.quotas = quotas;
	}

}
