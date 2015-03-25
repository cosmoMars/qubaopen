package com.qubaopen.doctor.controller.doctor
import com.fasterxml.jackson.databind.node.ArrayNode
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.doctor.*
import com.qubaopen.doctor.utils.UploadUtils
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.doctor.DoctorInfo
import com.qubaopen.survey.entity.doctor.DoctorRecord
import org.apache.commons.lang3.time.DateUtils
import org.joda.time.DateTime
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

import javax.servlet.http.HttpServletRequest

import static com.qubaopen.doctor.utils.ValidateUtil.validateEmail
import static com.qubaopen.doctor.utils.ValidateUtil.validatePhone

@RestController
@RequestMapping('doctorInfo')
@SessionAttributes('currentDoctor')
public class DoctorInfoController extends AbstractBaseController<DoctorInfo, Long> {

    @Autowired
    DoctorRepository doctorRepository

    @Autowired
    DoctorInfoRepository doctorInfoRepository

    @Autowired
    DoctorIdCardBindRepository doctorIdCardBindRepository

    @Autowired
    DoctorAddressRepository doctorAddressRepository

    @Autowired
    DoctorRecordRepository doctorRecordRepository

    @Value('${doctor_record_url}')
    String doctor_record_url

    @Value('${doctor_url}')
    String doctor_url
    @Override
    protected MyRepository<DoctorInfo, Long> getRepository() {
        return doctorInfoRepository;
    }

    /**
     * @param doctor
     * @return
     * 获取医师信息
     */
    @RequestMapping(value = 'retrieveDoctorInfo', method = RequestMethod.GET)
    retrieveDoctorInfo(@ModelAttribute('currentDoctor') Doctor doctor) {

        logger.trace ' -- 获得用户个人信息 -- '

        def doctorInfo = doctorInfoRepository.findOne(doctor.id),
                doctorIdCardBind = doctorIdCardBindRepository.findOne(doctor.id)
//			address = doctorAddressRepository.findByDoctorAndUsed(doctor, true)

        [
                'success'       : '1',
                'doctorId'      : doctor.id,
                'phone'         : doctorInfo?.phone,
                'name'          : doctorIdCardBind?.userIDCard?.name,
                'infoName'      : doctorInfo?.name,
                'contactPhone'  : doctorInfo?.phone,
                'sex'           : doctorInfo?.sex?.ordinal(),
                'birthday'      : doctorInfo?.birthday,
                'experience'    : doctorInfo?.experience,
                'field'         : doctorInfo?.field,
                'qq'            : doctorInfo?.qq,
                'faceToFace'    : doctorInfo?.faceToFace,
                'video'         : doctorInfo?.video,
                'targetUser'    : doctorInfo?.targetUser,
                'genre'         : doctorInfo?.genre,
                'time'          : doctorInfo?.bookingTime,
                'introduce'     : doctorInfo?.introduce,
                'quick'         : doctorInfo.quick,
                'email'         : doctorInfo?.doctor?.email,
                'address'       : doctorInfo?.address,
                'idCard'        : doctorIdCardBind?.userIDCard?.IDCard,
                'recordPath'    : doctorInfo?.recordPath,
                'avatarPath'    : doctorInfo?.avatarPath,
                'loginStatus'   : doctorInfo?.loginStatus?.ordinal(),
                'refauslReason' : doctorInfo?.refusalReason,
                'commentConsult': doctorInfo?.commentConsult,
                'phoneConsult'  : doctorInfo?.phoneConsult,
                'onlineFee'     : doctorInfo?.onlineFee,
                'offlineFee'    : doctorInfo?.offlineFee
        ]
    }

    /**
     * @param name
     * @param sex
     * @param birthday
     * @param experience
     * @param field
     * @param qq
     * @param consultType
     * @param targetUser
     * @param genre
     * @param time
     * @param quick
     * @param introduce
     * @param avatar
     * @param record
     * @param doctor
     * @param request
     * @return
     * 修改医师信息
     */
    @Transactional
    @RequestMapping(value = 'modifyDoctorInfo', method = RequestMethod.POST, consumes = 'multipart/form-data')
    modifyDoctorInfo(@RequestParam(required = false) String infoName,
                     @RequestParam(required = false) String contactPhone,
                     @RequestParam(required = false) String email,
                     @RequestParam(required = false) Integer sex,
                     @RequestParam(required = false) String birthday,
                     @RequestParam(required = false) String experience,
                     @RequestParam(required = false) String field,
                     @RequestParam(required = false) String qq,
                     @RequestParam(required = false) Boolean faceToFace,
                     @RequestParam(required = false) Boolean video,
                     @RequestParam(required = false) String targetUser,
                     @RequestParam(required = false) String genre,
                     @RequestParam(required = false) String bookingTime,
                     @RequestParam(required = false) Boolean quick,
                     @RequestParam(required = false) String introduce,
                     @RequestParam(required = false) Boolean commentConsult,
                     @RequestParam(required = false) Boolean phoneConsult,
                     @RequestParam(required = false) String address,
                     @RequestParam(required = false) Double onlineFee,
                     @RequestParam(required = false) Double offlineFee,
                     @RequestParam(required = false) MultipartFile avatar,
                     @RequestParam(required = false) MultipartFile record,
                     @RequestParam(required = false) String times,
                     @RequestParam(required = false) String json,
                     @RequestParam(required = false) String recordJson,
                     @ModelAttribute('currentDoctor') Doctor doctor,
                     HttpServletRequest request) {

        def doctorInfo = doctorInfoRepository.findOne(doctor.id)

        def doctorInfoReview = new DoctorInfo()
        BeanUtils.copyProperties(doctorInfo, doctorInfoReview)


        def isChange = false
        if (email) {

            if (!validateEmail(email)) {
                return '{"success" : "0", "message": "err005"}'
            }
            if (doctor.email != email) {
                isChange = true
            }
            doctor.email = email
        }

        if (contactPhone) {
            if (!validatePhone(contactPhone)) {
                return '{"success" : "0", "message": "err003"}'
            }
            doctorInfo.phone = contactPhone
        }

        if (infoName) {
            doctorInfo.name = infoName
        }
        if (sex != null) {
            doctorInfo.sex = DoctorInfo.Sex.values()[sex]
        }
        if (birthday) {
            doctorInfo.birthday = DateUtils.parseDate(birthday, 'yyyy-MM-dd')
        }
        if (experience) {
            doctorInfo.experience = experience
        }
        if (field) {
            doctorInfo.field = field
        }
        if (qq) {
            doctorInfo.qq = qq
        }
        if (faceToFace != null) {
            doctorInfo.faceToFace = faceToFace
        }
        if (video != null) {
            doctorInfo.video = video
        }
        if (targetUser) {
            doctorInfo.targetUser = targetUser
        }
        if (genre) {
            doctorInfo.genre = genre
        }
        if (bookingTime) {
            doctorInfo.bookingTime = bookingTime
        }
        if (quick != null) {
            doctorInfo.quick = quick
        }
        if (introduce) {
            doctorInfo.introduce = introduce
        }
        if (commentConsult != null) {
            doctorInfo.commentConsult = commentConsult
        }
        if (phoneConsult != null) {
            doctorInfo.phoneConsult = phoneConsult
        }
        if (address != null) {
            doctorInfo.address = address
        }

        if (onlineFee != null) {
            doctorInfo.onlineFee = onlineFee
        }
        if (offlineFee != null) {
            doctorInfo.offlineFee = offlineFee
        }

        if (times != null) {
            println doctorInfo.bookingTime
            def resultTime = doctorInfo.bookingTime.split(','),
                    tempTimes = times.split('&')
            tempTimes.each { t ->
                def singleTime = t.split('#'),
                        day = singleTime[0] as int,
                        type = singleTime[1],
                        dayTime = singleTime[2].split(',')
                dayTime.each {
                    def index = it as int
                    if ('0' == type || '0'.equals(type)) {
                        resultTime[day - 1] = resultTime[day - 1].substring(0, index) + '0' + resultTime[day - 1].substring(index + 1)
                    } else if ('1' == type || '1'.equals(type)) {
                        resultTime[day - 1] = resultTime[day - 1].substring(0, index) + '1' + resultTime[day - 1].substring(index + 1)
                    }
                }
            }
            doctorInfo.bookingTime = resultTime.join(',')
        }

        if (json != null) {
            def resultModel = doctorInfo.bookingTime.split(',')

            def jsonNodes = objectMapper.readTree(json),
                    bookingModels = []

            jsonNodes.each {
                if (it.get('type') == null) {
                    return '{"success" : "0", "message" : "err905"}'// 没有预约时间类型
                }
                if (it.get('day') == null) {
                    return '{"success" : "0", "message" : "err910"}'// 没有指定时间
                }
                def index = it.get('day').asInt(),
                        type = it.get('type').asInt(),
                        hours = (ArrayNode) it.path('times')

                if (type == 0) {
                    hours.each { h ->
                        def idx = h.asInt()
                        resultModel[index - 1] = resultModel[index - 1].substring(0, idx) + '0' + resultModel[index - 1].substring(idx + 1)
                    }
				} else if (type == 1) {
					hours.each { h ->
						def idx = h.asInt()
						resultModel[index - 1] = resultModel[index - 1].substring(0, idx) + '1' + resultModel[index - 1].substring(idx + 1)
					}
				}
			}
			doctorInfo.bookingTime = resultModel.join(',')
		}
		
        if (avatar) {
            String aName = "$doctor_url$doctorInfo.id"
            // 上传图片
            def path = UploadUtils.uploadTo7niu(1, aName, avatar.inputStream)
            doctorInfo.avatarPath = path
            doctorInfo.lastModifiedDate = new DateTime()
        }
        def recordPath
        if (record) {
            def rName = "$doctor_record_url$doctorInfo.id"
            recordPath = UploadUtils.uploadTo7niu(2, rName, record.inputStream)
            doctorInfo.recordPath = recordPath
            doctorInfo.lastModifiedDate = new DateTime()
        }
		
		def dr = doctorRecordRepository.findOne(doctor.id)

        def drReview = new DoctorRecord()

        BeanUtils.copyProperties(dr, drReview)

		if (!dr) {
			dr = new DoctorRecord(
				id : doctor.id
			)
		}
		if (record) {
			dr.recordPath = recordPath
		}
		if (recordJson != null) {
			
			def jsonNode = objectMapper.readTree(recordJson)
			
			if (jsonNode.get('school') != null) {
				dr.school = jsonNode.get('school').asText()
			}
			if (jsonNode.get('profession') != null) {
				dr.profession = jsonNode.get('profession').asText()
			}
			if (jsonNode.get('degree') != null) {
				dr.degree = jsonNode.get('degree').asText()
			}
			if (jsonNode.get('educationalIntroduction') != null) {
				dr.educationalIntroduction = jsonNode.get('educationalIntroduction').asText()
			}
			if (jsonNode.get('course') != null) {
				dr.course = jsonNode.get('course').asText()
			}
			if (jsonNode.get('organization') != null) {
				dr.organization = jsonNode.get('organization').asText()
			}
			if (jsonNode.get('trainIntroduction') != null) {
				dr.trainIntroduction = jsonNode.get('trainIntroduction').asText()
			}
			if (jsonNode.get('supervise') != null) {
				dr.supervise = jsonNode.get('supervise').asText()
			}
			if (jsonNode.get('orientation') != null) {
				dr.orientation = jsonNode.get('orientation').asText()
			}
			if (jsonNode.get('superviseHour') != null) {
				dr.superviseHour = jsonNode.get('superviseHour').asInt()
			}
			if (jsonNode.get('contactMethod') != null) {
				dr.contactMethod = jsonNode.get('contactMethod').asText()
			}
			if (jsonNode.get('superviseIntroduction') != null) {
				dr.superviseIntroduction = jsonNode.get('superviseIntroduction').asText()
			}
			if (jsonNode.get('totalHour') != null) {
				dr.totalHour = jsonNode.get('totalHour').asInt()
			}
			if (jsonNode.get('selfIntroduction') != null) {
				dr.selfIntroduction = jsonNode.get('selfIntroduction').asText()
			}
			if (jsonNode.get('educationalStart') != null) {
				dr.educationalStart = DateUtils.parseDate(jsonNode.get('educationalStart').asText(), 'yyyy-MM-dd')
			}
			if (jsonNode.get('educationalEnd') != null) {
				dr.educationalEnd = DateUtils.parseDate(jsonNode.get('educationalEnd').asText(), 'yyyy-MM-dd')
			}
			if (jsonNode.get('trainStart') != null) {
				dr.trainStart = DateUtils.parseDate(jsonNode.get('trainStart').asText(), 'yyyy-MM-dd')
			}
			if (jsonNode.get('trainEnd') != null) {
				dr.trainEnd = DateUtils.parseDate(jsonNode.get('trainEnd').asText(), 'yyyy-MM-dd')
			}
			if (jsonNode.get('selfStart') != null) {
				dr.selfStart = DateUtils.parseDate(jsonNode.get('selfStart').asText(), 'yyyy-MM-dd')
			}
			if (jsonNode.get('selfEnd') != null) {
				dr.selfEnd = DateUtils.parseDate(jsonNode.get('selfEnd').asText(), 'yyyy-MM-dd')
			}
			
			if (dr.educationalStart && dr.educationalEnd && dr.educationalEnd.before(dr.educationalStart)) {
				return '{"success" : "0", "message" : "err920"}' // 学习结束时间不能小于学习开始时间
			}
			if (dr.trainStart && dr.trainEnd && dr.trainEnd.before(dr.trainStart)) {
				return '{"success" : "0", "message" : "err921"}' // 培续结束时间不能小于培训开始时间
			}
			if (dr.selfStart && dr.selfEnd && dr.selfEnd.before(dr.selfStart)) {
				return '{"success" : "0", "message" : "err922"}' // 自我学习结束时间不能小于自我学习开始时间
			}
			
		}
        if (!doctorInfo.equals(doctorInfoReview) || !dr.equals(drReview)) {
            isChange = true
        }

        if (doctorInfo.loginStatus == DoctorInfo.LoginStatus.Unaudited || (doctorInfo.loginStatus == DoctorInfo.LoginStatus.Refusal && isChange)) {
            doctorInfo.loginStatus = DoctorInfo.LoginStatus.Auditing
        }

        if (isChange && doctorInfo.loginStatus == DoctorInfo.LoginStatus.Audited) {
            doctorInfo.review = true
        }

		doctorRecordRepository.save(dr)
		doctorRepository.save(doctor)
		doctorInfoRepository.save(doctorInfo)
		'{"success": "1"}'
	}
}
	
