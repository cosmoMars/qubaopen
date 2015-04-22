package com.qubaopen.doctor.controller.doctor

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.doctor.DoctorArticleRepository
import com.qubaopen.doctor.utils.UploadUtils
import com.qubaopen.survey.entity.AuditStatus
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.doctor.DoctorArticle
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

/**
 * Created by mars on 15/4/20.
 */
@RestController
@RequestMapping('doctorArticle')
@SessionAttributes('currentDoctor')
class DoctorArticleController extends AbstractBaseController<DoctorArticle, Long> {

    @Autowired
    DoctorArticleRepository doctorArticleRepository

    @Value('${article_url}')
    String article_url

    @Override
    MyRepository<DoctorArticle, Long> getRepository() {
        doctorArticleRepository
    }

    /**
     * 获取文章列表
     * @param pageable
     * @param doctor
     * @return
     */
    @RequestMapping(value = 'retrieveArticleList', method = RequestMethod.GET)
    retrieveArticleList(
            @PageableDefault(page = 0, size = 20, sort = 'createdDate', direction = Sort.Direction.DESC) Pageable pageable,
            @ModelAttribute('currentDoctor') Doctor doctor) {

        def articles = doctorArticleRepository.findAll(pageable),
            data = []

        articles.each {
            data << [
                    'id'           : it.id,
                    'title'        : it.title,
                    'content'      : it.content,
                    'createdDate'  : it.createdDate.toDate(),
                    'status'       : it.status.ordinal(),
                    'refusalReason': it.refusalReason,
                    'url'          : it.picPath
            ]
        }

        [
                success: '1',
                data   : data,
                more   : articles.hasNext()
        ]

    }
    /**
     * 添加文章
     * @param title
     * @param content
     * @param file
     * @param doctor
     * @return
     */
    @RequestMapping(value = 'addArticle', method = RequestMethod.POST, consumes = 'multipart/form-data')
    addArticle(@RequestParam(required = false) String title,
               @RequestParam(required = false) String content,
               @RequestParam(required = false) MultipartFile file,
               @ModelAttribute('currentDoctor') Doctor doctor) {

        if (!title) {
            return '{"success" : "1", "message" : "没有标题"}'
        }
        if (!content) {
            return '{"success" : "1", "message" : "没有内容"}'
        }
        def article = new DoctorArticle(
                title: title,
                content: content,
                createTime: new Date(),
                status: AuditStatus.Auditing
        )
        if (file) {
            def name = article_url + doctor.id
            def path = UploadUtils.uploadTo7niu(1, name, file.inputStream)

            article.picPath = path
        }

        doctorArticleRepository.save(article)
        [
                "success"  : "1",
                "articleId": article.id
        ]
    }

    /**
     * 修改文章
     * @param id
     * @param title
     * @param content
     * @param file
     * @param doctor
     * @return
     */
    @RequestMapping(value = 'modifyArticle', method = RequestMethod.POST, consumes = 'multipart/form-data')
    modifyArticle(@RequestParam long id,
                  @RequestParam(required = false) String title,
                  @RequestParam(required = false) String content,
                  @RequestParam(required = false) MultipartFile file,
                  @ModelAttribute('currentDoctor') Doctor doctor) {

        def article = doctorArticleRepository.findOne(id)

        if (title) {
            if (!StringUtils.equals(title.trim(), article.title.trim())) {
                article.status = AuditStatus.Auditing
                article.title = title
            }
        }
        if (content) {
            if (!StringUtils.equals(content.trim(), article.content.trim())) {
                article.status = AuditStatus.Auditing
                article.content = content
            }
        }

        if (file) {
            def name = article_url + doctor.id
            def path = UploadUtils.uploadTo7niu(1, name, file.inputStream)

            article.picPath = path
            article.status = AuditStatus.Auditing
        }

        doctorArticleRepository.save(article)
        '{"success" : "1"}'
    }
}
