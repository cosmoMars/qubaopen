package com.knowheart3.repository.self;

import com.knowheart3.repository.self.custom.SelfGroupRepositoryCustom;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SelfGroupRepository extends MyRepository<SelfGroup, Long>, SelfGroupRepositoryCustom {

    @Query("from SelfGroup sg where sg.id in (:ids)")
    List<SelfGroup> findByGroupIds(@Param("ids") List<Long> ids);

}
