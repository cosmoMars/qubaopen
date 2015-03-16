package com.knowheart3.controller.doctor

import com.knowheart3.repository.doctor.DoctorArticleRepository
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.doctor.DoctorArticle
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
 * Created by mars on 15/3/13.
 */
@RestController
@RequestMapping('doctorArticle')
@SessionAttributes('currentUser')
class DoctorArticleController extends AbstractBaseController<DoctorArticle, Long> {

    Logger logger = LoggerFactory.getLogger(DoctorArticleController.class)

    @Autowired
    DoctorArticleRepository doctorArticleRepository

    @Override
    MyRepository getRepository() {
        doctorArticleRepository
    }

    /**
     * 获取医师文章列表
     * @param doctorId
     * @param pageable
     */
    @RequestMapping(value = 'retrieveDoctorArticleList', method = RequestMethod.POST)
    retrieveDoctorArticleList(@RequestParam(required = false) Long doctorId,
        @PageableDefault(page = 0, size = 20, sort = 'createdDate', direction = Sort.Direction.DESC)
        Pageable pageable) {

        logger.trace('-- 获取医师文章列表 --')

        def articles = doctorArticleRepository.findAll(
            [
                'doctor.id_equal' : doctorId
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
    @RequestMapping(value = 'retrieveDoctorArticle/{id}', method = RequestMethod.GET)
    retrieveDoctorArticle(@PathVariable Long id) {

        logger.trace('-- 获取文章信息 --')

        def article = doctorArticleRepository.findOne(id)

        [
            'success' : '1',
            'id' : article?.id,
            'title' : article?.title,
            'content' : article?.content,
            'createTime' : article?.createTime
        ]
    }
}
