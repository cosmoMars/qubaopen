package com.qubaopen.doctor.controller.hospital

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.hospital.HospitalDoctorRecordRepository
import com.qubaopen.doctor.utils.UploadUtils
import com.qubaopen.survey.entity.hospital.Hospital
import com.qubaopen.survey.entity.hospital.HospitalDoctorRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.multipart.MultipartFile

/**
 * Created by mars on 15/3/17.
 */
@RestController
@RequestMapping('hospitalDoctorRecord')
@SessionAttributes('currentHospital')
class HosptialDoctorRecordController extends AbstractBaseController<HospitalDoctorRecord, Long> {

    private static Logger logger = LoggerFactory.getLogger(HosptialDoctorRecordController.class);

    @Autowired
    HospitalDoctorRecordRepository hospitalDoctorRecordRepository

    @Autowired
    UploadUtils uploadUtils

    @Value('${hospital_doctor_url}')
    String hospital_doctor_url

    @Override
    protected MyRepository<HospitalDoctorRecord, Long> getRepository() {
        return hospitalDoctorRecordRepository
    }

    /**
     * 获取诊所医师图片
     * @param hospital
     * @return
     */
    @RequestMapping(value = 'retirevehospitalDoctorRecordList', method = RequestMethod.GET)
    retrieveHospitalDoctorRecordList(@ModelAttribute('currentHospital') Hospital hospital) {

        logger.trace('-- 获取诊所医师图片 --')

        def hdRecord = hospitalDoctorRecordRepository.findAll([
            'hospitalInfo.id_equal' : hospital.id
        ])

        def data = []

        hdRecord.each {
            if (it.doctorRecordPath) {
                def url = uploadUtils.retrievePriavteUrl(it.doctorRecordPath)
                data << [
                    'id' : it.id,
                    'url' : url
                ]
            }

        }

        [
            'success' : '1',
            'data' : data
        ]
    }

    /**
     * 修改诊所医师证明
     * @param id
     * @param record
     * @param hospital
     * @return
     */
    @RequestMapping(value = 'modifyHospitalDoctorRecord', method = RequestMethod.POST, consumes = 'multipart/form-data')
    modifyHospitalDoctorRecord(@RequestParam(required = false) Long id,
        @RequestParam(required = false) MultipartFile record,
        @ModelAttribute('currentHospital') Hospital hospital) {

        def hdRecord = hospitalDoctorRecordRepository.findOne(id),
            hdName = "$hospital_doctor_url$hospital.id"
        def url = uploadUtils.uploadTo7niu(2, hdName, record.inputStream)
        hdRecord.doctorRecordPath = url
        hospitalDoctorRecordRepository.save(url)
        '{"success" : "1"}'
    }
}
