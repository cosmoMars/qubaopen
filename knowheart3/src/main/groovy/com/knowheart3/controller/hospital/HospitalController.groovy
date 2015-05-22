package com.knowheart3.controller.hospital
import com.knowheart3.repository.base.AreaCodeRepository
import com.knowheart3.repository.booking.BookingRepository
import com.knowheart3.repository.hospital.HospitalInfoRepository
import com.knowheart3.repository.hospital.HospitalRepository
import com.knowheart3.service.AreaCodeService
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.doctor.TargetUser
import com.qubaopen.survey.entity.hospital.Hospital
import com.qubaopen.survey.entity.hospital.HospitalInfo
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping('hospital')
@SessionAttributes('currentUser')
public class HospitalController extends AbstractBaseController<Hospital, Long> {

	static Logger logger = LoggerFactory.getLogger(HospitalController.class)
	
	@Autowired
	HospitalRepository hospitalRepository
	
	@Autowired
	HospitalInfoRepository hospitalInfoRepository

    @Autowired
    BookingRepository bookingRepository

    @Autowired
    AreaCodeRepository areaCodeRepository

    @Autowired
    AreaCodeService areaCodeService

	@Override
	MyRepository<Hospital, Long> getRepository() {
		hospitalRepository
	}

	/**
	 * @param ids
	 * @param pageable
	 * @param user
	 * @return
	 * 诊所列表
	 */
	@RequestMapping(value = 'retrieveHospitalList', method = RequestMethod.POST)
	retrieveHospitalList(@RequestParam(required = false) String ids,
		@PageableDefault(page = 0, size = 20) Pageable pageable) {
		
		logger.trace '-- 诊所列表 --'


        def list = [], hospitals, data = []
        if (ids) {
            def strIds = ids.split(',')
            strIds.each {
                list << Long.valueOf(it.trim())
            }
            hospitals = hospitalRepository.findOtherHospital(list, pageable)
        } else {
            hospitals = hospitalRepository.findAll(
                    [
                            'hospitalInfo.loginStatus_equal': HospitalInfo.LoginStatus.Audited
                    ], pageable)
        }
        hospitals.each {
            data << [
                    'hospitalId'  : it.id,
                    'hospitalName': it.hospitalInfo?.name,
                    'appellation' : it.hospitalInfo?.appellation
            ]
        }
        [
                'success': '1',
                'data'   : data
        ]
    }
		
	@RequestMapping(value = 'retrieveHospitalDetail/{id}', method = RequestMethod.GET)
	retrieveHosptialDetial(@PathVariable long id) {
		
		logger.trace '-- 获取诊所详细 --'

        def hi = hospitalInfoRepository.findOne(id)
		
		[
                success       : '1',
                name          : hi.name,
                address       : hi.address,
                establishTime : hi.establishTime,
                phone         : hi.phone,
                urgentPhone   : hi.urgentPhone,
                qq            : hi.qq,
                introduce     : hi.introduce,
                wordsConsult  : hi.wordsConsult,
                minCharge     : hi.minCharge,
                maxCharge     : hi.maxCharge,
                records       : hi.hospitalDoctorRecords?.size(),
                video         : hi.video,
                faceToFace    : hi.faceToFace,
                hospitalAvatar: hi.hospitalAvatar,
                appellation   : hi.appellation

		]
	}

    /**
     * 筛选诊所
     * @param genreId
     * @param targetId
     * @param areaCode
     * @param faceToFace
     * @param video
     * @param ids
     * @param pageable
     */
    @RequestMapping(value = 'retrieveHospitalByFilter', method = RequestMethod.POST)
    retrieveHospitalByFilter(@RequestParam(required = false) Long genreId,
        @RequestParam(required = false) Long targetId,
        @RequestParam(required = false) String areaCode,
        @RequestParam(required = false) Boolean faceToFace,
        @RequestParam(required = false) Boolean video,
        @RequestParam(required = false) String ids,
        @PageableDefault(page = 0, size = 20) Pageable pageable) {

        def filters = [:], data = [], list = []
        filters.put('pageable', pageable)

        if (ids) {
            def strIds = ids.split(',')
            strIds.each {
                list << Long.valueOf(it.trim())
            }
            filters.put('ids', list)
        }
        if (genreId != null) {
            filters.put('genreId', genreId)
        }
        if (targetId != null) {
            filters.put('targetId', targetId)
        }
        if (areaCode != null) {
            def idsList = [],
                    code = areaCodeRepository.findByCode(areaCode)
            idsList.add(-1l)
            if (code) {
                idsList = areaCodeService.getAreaCodeIds(idsList, code)
            }
            filters.put('areaCode', idsList)
        }
        if (faceToFace != null) {
            filters.put('faceToFace', faceToFace)
        }
        if (video != null) {
            filters.put('video', video)
        }
        def hospitalInfos = hospitalInfoRepository.findByFilter(filters)
        hospitalInfos.each {
            def targets = it.targetUsers, userTargets = []
            for (TargetUser targetUser : targets) {
                if (userTargets.size() < 3) {
                    userTargets.add(targetUser.name)
                }

            }
            data << [
                    hospitalId       : it.id,
                    hospitalName     : it.name,
                    hospitalAddress  : it.address,
                    hospitalAvatar   : it.hospitalAvatar,
                    hospitalIntroduce: it.introduce,
                    faceToFace       : it.faceToFace,
                    video            : it.video,
                    minCharge        : it.minCharge,
                    maxCharge        : it.maxCharge,
                    appellation      : it.appellation,
                    targetUser       : StringUtils.join(userTargets, "  ")
            ]
        }
        def more = true
        if (hospitalInfos.size() < pageable.pageSize) {
            more = false
        }
        [
                'success': '1',
                'more'   : more,
                'data'   : data
        ]

    }

}
