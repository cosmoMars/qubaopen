package com.qubaopen.survey.entity.mindmap;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.self.Self;
import com.qubaopen.survey.entity.self.SelfManagementType;
import com.qubaopen.survey.entity.self.SelfResultOption;
import com.qubaopen.survey.entity.user.User;

/**
 * 心理地图统计
 * 
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

	/**
	 * 问卷
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Self self;

	/**
	 * 单个结果
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private SelfResultOption selfResultOption;

	/**
	 * 分数
	 */
	private Integer score;

	/**
	 * 地图最大值
	 */
	private Integer mapMax;

	/**
	 * 自测管理类型
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "self_management_type_id")
	private SelfManagementType selfManagementType;

	/**
	 * 推荐优先级
	 */
	private int recommendedValue;

	/**
	 * 地图分数
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mapStatistics", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private Set<MapRecord> mapRecords;

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

	public SelfResultOption getSelfResultOption() {
		return selfResultOption;
	}

	public void setSelfResultOption(SelfResultOption selfResultOption) {
		this.selfResultOption = selfResultOption;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getMapMax() {
		return mapMax;
	}

	public int getRecommendedValue() {
		return recommendedValue;
	}

	public void setRecommendedValue(int recommendedValue) {
		this.recommendedValue = recommendedValue;
	}

	public void setMapMax(Integer mapMax) {
		this.mapMax = mapMax;
	}

	public SelfManagementType getSelfManagementType() {
		return selfManagementType;
	}

	public void setSelfManagementType(SelfManagementType selfManagementType) {
		this.selfManagementType = selfManagementType;
	}

	public Set<MapRecord> getMapRecords() {
		return mapRecords;
	}

	public void setMapRecords(Set<MapRecord> mapRecords) {
		this.mapRecords = mapRecords;
	}

}
