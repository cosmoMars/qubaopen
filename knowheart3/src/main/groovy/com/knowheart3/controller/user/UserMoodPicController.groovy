package com.knowheart3.controller.user
import com.knowheart3.repository.user.UserMoodPicRepository
import com.knowheart3.utils.UploadUtils
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.user.UserMoodPic
import org.apache.commons.lang3.time.DateFormatUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
/**
 * Created by mars on 15/4/8.
 */
@RestController
@RequestMapping('userMoodPic')
class UserMoodPicController extends AbstractBaseController<UserMoodPic, Long> {

    @Autowired
    UserMoodPicRepository userMoodPicRepository

    @Override
    protected MyRepository<UserMoodPic, Long> getRepository() {
        return userMoodPicRepository
    }

    @RequestMapping(value = 'uploadUserMoodPic', method = RequestMethod.POST, consumes = 'multipart/form-data')
    uploadUserMoodPic(@RequestParam int type,
                      @RequestParam(required = false) MultipartFile file) {

        def moodType = UserMoodPic.Type.values()[type],
            moodPic = userMoodPicRepository.findByType(moodType)
        def name = "um/${type}um${DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")}",
            path = UploadUtils.uploadTo7niu(1, name, file.inputStream)
        if (moodPic) {
            moodPic.path = path
        } else {
            moodPic = new UserMoodPic(
                    type: moodType,
                    path: path
            )
        }
        userMoodPicRepository.save(moodPic)
        '{"success" : "1"}'
    }
}
