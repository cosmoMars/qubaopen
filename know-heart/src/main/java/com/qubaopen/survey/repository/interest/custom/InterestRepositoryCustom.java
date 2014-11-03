package com.qubaopen.survey.repository.interest.custom;

import java.util.Map;

public interface InterestRepositoryCustom {

	Map<String, Object> findByFilters(Map<String, Object> filters);

	Map<String, Object> findByFilters2(Map<String, Object> filters);

}
