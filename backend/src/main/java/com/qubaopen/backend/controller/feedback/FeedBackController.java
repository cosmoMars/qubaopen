package com.qubaopen.backend.controller.feedback;

import com.qubaopen.backend.repository.user.UserFeedBackRepository;
import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserFeedBack;
import com.qubaopen.survey.entity.user.UserFeedBackType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by mars on 15/5/19.
 */
@RestController
@RequestMapping("feedback")
public class FeedBackController extends AbstractBaseController<UserFeedBack, Long> {


    @Autowired
    private UserFeedBackRepository userFeedBackRepository;

    @Override
    protected MyRepository<UserFeedBack, Long> getRepository() {
        return userFeedBackRepository;
    }


    @RequestMapping(value = "retrieveFeedBack")
    private
    @ResponseBody
    Map<String, Object> retrieveFeedBack(@PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<UserFeedBack> all = userFeedBackRepository.findAll(pageable);

        List<UserFeedBack> userFeedBacks = all.getContent();

        List<Map<String, Object>> data = new ArrayList<>();
        for (UserFeedBack userFeedBack : userFeedBacks) {

            Map<String, Object> uMap = new HashMap<>();
            uMap.put("content", userFeedBack.getContent() == null ? "" : userFeedBack.getContent());
            uMap.put("title", userFeedBack.getTitle() == null ? "" : userFeedBack.getTitle());
            uMap.put("time", userFeedBack.getFeedBackTime() == null ? "" : userFeedBack.getFeedBackTime());
            uMap.put("contactMethod", userFeedBack.getContactMethod() == null ? "" : userFeedBack.getContactMethod());
            uMap.put("userId", userFeedBack.getUser() == null ? "" : userFeedBack.getUser().getId());
            uMap.put("type", userFeedBack.getType() == null ? "" : userFeedBack.getType());
            List<String> backType = new ArrayList<>();
            for (UserFeedBackType ufbt : userFeedBack.getBackTypes()) {
                backType.add(ufbt.getName());

            }
            uMap.put("feedBackType", StringUtils.join(backType, ","));

            data.add(uMap);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", "1");
        result.put("data", data);

        return result;
    }

}
