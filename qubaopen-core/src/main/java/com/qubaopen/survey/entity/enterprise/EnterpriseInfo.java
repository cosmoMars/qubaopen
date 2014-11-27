package com.qubaopen.survey.entity.enterprise;

import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.qubaopen.core.entity.AbstractBaseEntity;

public class EnterpriseInfo extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -4979217651297221804L;

	/**
	 * 企业
	 */
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	private Enterprise enterprise;

	/**
	 * 企业名
	 */
	private String name;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 证书
	 */
	private String recordPath;

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRecordPath() {
		return recordPath;
	}

	public void setRecordPath(String recordPath) {
		this.recordPath = recordPath;
	}

}
