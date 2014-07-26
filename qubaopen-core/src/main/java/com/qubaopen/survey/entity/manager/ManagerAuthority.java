package com.qubaopen.survey.entity.manager;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Created by duel on 2014/6/24. 后台用户权限
 */

@Entity
@Table(name = "MANAGER_AUTHORITY")
@Audited
public class ManagerAuthority extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -4261601284016380235L;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 用户
	 */
	@ManyToMany
	@JoinTable(name = "manager_authority_relateion", joinColumns = @JoinColumn(name = "authority_id"), inverseJoinColumns = @JoinColumn(name = "manager_id"))
	private Set<Manager> managers;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Manager> getManagers() {
		return managers;
	}

	public void setManagers(Set<Manager> managers) {
		this.managers = managers;
	}

}
