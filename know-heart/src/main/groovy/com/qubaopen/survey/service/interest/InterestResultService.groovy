package com.qubaopen.survey.service.interest;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qubaopen.survey.entity.interest.Interest;
import com.qubaopen.survey.entity.interest.InterestQuestionOption;
import com.qubaopen.survey.entity.user.User;
import com.qubaopen.survey.entity.vo.QuestionVo;

@Service
public class InterestResultService {

	@Transactional
	calculateScore(User user, Interest interest, List<InterestQuestionOption> questionOptions, List<QuestionVo> questionVos) {

	}

}
