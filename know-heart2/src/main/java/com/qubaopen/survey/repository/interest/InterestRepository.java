package com.qubaopen.survey.repository.interest;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.interest.Interest;
import com.qubaopen.survey.repository.interest.custom.InterestRepositoryCustom;

public interface InterestRepository extends MyRepository<Interest, Long>, InterestRepositoryCustom {

}
