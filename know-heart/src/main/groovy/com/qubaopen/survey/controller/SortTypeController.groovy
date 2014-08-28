package com.qubaopen.survey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.SortType;
import com.qubaopen.survey.repository.SortTypeRepository;

@RestController
@RequestMapping("sortTypes")
public class SortTypeController extends AbstractBaseController<SortType, Long> {

	@Autowired
	SortTypeRepository sortTypeRepository;

	@Override
	protected MyRepository<SortType, Long> getRepository() {
		sortTypeRepository;
	}
	
	/**
	 * 获取排序方法名
	 * @return
	 */
	@RequestMapping(value = 'retrieveSortTypes', method = RequestMethod.GET)
	retrieveSortTypes(){
		
		logger.trace ' -- 获取排序方法名 -- '
		
		def sortTypes = sortTypeRepository.findAll(),
			data = []
		
		sortTypes.each {
			data << [
				'id' : it.id,
				'name' : it.name
			]
		}
		[
			'success' : '1',
			'message' : '成功',
			'data' : data
		]
	}
}
