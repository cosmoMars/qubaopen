package com.qubaopen.doctor.controller.doctor
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.doctor.DoctorCaseRepository
import com.qubaopen.doctor.utils.UploadUtils
import com.qubaopen.survey.entity.AuditStatus
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.doctor.DoctorCase
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
@RequestMapping('doctorCase')
@SessionAttributes('currentDoctor')
class DoctorCaseController extends AbstractBaseController<DoctorCase, Long> {

    @Autowired
    DoctorCaseRepository doctorCaseRepository

    @Value('${case_url}')
    String case_url

    @Override
    MyRepository<DoctorCase, Long> getRepository() {
        doctorCaseRepository
    }


    /**
     * 获取文章列表
     * @param pageable
     * @param doctor
     * @return
     */
    @RequestMapping(value = 'retrieveCaseList', method = RequestMethod.GET)
    retrieveCaseList(
            @PageableDefault(page = 0, size = 20, sort = 'createdDate', direction = Sort.Direction.DESC) Pageable pageable,
            @ModelAttribute('currentDoctor') Doctor doctor) {

        def cases = doctorCaseRepository.findAll(pageable),
            data = []

        cases.each {
            data << [
                    'id'           : it.id,
                    'title'        : it.title,
                    'content'      : it.content,
                    'createdDate'  : it.createdDate.toDate(),
                    'status'       : it.status,
                    'refusalReason': it.refusalReason,
                    'url'          : it.picPath
            ]
        }

        [
                success: '1',
                data   : data,
                more   : cases.hasNext()
        ]

    }
    /**
     * 添加案例
     * @param title
     * @param content
     * @param file
     * @param doctor
     * @return
     */
    @RequestMapping(value = 'addCase', method = RequestMethod.POST, consumes = 'multipart/form-data')
    addCase(@RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) MultipartFile file,
            @ModelAttribute('currentDoctor') Doctor doctor) {

        if (!title) {
            return '{"success" : "1", "message" : "没有标题"}'
        }
        if (!content) {
            return '{"success" : "1", "message" : "没有内容"}'
        }
        def dCase = new DoctorCase(
                title: title,
                content: content,
                createTime: new Date(),
                status: AuditStatus.Auditing
        )
        if (file) {
            def name = case_url + doctor.id
            def path = UploadUtils.uploadTo7niu(1, name, file.inputStream)

            dCase.picPath = path
        }

        doctorCaseRepository.save(dCase)
        [
                'success': '1',
                'caseId' : dCase.id
        ]
    }

    /**
     * 修改案例
     * @param id
     * @param title
     * @param content
     * @param file
     * @param doctor
     * @return
     */
    @RequestMapping(value = 'modifyCase', method = RequestMethod.POST, consumes = 'multipart/form-data')
    modifyCase(@RequestParam long id,
               @RequestParam(required = false) String title,
               @RequestParam(required = false) String content,
               @RequestParam(required = false) MultipartFile file,
               @ModelAttribute('currentDoctor') Doctor doctor) {

        def dCase = doctorCaseRepository.findOne(id)

        if (title) {
            if (!StringUtils.equals(title.trim(), dCase.title.trim())) {
                dCase.status = AuditStatus.Auditing
                dCase.title = title
            }
        }
        if (content) {
            if (!StringUtils.equals(content.trim(), dCase.content.trim())) {
                dCase.status = AuditStatus.Auditing
                dCase.content = content
            }
        }

        if (file) {
            def name = case_url + doctor.id
            def path = UploadUtils.uploadTo7niu(1, name, file.inputStream)

            dCase.picPath = path
            dCase.status = AuditStatus.Auditing
        }

        doctorCaseRepository.save(dCase)
        '{"success" : "1"}'
    }

}
