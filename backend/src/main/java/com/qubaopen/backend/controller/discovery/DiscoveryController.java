package com.qubaopen.backend.controller.discovery;

import com.qubaopen.backend.repository.discovery.DiscoveryRepository;
import com.qubaopen.backend.repository.exercise.ExerciseInfoRepository;
import com.qubaopen.backend.repository.exercise.ExerciseRepository;
import com.qubaopen.backend.repository.exercise.UserExerciseInfoLogRepository;
import com.qubaopen.backend.repository.exercise.UserExerciseRepository;
import com.qubaopen.backend.repository.self.SelfUserQuestionnaireRepository;
import com.qubaopen.backend.repository.topic.DailyDiscoveryRepository;
import com.qubaopen.backend.repository.user.UserRepository;
import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.topic.*;
import com.qubaopen.survey.entity.user.User;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mars on 15/5/19.
 */
public class DiscoveryController extends AbstractBaseController<DailyDiscovery, Long> {

    @Autowired
    private DiscoveryRepository discoveryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private UserExerciseRepository userExerciseRepository;

    @Autowired
    private UserExerciseInfoLogRepository userExerciseInfoLogRepository;

    @Autowired
    private ExerciseInfoRepository exerciseInfoRepository;

    @Autowired
    private DailyDiscoveryRepository dailyDiscoveryRepository;

    @Autowired
    private SelfUserQuestionnaireRepository selfUserQuestionnaireRepository;

    @Override
    protected MyRepository<DailyDiscovery, Long> getRepository() {
        return discoveryRepository;
    }

    /**
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    @RequestMapping(value = "retrieveDiscoveryContentForRoot", method = RequestMethod.GET)
    public Map<String, Object> retrieveDiscoveryContentForRoot(@PageableDefault(page = 0, size = 1, sort = "time", direction = Sort.Direction.DESC) Pageable pageable) {

        User user = userRepository.findOne(2l);

        Exercise exercise = null;
        int number = 0;
        UserExercise userExercise = null;
        boolean mession = false, isLogin = false;
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        int diff = -pageable.getPageNumber();
        c.add(Calendar.DATE, diff);
        Date today = c.getTime();

        if (null == user.getId()) {
            exercise = exerciseRepository.findRandomExercise();
            // 查找练习第一题
            number = 1;
        } else {
            isLogin = true;
            Map<String, Object> ueFilters = new HashMap<>();
            ueFilters.put("user_equal", user);
            ueFilters.put("complete_isFalse", null);
            userExercise = userExerciseRepository.findOneByFilters(ueFilters);

            UserExerciseInfoLog eLog = null;
            if (!DateUtils.isSameDay(now, today) && today.compareTo(now) == -1) {
                eLog = userExerciseInfoLogRepository.findLastLogByUserAndTime(user, today);
            }
            if (eLog != null) {
                ExerciseInfo exerciseInfo = eLog.getExerciseInfo();
                exercise = exerciseInfo.getExercise();
                number = Integer.valueOf(exerciseInfo.getNumber());
                mession = true;
            } else {
                // 查找未完成的用户练习纪录
                if (userExercise == null) {
                    // 查找刚完成的用户练习纪录
                    userExercise = userExerciseRepository.findCompleteExerciseByUser(user);
                    if (userExercise != null) {
                        mession = true;
                    } else {
                        exercise = exerciseRepository.findRandomExercise();
                        number = 1;
                    }

                } else {
                    // 存在用户练习，得到练习大题
                    if (DateUtils.isSameDay(userExercise.getTime(), now)) {
                        mession = true;
                        number = Integer.parseInt(userExercise.getNumber());
                    } else {
                        number = Integer.valueOf(userExercise.getNumber()) + 1;
                    }
                }
                if (userExercise != null) {
                    exercise = userExercise.getExercise();
                }
            }
        }
        int exerciseCount = exerciseInfoRepository.countByExercise(exercise);
        if (userExercise != null && userExercise.isComplete() == true) {
            number = exerciseCount;
        }
        DailyDiscovery dailyDiscovery = dailyDiscoveryRepository.findByTime(today);
        int size = dailyDiscoveryRepository.countByTime(today);
        boolean more = false;
        if (size > 0) {
            more = true;
        }
        boolean haveDone = false;
        if (dailyDiscovery != null && dailyDiscovery.getSelf() != null && null != user.getId()) {
            int count = selfUserQuestionnaireRepository.countBySelfAndUser(dailyDiscovery.getSelf(), user);
            if (count > 0) {
                haveDone = true;
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", "1");
        result.put("exerciseId", exercise.getId());
        result.put("exerciseName", exercise.getName());
        result.put("exerciseContent", exercise.getRemark());
        result.put("exerciseNumber", number);
        result.put("exerciseCount", exerciseCount);
        result.put("userExerciseId", userExercise == null ? 0 : userExercise.getId());
        result.put("exerciseComplete", userExercise == null ? "" : userExercise.getCompleteCount());
        result.put("messionComplete", mession);
        result.put("exercisePic", exercise.getUrl());
        result.put("selfId", dailyDiscovery == null ? "" : dailyDiscovery.getSelf().getId());
        result.put("selfName", dailyDiscovery == null ? "" : dailyDiscovery.getSelf().getTitle());
        result.put("selfContent", dailyDiscovery == null ? "" : dailyDiscovery.getSelf().getRemark());
        result.put("selfPic", dailyDiscovery == null ? "" : dailyDiscovery.getSelf().getPicPath());
        result.put("selfDone", haveDone);
        result.put("topicId", dailyDiscovery == null ? "" : dailyDiscovery.getTopic().getId());
        result.put("topicName", dailyDiscovery == null ? "" : dailyDiscovery.getTopic().getName());
        result.put("topicContent", dailyDiscovery == null ? "" : dailyDiscovery.getTopic().getContent());
        result.put("topicPic", dailyDiscovery == null ? "" : dailyDiscovery.getTopic().getPicUrl());
        result.put("isLogin", isLogin);
        result.put("time", dailyDiscovery == null ? "" : dailyDiscovery.getTime());
        result.put("moew", more);

        return result;
    }

}
