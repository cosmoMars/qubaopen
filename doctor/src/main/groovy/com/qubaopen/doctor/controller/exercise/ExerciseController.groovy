package com.qubaopen.doctor.controller.exercise

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.exercise.ExerciseInfoRepository
import com.qubaopen.doctor.repository.exercise.ExerciseRepository
import com.qubaopen.doctor.utils.UploadUtils
import com.qubaopen.survey.entity.AuditStatus
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.topic.Exercise
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

/**
 * Created by mars on 15/4/21.
 */
@RestController
@RequestMapping('exercise')
@SessionAttributes('currentDoctor')
class ExerciseController extends AbstractBaseController<Exercise, Long> {

    @Autowired
    ExerciseRepository exerciseRepository

    @Autowired
    ExerciseInfoRepository exerciseInfoRepository

    @Value('${doctor_exercise_url}')
    String doctor_exercise_url

    @Override
    MyRepository<Exercise, Long> getRepository() {
        exerciseRepository
    }


    @RequestMapping(value = 'retrieveExerciseList', method = RequestMethod.GET)
    retrieveExerciseList(
            @PageableDefault(page = 0, size = 20, sort = 'createdDate', direction = Sort.Direction.DESC) Pageable pageable,
            @ModelAttribute('currentDoctor') Doctor doctor) {


        def exercises = exerciseRepository.findAll(
                [
                        doctor_equal: doctor
                ], pageable
        )

        def data = [], page = new PageRequest(0, 20, Sort.Direction.ASC, 'number')

        exercises.each {
            def infoData = []
            def eInfos = exerciseInfoRepository.findAll(
                    [
                            exercise_equal: it
                    ], page
            )
            eInfos.each { ei ->
                infoData << [
                        eId     : ei.id,
                        eName   : ei.name,
                        eContent: ei.content,
                        eNumber : ei.number,
                        ePicUrl : ei.picUrl
                ]
            }

            data << [
                    id    : it.id,
                    name  : it.name,
                    remark: it.remark,
                    url   : it.url,
                    eData : infoData
            ]
        }
        [
                success: '1',
                data   : data,
                more   : exercises.hasNext()
        ]
    }

    /**
     * 添加练习套题
     * @param name
     * @param remark
     * @param file
     * @param doctor
     */
    @RequestMapping(value = 'addExercise', method = RequestMethod.POST, consumes = 'multipart/form-data')
    addExercise(@RequestParam(required = false) String name,
                @RequestParam(required = false) String remark,
                @RequestParam(required = false) MultipartFile file,
                @ModelAttribute('currentDoctor') Doctor doctor) {

        def exercise = new Exercise(
                name: name,
                remark: remark,
                doctor: doctor,
                status: AuditStatus.Auditing
        )
        if (file) {
            def eName = doctor_exercise_url + doctor.id,
                url = UploadUtils.uploadTo7niu(1, eName, file.inputStream)
            exercise.url = url
        }

        exerciseRepository.save(exercise)
        [
                success: '1',
                id     : exercise.id
        ]
    }
}
