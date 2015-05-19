package com.qubaopen.backend.controller.topic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.qubaopen.backend.repository.exercise.ExerciseInfoRepository;
import com.qubaopen.backend.repository.exercise.ExerciseRepository;
import com.qubaopen.backend.utils.UploadUtils;
import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.topic.Exercise;
import com.qubaopen.survey.entity.topic.ExerciseInfo;

@RestController
@RequestMapping("exerciseInfo")
public class ExerciseInfoController extends AbstractBaseController<ExerciseInfo, Long> {

	@Autowired
	private ExerciseRepository exerciseRepository;

	@Autowired
	private ExerciseInfoRepository exerciseInfoRepository;
	

    @Value("${exercise_url}")
    private String exercise_url;

	@Override
	protected MyRepository<ExerciseInfo, Long> getRepository() {
		return exerciseInfoRepository;
	}


	/**
	 * 获取 子练习 列表
	 * 
	 * @param pageable
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "retrieveExerciseInfo", method = RequestMethod.GET)
	private Object retrieveExerciseInfo(
			@PageableDefault(page = 0, size = 20) Pageable pageable,
			@RequestParam long id) {

		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Exercise exercise = exerciseRepository.findOne(id);

		if (exercise == null) {
			result.put("success", "0");
			result.put("message", "该套练习不存在");
			return result;
		}

		List<ExerciseInfo> exerciseInfos = exerciseInfoRepository
				.findExerciseInfo(pageable, exercise);

		for (ExerciseInfo exerciseInfo : exerciseInfos) {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", exerciseInfo.getId());
			map.put("name", exerciseInfo.getName());
			map.put("content", exerciseInfo.getContent());
			map.put("exerciseId", exerciseInfo.getExercise().getId());
			map.put("number", exerciseInfo.getNumber());
			// map.put("createdDate", exerciseInfo.getCreatedDate().toDate());
			list.add(map);
		}

		result.put("success", "1");
		result.put("list", list);
		return result;
	}
	
	/**
	 * 新增/修改 子练习
	 * @param id
	 * @param name
	 * @param content
	 * @param number
	 * @param multipartFile
	 * @return
	 */
	@RequestMapping(value = "generateExerciseInfo", method = RequestMethod.POST)
	private Object generateExerciseInfo(@RequestParam(required = false) long exerciseId,
			@RequestParam(required = false) Long id,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String content,
			@RequestParam(required = false) String number,
			@RequestParam(required = false) MultipartFile multipartFile) {

		Map<String, Object> result = new HashMap<String, Object>();
		ExerciseInfo exerciseInfo = null;
		Exercise exercise= exerciseRepository.findOne(exerciseId);
		if(exercise==null){
			result.put("success", "0");
			result.put("message", "该套练习id不存在");
			return result;
		}
		
		if (id != null) {
			exerciseInfo = exerciseInfoRepository.findOne(id);
		} else {
			exerciseInfo = new ExerciseInfo();
		}
		exerciseInfo.setExercise(exercise);
		exerciseInfo.setName(name);
		exerciseInfo.setContent(content);
		exerciseInfo.setNumber(number);
		
		  if (null != multipartFile) {
	            String uname;
	            if (null == exerciseInfo.getId()) {
	                uname = exercise_url;
	            } else {
	                uname = exercise_url + exerciseInfo.getId();
	            }

	            String picUrl;
	            try {
	                picUrl = UploadUtils.uploadTo7niu(1, uname, multipartFile.getInputStream());
	            } catch (IOException e) {
	                throw new RuntimeException(e);
	            }
	            exerciseInfo.setPicUrl(picUrl);
	        }
		
		exerciseInfoRepository.save(exerciseInfo);

		result.put("success", "1");
		return result;
	}

}
