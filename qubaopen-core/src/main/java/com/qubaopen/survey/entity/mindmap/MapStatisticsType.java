package com.qubaopen.survey.entity.mindmap;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * 心里地图类型
 * @author mars
 */
@Entity
@Table(name = "map_statistics_type")
@Audited
public class MapStatisticsType extends AbstractPersistable<Long>{

	private static final long serialVersionUID = -177909067981874964L;
	/**
	 * 名称
	 */
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
