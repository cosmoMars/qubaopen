package com.knowheart3.controller.hospital

import com.knowheart3.repository.hospital.HospitalArticleRepository
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.hospital.HospitalArticle
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
@RequestMapping('hospitalArticle')
@SessionAttributes('currentUser')
class HospitalArticleController extends AbstractBaseController<HospitalArticle, Long> {

    @Autowired
    HospitalArticleRepository hospitalArticleRepository

    @Override
    MyRepository<HospitalArticle, Long> getRepository() {
        hospitalArticleRepository
    }

    /**
     * 获取诊所文章列表
     * @param hospitalId
     * @param pageable
     * @return
     */
    @RequestMapping(value = 'retrieveHospitalArticleList', method = RequestMethod.POST)
    retrieveHospitalArticleList(@RequestParam(required = false) Long hospitalId,
            @PageableDefault(page = 0, size = 20, sort = 'createdDate', direction = Sort.Direction.DESC)
            Pageable pageable) {

        logger.trace('-- 获取诊所文章列表 --')

        def articles = hospitalArticleRepository.findAll(
            [
                'hospital.id_equal' : hospitalId
            ], pageable
        )

        def data = []
        articles.each {
            data << [
                'id' : it?.id,
                'title' : it?.title,
                'createTime' : it?.createTime
            ]
        }
        [
            'success' : '1',
            'more' : articles.hasNext(),
            'data' : data
        ]

    }

    /**
     * 获取文章信息
     * @param id
     */
    @RequestMapping(value = 'retrieveHospitalArticle/{id}', method = RequestMethod.GET)
    retrieveHospitalArticle(@PathVariable Long id) {

        logger.trace('-- 获取文章信息 --')

        def article = hospitalArticleRepository.findOne(id)

        [
            'success' : '1',
            'id' : article?.id,
            'title' : article?.title,
            'content' : article?.content,
            'createTime' : article?.createTime
        ]
    }
}

