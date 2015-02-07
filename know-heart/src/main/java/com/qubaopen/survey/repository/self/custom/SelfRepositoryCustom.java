package com.qubaopen.survey.repository.self.custom;

import java.util.List;

import com.qubaopen.survey.entity.self.Self;


public interface SelfRepositoryCustom {

	List<Self> findRandomSelfs(List<Self> exists, int limit);
	
	Self findSpecialSelf();
	
	Self findRandomSelfWithoutExist(long id);
	
	Self findRandomSelf();
}
