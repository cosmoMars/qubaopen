package com.knowheart3.controller.hospital
import com.knowheart3.repository.base.AreaCodeRepository
import com.knowheart3.repository.booking.BookingRepository
import com.knowheart3.repository.hospital.HospitalInfoRepository
import com.knowheart3.repository.hospital.HospitalRepository
import com.knowheart3.service.AreaCodeService
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.hospital.Hospital
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
			hospitals = hospitalRepository.findAll(pageable)
		}
		hospitals.each {
			data << [
				'hospitalId' : it?.id,
				'hospitalName' : it?.hospitalInfo?.name
			]
		}
		[
			'success' : '1',
			'data' : data
		]
	}
		
	@RequestMapping(value = 'retrieveHospitalDetail/{id}', method = RequestMethod.GET)
	retrieveHosptialDetial(@PathVariable long id) {
		
		logger.trace '-- 获取诊所详细 --'
		
		def hospital = hospitalInfoRepository.findOne(id)
		
		[
			'success' : '1',
			'name' : hospital?.name,
			'address' : hospital?.address,
			'establishTime' : hospital?.establishTime,
			'phone' : hospital?.phone,
			'urgentPhone' : hospital?.urgentPhone,
			'qq' : hospital?.qq,
			'introduce' : hospital?.introduce,
			'wordsConsult' : hospital?.wordsConsult,
			'minCharge' : hospital?.minCharge,
			'maxCharge' : hospital?.maxCharge,
			'records' : hospital?.hospitalDoctorRecords?.size()
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
            def code = areaCodeRepository.findByCode(areaCode),
                idsList = []
            idsList = areaCodeService.getAreaCodeIds(idsList, code)
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
            data << [
                'hospital' : it?.id,
                'hospitalName' : it?.name,
                'hospitalAddress' : it?.address,
                'hospitalIntroduce' : it?.introduce,
                'faceToFace' : it?.faceToFace,
                'video' : it?.video
            ]
        }
        def more = true
        if (hospitalInfos && hospitalInfos.size() < pageable.pageSize) {
            more = false
        }
        [
            'success' : '1',
            'more' : more,
            'data' : data
        ]

    }

}
