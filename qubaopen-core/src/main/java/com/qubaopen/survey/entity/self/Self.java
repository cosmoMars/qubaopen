package com.qubaopen.survey.entity.self;

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

/**
 * @author mars 自测
 */
@Entity
@Table(name = "self")
@Audited
public class Self extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 4650668582375674301L;

	/**
	 * 问卷类型 SDS, PDP
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "self_type_id")
	private SelfType selfType;

	/**
	 * 问卷类型  乱序 DISOREDER, 得分 SORCE, 乱序得分 DISORDERSCORE
	 */
	@Enumerated
	private Type type;

	private enum Type {
		DISOREDER, SORCE, DISORDERSCORE
	}

	/**
	 * 标题
	 */
	private String titile;

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

	public SelfType getSelfType() {
		return selfType;
	}

	public void setSelfType(SelfType selfType) {
		this.selfType = selfType;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getTitile() {
		return titile;
	}

	public void setTitile(String titile) {
		this.titile = titile;
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
