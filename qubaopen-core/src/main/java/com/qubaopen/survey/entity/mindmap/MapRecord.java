package com.qubaopen.survey.entity.mindmap;

import com.qubaopen.core.entity.AbstractBaseEntity;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "map_record")
@Audited
public class MapRecord extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -1998287891123862640L;

	private String name;

	private int value;

	private int naValue;

	@ManyToOne(fetch = FetchType.LAZY)
	private MapStatistics mapStatistics;

	/**
	 * 是否是准确按钮
	 */
	private Boolean accurary;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public MapStatistics getMapStatistics() {
		return mapStatistics;
	}

	public void setMapStatistics(MapStatistics mapStatistics) {
		this.mapStatistics = mapStatistics;
	}

	public int getNaValue() {
		return naValue;
	}

	public void setNaValue(int naValue) {
		this.naValue = naValue;
	}

	public Boolean getAccurary() {
		return accurary;
	}

	public void setAccurary(Boolean accurary) {
		this.accurary = accurary;
	}
}
