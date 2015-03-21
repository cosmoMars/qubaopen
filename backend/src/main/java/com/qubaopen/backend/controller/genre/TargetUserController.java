package com.qubaopen.backend.controller.genre;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.backend.repository.genre.TargetUserRepository;
import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.TargetUser;

@RestController
@RequestMapping("targetUser")
public class TargetUserController extends AbstractBaseController<TargetUser, Long> {

	private static Logger logger = LoggerFactory
			.getLogger(TargetUserController.class);

	@Autowired
	private TargetUserRepository targetUserRepository;

	@Override
	protected MyRepository<TargetUser, Long> getRepository() {
		return targetUserRepository;
	}

	/**
	 * @return 获取咨询对象列表
	 */
	@RequestMapping(value = "retrieveTargetUser", method = RequestMethod.GET)
	Map<String, Object> retrieveTargetUser() {

		logger.trace("-- 获取流派 --");

		Map<String, Object> map = new HashMap<String, Object>();
		List<TargetUser> targetUsers = targetUserRepository.findAll();

		map.put("success", "1");
		map.put("targetUsers", targetUsers);

		return map;
	}

	/**
	 * @param id
	 * @param name
	 * @return 新增/修改 咨询对象
	 */
	@RequestMapping(value = "generateTargetUser", method = RequestMethod.POST)
	private Object generateTargetUser(
			@RequestParam(required = false) Long id,
			@RequestParam(required = false) String name) {

		TargetUser targetUser = null;

		if (id != null) {
			targetUser = targetUserRepository.findOne(id);
		} else {
			targetUser = new TargetUser();
		}

		targetUser.setName(name);

		targetUserRepository.save(targetUser);

		return "{\"success\" : \"1\",  \"targetUserId\" : " + targetUser.getId() + "}";
	}

}
