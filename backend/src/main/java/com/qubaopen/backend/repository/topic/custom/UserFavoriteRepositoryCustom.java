package com.qubaopen.backend.repository.topic.custom;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.qubaopen.backend.vo.FavoriteVo;

public interface UserFavoriteRepositoryCustom {
	
	List<FavoriteVo> findTopicVos(Pageable pageable);
	
	List<FavoriteVo> findSelfVos(Pageable pageable);
}
