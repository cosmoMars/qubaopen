package com.knowheart3.service;

import com.qubaopen.survey.entity.base.AreaCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaCodeService {

	private List<Long> getAreaCodeIds(List<Long> ids, AreaCode parent) {
		ids.add(parent.getId());
		if (parent.getChildren() != null && parent.getChildren().size() > 0) {
			for (AreaCode code : parent.getChildren()) {
				getAreaCodeIds(ids, code);
			}
		}
		return ids;
	}
}
