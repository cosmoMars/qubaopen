package com.qubaopen.backend.controller.feedback;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserFeedBack;

/**
 * Created by mars on 15/5/19.
 */
public class FeedBackController extends AbstractBaseController<UserFeedBack, Long> {


    @Override
    protected MyRepository<UserFeedBack, Long> getRepository() {
        return null;
    }
}
   