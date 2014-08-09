package com.qubaopen.survey.entity.base;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * 地区表
 *
 * @author mars
 */
@Entity
@Table(name = "area_code")
@Audited
public class AreaCode extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -6915387545416832379L;

	/**
	 * 名字
	 */
	private String name;

	/**
	 * 代码
	 */
	@Column(unique = true)
	private String code;

	/**
	 * 是否显示
	 */
	private Boolean display;

	@ManyToOne(fetch = FetchType.LAZY)
	private AreaCode parent;

	/**
	 * 孩纸
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
	private Set<AreaCode> children;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getDisplay() {
		return display;
	}

	public void setDisplay(Boolean display) {
		this.display = display;
	}

	public AreaCode getParent() {
		return parent;
	}

	public void setParent(AreaCode parent) {
		this.parent = parent;
	}

	public Set<AreaCode> getChildren() {
		return children;
	}

	public void setChildren(Set<AreaCode> children) {
		this.children = children;
	}

}
