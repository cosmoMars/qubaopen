package com.qubaopen.survey.entity.mindmap;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "map_record")
@Audited
public class MapRecord extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -1998287891123862640L;

	private String name;

	private int value;

	@ManyToOne(fetch = FetchType.LAZY)
	private MapStatistics mapStatistics;

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

}
