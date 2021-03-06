package com.qubaopen.survey.entity.vo;

import java.io.Serializable;

public class Content implements Serializable{

	private static final long serialVersionUID = -8609918509158631114L;

	/**
	 * id
	 */
	private long id;

	/**
	 * 内容
	 */
	private String cnt;

	/**
	 * 顺序
	 */
	private int order;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCnt() {
		return cnt;
	}

	public void setCnt(String cnt) {
		this.cnt = cnt;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}


}
