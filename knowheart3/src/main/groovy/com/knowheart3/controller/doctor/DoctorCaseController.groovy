package com.knowheart3.controller.doctor

import com.knowheart3.repository.doctor.DoctorCaseRepository
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.doctor.DoctorCase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

/**
 * Created by mars on 15/3/13.
 */
@RestController
@RequestMapping('doctorCase')
@SessionAttributes('currentUser')
public class DoctorCaseController extends AbstractBaseController<DoctorCase, Long> {

    Logger logger = LoggerFactory.getLogger(DoctorCaseController.class)

    @Autowired
    DoctorCaseRepository doctorCaseRepository

    @Override
    MyRepository getRepository() {
        doctorCaseRepository
    }

    /**
     *
     * @param doctorId
     * @param pageable
     * @return
     * 获取医师案例
     */
    @RequestMapping(value = 'retrieveDoctorCaseList', method = RequestMethod.POST)
    retrieveDoctorCaseList(@RequestParam(required = false) Long doctorId,
        @PageableDefault(page = 0, size = 20, sort = 'createdDate', direction = Sort.Direction.DESC)
        Pageable pageable) {

        logger.trace('-- 获取医师案例 --')

        def cases = doctorCaseRepository.findAll(
            [
                'doctor.id_equal' : doctorId
            ], pageable
        )

        def data = []
        cases.each {
            data << [
                'id' : it?.id,
                'title' : it?.title,
                'createTime' : it?.createTime
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
    @RequestMapping(value = 'retrieveDoctorCase/{id}', method = RequestMethod.GET)
    retrieveDoctorCase(@PathVariable Long id) {

        logger.trace('-- 获取案例详情 --')

        def doctorCase = doctorCaseRepository.findOne(id)

        [
            'success' : '1',
            'id' : doctorCase?.id,
            'title' : doctorCase?.title,
            'content' : doctorCase?.content,
            'createTime' : doctorCase?.createTime
        ]

    }
}
