package com.knowheart3.controller.hospital

import com.knowheart3.repository.hospital.HospitalCaseRepository
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.hospital.HospitalCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

/**
 * Created by mars on 15/3/19.
 */
@RestController
@RequestMapping('hospitalCase')
@SessionAttributes('currentUser')
class HospitalCaseController extends AbstractBaseController<HospitalCase, Long> {

    @Autowired
    HospitalCaseRepository hospitalCaseRepository

    @Override
    MyRepository<HospitalCase, Long> getRepository() {
        hospitalCaseRepository
    }

    /**
     * 获取诊所案例
     * @param hospitalId
     * @param pageable
     * @return
     */
    @RequestMapping(value = 'retrieveHospitalCaseList', method = RequestMethod.POST)
    retrieveHospitalCaseList(@RequestParam(required = false) Long hospitalId,
            @PageableDefault(page = 0, size = 20, sort = 'createdDate', direction = Sort.Direction.DESC)
            Pageable pageable) {

        logger.trace('-- 获取诊所案例 --')

        def cases = hospitalCaseRepository.findAll(
            [
                'hospital.id_equal' : hospitalId
            ], pageable
        )

        def data = []
        cases.each {
            data << [
                'id' : it.id,
                'title' : it.title,
                'content' : it.content,
                'createTime' : it.createTime
            ]
        }

        [
            'success' : '1',
            'more' : cases.hasNext(),
            'data' : data,
        ]
    }

    /**
     *
     * @param id
     * 获取案例详情
     */
    @RequestMapping(value = 'retrieveHospitalCase/{id}', method = RequestMethod.GET)
    retrieveHospitalCase(@PathVariable Long id) {

        logger.trace('-- 获取案例详情 --')

        def doctorCase = hospitalCaseRepository.findOne(id)

        [
            'success' : '1',
            'id' : doctorCase.id,
            'title' : doctorCase.title,
            'content' : doctorCase.content,
            'createTime' : doctorCase.createTime
        ]

    }

}
