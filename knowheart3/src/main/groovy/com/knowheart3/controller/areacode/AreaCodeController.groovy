package com.knowheart3.controller.areacode

import com.knowheart3.repository.base.AreaCodeRepository
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.base.AreaCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by mars on 15/4/8.
 */
@RestController
@RequestMapping('areaCode')
class AreaCodeController extends AbstractBaseController<AreaCode, Long> {

    @Autowired
    AreaCodeRepository areaCodeRepository

    @Override
    protected MyRepository<AreaCode, Long> getRepository() {
        return areaCodeRepository
    }

    @RequestMapping(value = "retrieveAreaCode", method = RequestMethod.GET)
    retrieveAreaCode() {
        List<AreaCode> areaCodes = areaCodeRepository.findExistDoctorAreaCode();

        [
                'success': '1',
                'list'   : areaCodes
        ]
    }
}
