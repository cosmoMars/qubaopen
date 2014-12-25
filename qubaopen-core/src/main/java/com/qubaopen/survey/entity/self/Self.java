package com.qubaopen.survey.entity.self;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.QuestionnaireType;

/**
 * @author mars 自测
 */
@Entity
@Table(name = "self")
@Audited
public class Self extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -1342110032318313940L;

	/**
	 * 问卷类型
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "questionnaire_type_id")
	private QuestionnaireType questionnaireType;

	/**
	 * 问卷分组类型
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "self_type_id")
	private SelfType selfType;

//	/**
//	 * 自测类型 Character性格分析, Emotional 情绪管理, Personal个人发展
//	 */
//	@Enumerated
//	private ManagementType managementType;
//
//	public enum ManagementType {
//		Character, Emotional, Personal
//	}

	/**
	 * 自测管理类型
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "self_management_type_id")
	private SelfManagementType selfManagementType;
	
	/**
	 * 得分系数
	 */
	private int coefficient = 1;

	/**
	 * 问卷缩写
	 */
	@Column(unique = true)
	private String abbreviation;

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
	public enum Status {
		INITIAL, ONLINE, CLOSED
	}

	/**
	 * 备注
	 */
	@Column(columnDefinition = "TEXT")
	private String remark;

	/**
	 * 答题总人数
	 */
	private int totalRespondentsCount;

	/**
	 * 推荐值 优先级
	 */
	private int recommendedValue;

	/**
	 * 指导语
	 */
	private String guidanceSentence;

	/**
	 * 地图区间最大值
	 */
	private Integer mapMax;

	/**
	 * 图片地址
	 */
	private String picPath;

	/**
	 * 间隔时间 单位：小时
	 */
	private int intervalTime;

	/**
	 * 图形类型
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "graphics_type_id")
	private GraphicsType graphicsType;
	
	/**
	 * 问卷组
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "self_group_id")
	private SelfGroup selfGroup;
	
	/**
	 * 提示
	 */
	private String tips;
	
	/**
	 * 是否性格解析度 必做
	 */
	private boolean analysis;
	

	public QuestionnaireType getQuestionnaireType() {
		return questionnaireType;
	}

	public void setQuestionnaireType(QuestionnaireType questionnaireType) {
		this.questionnaireType = questionnaireType;
	}

//	public SelfType getSelfType() {
//		return selfType;
//	}
//
//	public void setSelfType(SelfType selfType) {
//		this.selfType = selfType;
//	}
//
//	public ManagementType getManagementType() {
//		return managementType;
//	}
//
//	public void setManagementType(ManagementType managementType) {
//		this.managementType = managementType;
//	}

	public SelfType getSelfType() {
		return selfType;
	}

	public void setSelfType(SelfType selfType) {
		this.selfType = selfType;
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

	public SelfManagementType getSelfManagementType() {
		return selfManagementType;
	}

	public void setSelfManagementType(SelfManagementType selfManagementType) {
		this.selfManagementType = selfManagementType;
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

	public Integer getMapMax() {
		return mapMax;
	}

	public void setMapMax(Integer mapMax) {
		this.mapMax = mapMax;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public int getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(int intervalTime) {
		this.intervalTime = intervalTime;
	}

	public GraphicsType getGraphicsType() {
		return graphicsType;
	}

	public void setGraphicsType(GraphicsType graphicsType) {
		this.graphicsType = graphicsType;
	}

	public SelfGroup getSelfGroup() {
		return selfGroup;
	}

	public void setSelfGroup(SelfGroup selfGroup) {
		this.selfGroup = selfGroup;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public boolean isAnalysis() {
		return analysis;
	}

	public void setAnalysis(boolean analysis) {
		this.analysis = analysis;
	}
}
