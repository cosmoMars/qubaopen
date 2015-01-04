package com.qubaopen.survey.entity.self;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * 自测问卷组
 * 
 * @author mars
 */
@Entity
@Table(name = "self_group")
@Audited
public class SelfGroup extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -4522495130236449410L;

	/**
	 * 组名称
	 */
	private String name;

	/**
	 * 问卷
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "selfGroup", cascade = { CascadeType.REMOVE, CascadeType.PERSIST })
	private Set<Self> selfs;

	/**
	 * 图形类型
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "graphics_type_id")
	private GraphicsType graphicsType;

	/**
	 * 地图最大值
	 */
	private Integer mapMax;

	/**
	 * 推荐优先级
	 */
	private int recommendedValue;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 自测管理类型
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private SelfManagementType selfManagementType;

	/**
	 * 使用
	 */
	private boolean status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Self> getSelfs() {
		return selfs;
	}

	public void setSelfs(Set<Self> selfs) {
		this.selfs = selfs;
	}

	public GraphicsType getGraphicsType() {
		return graphicsType;
	}

	public void setGraphicsType(GraphicsType graphicsType) {
		this.graphicsType = graphicsType;
	}

	public Integer getMapMax() {
		return mapMax;
	}

	public void setMapMax(Integer mapMax) {
		this.mapMax = mapMax;
	}

	public int getRecommendedValue() {
		return recommendedValue;
	}

	public void setRecommendedValue(int recommendedValue) {
		this.recommendedValue = recommendedValue;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public SelfManagementType getSelfManagementType() {
		return selfManagementType;
	}

	public void setSelfManagementType(SelfManagementType selfManagementType) {
		this.selfManagementType = selfManagementType;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
