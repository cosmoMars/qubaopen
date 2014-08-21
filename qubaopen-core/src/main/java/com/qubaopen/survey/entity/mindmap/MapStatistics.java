package com.qubaopen.survey.entity.mindmap;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.self.Self;
import com.qubaopen.survey.entity.self.SelfResultOption;
import com.qubaopen.survey.entity.user.User;

/**
 * 心理地图统计
 * @author mars
 *
 */
@Entity
@Table(name = "map_statistics")
@Audited
public class MapStatistics extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -3138111352912710215L;

	/**
	 * 用户
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	private Self self;

	/**
	 * 图形结果
	 */
	private String result;

	/**
	 * 单个结果
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private SelfResultOption selfResultOption;

	/**
	 * SDS, ABCD, PDP, MBTI
	 */
	@Enumerated
	private Type type;

	public enum Type {
		SDS, ABCD, PDP, MBTI
	}

	/**
	 * 分数
	 */
	private int score;

	/**
	 * 地图最大值
	 */
	private Integer mapMax;

	/**
	 * 自测类型
	 */
	private String managementType;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Self getSelf() {
		return self;
	}

	public void setSelf(Self self) {
		this.self = self;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public SelfResultOption getSelfResultOption() {
		return selfResultOption;
	}

	public void setSelfResultOption(SelfResultOption selfResultOption) {
		this.selfResultOption = selfResultOption;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Integer getMapMax() {
		return mapMax;
	}

	public String getManagementType() {
		return managementType;
	}

	public void setManagementType(String managementType) {
		this.managementType = managementType;
	}

	public void setMapMax(Integer mapMax) {
		this.mapMax = mapMax;
	}

}
