package com.knowheart3.controller.exercise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.knowheart3.repository.exercise.ExerciseInfoRepository;
import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.topic.ExerciseInfo;

@RestController
@RequestMapping('exerciseInfo')
@SessionAttributes('currentUser')
public class ExerciseInfoController extends AbstractBaseController<ExerciseInfo, Long> {

	@Autowired
	ExerciseInfoRepository exerciseInfoRepository
	
	@Override
	MyRepository<ExerciseInfo, Long> getRepository() {
		exerciseInfoRepository
	}

	/**
	 * @param id
	 * @return
	 * 获取练习内容列表
	 */
	@RequestMapping(value = 'retrieveInfoList', method = RequestMethod.GET)
	retrieveInfoList(@RequestParam(required = false) Long id) {
		
		def list = exerciseInfoRepository.findAllByExerciseIdOrderByNumber(id)
		
		def data = []
		
		list.each {
			data << [
				'id' : it?.id,
				'name' : it?.name,
				'content' : it?.content,
				'number' : it?.number
			]
		}

		[
			'success' : '1',
			'size' : list ? list.size() : 0,
			'data' : data
		]		
	}
	
}
