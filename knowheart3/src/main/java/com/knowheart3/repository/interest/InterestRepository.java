package com.knowheart3.repository.interest;

import com.knowheart3.repository.interest.custom.InterestRepositoryCustom;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.interest.Interest;

public interface InterestRepository extends MyRepository<Interest, Long>, InterestRepositoryCustom {

}
