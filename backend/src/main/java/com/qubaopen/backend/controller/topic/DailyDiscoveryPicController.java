package com.qubaopen.backend.controller.topic;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qubaopen.backend.utils.UploadUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.backend.repository.topic.DailyDiscoveryPicRepository;
import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.topic.DailyDiscoveryPic;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("dailyDiscoveryPic")
public class DailyDiscoveryPicController  extends AbstractBaseController<DailyDiscoveryPic, Long> {
	
	@Autowired
	private DailyDiscoveryPicRepository dailyDiscoveryPicRepository;

    @Autowired
    UploadUtils uploadUtils;
	
	@Override
	protected MyRepository<DailyDiscoveryPic, Long> getRepository() {
		return dailyDiscoveryPicRepository;
	}
	
	/**
	 * 获取发现明日图片列表
	 * @return
	 */
	@RequestMapping(value = "retrieveDailyDiscoveryPics", method = RequestMethod.GET)
	private Object retrieveDailyDiscoveryPics(@PageableDefault(size = 20, page =  0) Pageable pageable) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		List<DailyDiscoveryPic> dailyDiscoveryPics = dailyDiscoveryPicRepository.findDailyDiscoveryPicsOrderByTimeDesc(pageable);
		
		for (DailyDiscoveryPic dailyDiscoveryPic : dailyDiscoveryPics) {
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", dailyDiscoveryPic.getId());
			map.put("startTime", dailyDiscoveryPic.getStartTime());
			map.put("name", dailyDiscoveryPic.getName());
			Date createdDate = null;
			if (null != dailyDiscoveryPic.getCreatedDate()) {
				DateTime sqlTime = dailyDiscoveryPic.getCreatedDate();
				createdDate = new Date(sqlTime.getMillis());
			}
			map.put("time", createdDate);
			list.add(map);
			
		}
		result.put("success", "1");
		result.put("list", list);
		return result;
	}
	
	/**
	 * @param id
	 * @param time
	 * @param name
	 * @return
	 * 新增"发现"明日图片
	 */
	@RequestMapping(value = "generateDailyDiscoveryPic", method = RequestMethod.POST)
	private Object generateDailyDiscoveryPic(@RequestParam(required = false) Long id,
			@RequestParam(required = false) String startTime,
			@RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) MultipartFile multipartFile) {
		
		DailyDiscoveryPic ddp = null;
		
		if (id != null) {
			ddp = dailyDiscoveryPicRepository.findOne(id);
		} else {
			ddp = new DailyDiscoveryPic();
		}

		ddp.setName(name);
		
		try {
			DailyDiscoveryPic exist = dailyDiscoveryPicRepository.findByTime(DateUtils.parseDate(startTime, "yyyy-MM-dd"));
			if (exist != null) {
				return  "该日期已经被使用";
			}
			ddp.setStartTime(DateUtils.parseDate(startTime, "yyyy-MM-dd"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
        if (null != multipartFile) {
            String uname = null;
            if (null == ddp.getId()) {
                uname = "pdd";
            } else {
                uname = "pdd" + ddp.getId().toString();
            }

            String picUrl = uploadUtils.uploadTo7niu(type, uname, multipartFile);
            ddp.setPicUrl(picUrl);
        }

		dailyDiscoveryPicRepository.save(ddp);
		
		return "{\"success\" : \"1\",  \"discoveryPicId\" : " + ddp.getId() + "}";
	}
	
}
