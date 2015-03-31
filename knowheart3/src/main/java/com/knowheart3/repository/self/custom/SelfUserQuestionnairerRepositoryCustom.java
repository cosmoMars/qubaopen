package com.knowheart3.repository.self.custom;

import com.knowheart3.vo.SelfResultVo;
import com.qubaopen.survey.entity.user.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by mars on 15/3/30.
 */
public interface SelfUserQuestionnairerRepositoryCustom {

    List<Long> findGroupId(User user, Pageable pageable);

    List<SelfResultVo> findSelfResultVo(User user, List<Long> groupIds);

}
