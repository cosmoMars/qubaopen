package com.knowheart3.controller.self

import com.knowheart3.controller.FileUtils
import com.knowheart3.repository.discovery.DailyDiscoveryRepository
import com.knowheart3.repository.favorite.UserFavoriteRepository
import com.knowheart3.repository.interest.InterestUserQuestionnaireRepository
import com.knowheart3.repository.self.SelfGroupRepository
import com.knowheart3.repository.self.SelfRepository
import com.knowheart3.repository.self.SelfUserQuestionnaireRepository
import com.knowheart3.repository.user.UserInfoRepository
import com.knowheart3.service.self.SelfService
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.self.Self
import com.qubaopen.survey.entity.user.User
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateFormatUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping('selfs')
@SessionAttributes('currentUser')
public class SelfController extends AbstractBaseController<Self, Long> {

    @Autowired
    SelfRepository selfRepository

    @Autowired
    SelfUserQuestionnaireRepository selfUserQuestionnaireRepository

    @Autowired
    SelfService selfService

    @Autowired
    FileUtils fileUtils

    @Autowired
    SelfGroupRepository selfGroupRepository

    @Autowired
    UserInfoRepository userInfoRepository

    @Autowired
    InterestUserQuestionnaireRepository interestUserQuestionnaireRepository

    @Autowired
    UserFavoriteRepository userFavoriteRepository

    @Autowired
    DailyDiscoveryRepository dailyDiscoveryRepository


    @Override
    protected MyRepository<Self, Long> getRepository() {
        selfRepository
    }

/**
 * @param id
 * @param user
 * @return
 * 获取自测信息
 */
    @RequestMapping(value = 'retrieveSelfContent', method = RequestMethod.POST)
    retrieveSelfContent(@RequestParam(required = false) Long id,
                        @ModelAttribute('currentUser') User user) {

        def favorite
        def self = selfRepository.findOne(id)
        if (null != user.id) {
            favorite = userFavoriteRepository.findOneByFilters([
                    user_equal: user,
                    self_equal: self
            ])
        }

        def isFavorite = false
        if (favorite != null) {
            isFavorite = true
        }

        [
                'success'         : '1',
                'id'              : self.id,
                'name'            : self?.title,
                'guidanceSentence': self?.guidanceSentence,
                'tips'            : self?.tips,
                'content'         : self?.remark,
                'isFavorite'      : isFavorite
        ]
    }

    /**
     * 获取用户自测问卷
     * @param userId
     * @return
     */
    @RequestMapping(value = 'retrieveSelf', method = RequestMethod.GET)
    retrieveSelf(@RequestParam(required = false) Boolean refresh, @ModelAttribute('currentUser') User user) {

        logger.trace ' -- 获取用户自测问卷 -- '

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }

        if (!refresh) {
            refresh = false
        }
        def resultSelfs = selfService.retrieveSelf(user, refresh),
            data = []

        resultSelfs.each {
            def self = [
                    'selfId'        : it?.id,
                    'managementType': it?.selfManagementType?.id,
                    'title'         : it?.title,
                    'version'       : it?.version
            ]
            data << self
        }
        [
                'success': '1',
                'message': '成功',
                'data'   : data
        ]
    }

    /**
     * 获取问卷文字信息
     * @param selfId
     * @return
     */
    @RequestMapping(value = 'retrieveSelfScript/{selfId}', method = RequestMethod.GET)
    retrieveSelfScript(@PathVariable long selfId) {
        def self = selfRepository.findOne(selfId)

        [
                'success'         : '1',
                'message'         : '成功',
                'title'           : self?.title,
                'guidanceSentence': self?.guidanceSentence,
                'tips'            : self?.tips,
                'remark'          : self?.remark
        ]
    }

    /**
     * 计算自测结果选项
     * @param userId
     * @param selfId
     * @param questionJson
     * @return
     */
    @RequestMapping(value = 'calculateSelfResult', method = RequestMethod.POST)
    calculateSelfReslut(@RequestParam(required = false) long selfId,
                        @RequestParam(required = false) String questionJson,
                        @RequestParam(required = false) Boolean refresh,
                        @ModelAttribute('currentUser') User user
    ) {

        logger.trace ' -- 计算自测结果选项 -- '

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }

        if (StringUtils.isEmpty(questionJson)) {
            return '{"success" : "0", "message" : "err600"}'
        }

        if (refresh == null) {
            refresh = true
        }

        def result = selfService.calculateSelfReslut(user.id, selfId, questionJson, refresh)

        if (result instanceof String) {
            return result
        }

        if (!result) {
            return '{"success" : "0", "message" : "err601"}'
        }

        def userInfo = userInfoRepository.findOne(user.id),
            gradeHint = false
        if (!userInfo.evaluate) {
            def selfCount = selfUserQuestionnaireRepository.countByUser(user),
                interestCount = interestUserQuestionnaireRepository.countByUser(user)
            def allCount = selfCount + interestCount

            if (allCount == 10 || allCount == 30 || allCount == 100) {
                gradeHint = true
            }
        }

        def questionnaire = selfUserQuestionnaireRepository.findByUserAndSelfAndUsed(user, new Self(id: selfId), true)

        def isFavorite = false,
            userFavorite = userFavoriteRepository.findBySelfAndUser(new Self(id: selfId), user)
        if (userFavorite) {
            isFavorite = true
        }
        [
                'success'     : '1',
                'message'     : '成功',
                'id'          : result?.id,
                'qId'         : questionnaire?.accuracy == null ? questionnaire?.id : 0,
                'resultTitle' : result?.selfResult?.title,
                'content'     : result?.content,
                'optionTitle' : result?.title,
                'resultRemark': result?.selfResult?.remark,
                'optionNum'   : result?.resultNum,
                'gradeHint'   : gradeHint,
                'isFavorite'  : isFavorite
        ]
    }

    /**
     * 上传图片
     * @param pic
     * @param selfId
     * @param request
     * @return
     */
    @RequestMapping(value = 'uploadSelfPic', method = RequestMethod.POST, consumes = 'multipart/form-data')
    uploadAvatar(
            @RequestParam(required = false) MultipartFile pic, @RequestParam long selfId, HttpServletRequest request) {

        logger.trace(' -- 上传头像 -- ')
        def self = selfRepository.findOne(selfId)

        if (pic) {

            def filename = "${selfId}_${DateFormatUtils.format(new Date(), 'yyyyMMdd-HHmmss')}.png",
                picPath = "${request.getServletContext().getRealPath('/')}pic/$filename"

            fileUtils.saveFile(pic.bytes, picPath)

            self.picPath = "/pic/$filename"
            selfRepository.save(self)
            return '{"success": "1"}'
        }
        '{"success": "0", "message" : "err102"}'
    }

    /**
     * 获取自测问卷
     * @param typeId
     * @return
     */
    @RequestMapping(value = 'retrieveSelfByType/{typeId}', method = RequestMethod.GET)
    retrieveSelfByType(@PathVariable Long typeId, @ModelAttribute('currentUser') User user) {

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }
        /*def selfs = selfRepository.findAll(
            [
                'selfManagementType.id_equal' : typeId,
                'status_equal' : Self.Status.ONLINE
            ]
        )

        def data = [],
            specialSelf = selfRepository.findByMaxRecommendedValue()

        def selfs = selfService.retrieveSelfByTypeId(user, typeId)
        selfs.each {
            if (it.id != specialSelf.id) {
                data <<	[
                    'selfId' : it?.id,
                    'managementType' : it?.selfManagementType?.id,
                    'title' : it?.title
                ]
            }
        }*/

        def specialSelf = selfRepository.findByMaxRecommendedValue()

        def todayQuestionnaires = selfUserQuestionnaireRepository.findByTimeAndTypeIdWithOutSpecial(DateFormatUtils.format(new Date(), 'yyyy-MM-dd'), typeId, specialSelf, user)

//		if (todayQuestionnaires?.size() >= 2) {
//			return '{"success" : "0", "message" : "亲，该类别每日2题上线你已经答满咯～"}' // err603
//		}

        def data = [],
            remainCount = 2 - todayQuestionnaires?.size() ?: 0
        def selfs = selfService.retrieveSelfByTypeId(user, typeId, specialSelf)
        selfs.each {
            data << [
                    'selfId'        : it?.id,
                    'managementType': it?.selfManagementType?.id,
                    'title'         : it?.title,
                    'version'       : it?.version
            ]
        }
        [
                'success'    : '1',
                'message'    : '成功',
                'data'       : data,
                'remainCount': remainCount
        ]
    }

    @RequestMapping(value = 'findSelfUserQuestionnaire', method = RequestMethod.GET)
    findSelfUserQuestionnaire(@ModelAttribute('currentUser') User user) {

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }

        selfUserQuestionnaireRepository.findRecentQuestionnarie(user)
    }

    /**
     * 更新性格解析度
     * @param user
     * @return
     */
    @RequestMapping(value = 'refreshAnalysis', method = RequestMethod.GET)
    refreshAnalysis(@ModelAttribute('currentUser') User user) {

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }
        selfService.calcUserAnalysisRadio(user);
    }

    /**
     * 更新心理指数
     * @param user
     * @return
     */
    @RequestMapping(value = 'refreshMentalStatus', method = RequestMethod.GET)
    refreshMentalStatus(@ModelAttribute('currentUser') User user) {

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }
        selfService.calcUserMentalStatus(user);
    }

    /**
     * 获取自测问卷组问卷
     * @param groupId
     * @return
     */
    @RequestMapping(value = 'retrieveSelfByGroupId/{groupId}', method = RequestMethod.GET)
    retrieveSelfByGroupId(@PathVariable Long groupId) {

        logger.trace ' -- 查询自测组问卷 -- '

        def selfGroup = selfGroupRepository.findOne(groupId),
            data = []
        selfGroup.selfs.each {
            def self = [
                    'selfId'        : it?.id,
                    'managementType': it?.selfManagementType?.id,
                    'title'         : it?.title
            ]
            data << self
        }
        [
                'success': '1',
                'message': '成功',
                'data'   : data
        ]
    }

    /**
     * 获取自测历史
     * @param pageable
     * @param user
     * @return
     */
    @RequestMapping(value = 'retrieveSelfResult', method = RequestMethod.POST)
    retrieveSelfResult(
            @PageableDefault(page = 0, size = 20, sort = 'time', direction = Sort.Direction.DESC) Pageable pageable,
            @ModelAttribute('currentUser') User user) {

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }

//        def count = selfUserQuestionnaireRepository.countBySelfAndUser(new Self(id : selfId), user)
        def questionnaires = selfUserQuestionnaireRepository.findByUserAndUsed(user, true, pageable)

        def data = [], more = true
        if (questionnaires) {
            questionnaires.each {
                data << [
                        'id'          : it.selfResultOption?.id,
                        'resultTitle' : it.selfResultOption?.selfResult?.title,
                        'content'     : it.selfResultOption?.content,
                        'optionTitle' : it.selfResultOption?.title,
                        'resultRemark': it.selfResultOption?.selfResult?.remark,
                        'optionNum'   : it.selfResultOption?.resultNum
                ]
            }
        }
        if (questionnaires.size() < pageable.pageSize) {
            more = false
        }

        [
                'success': '1',
                'data'   : data,
                'more'   : more
        ]

    }

    /**
     * 根据selfid查找用户答案
     * @param id
     * @param user
     * @return
     */
    @RequestMapping(value = 'retrieveResultBySelfId/{id}', method = RequestMethod.GET)
    retrieveResultBySelfId(@PathVariable long id,
                           @ModelAttribute('currentUser') User user) {

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }
        def userFavorite
        def questionnaire = selfUserQuestionnaireRepository.findByUserAndSelfAndUsed(user, new Self(id: id), true),
            isFavorite = false
        if (questionnaire) {
            userFavorite = userFavoriteRepository.findBySelfAndUser(questionnaire.self, user)
            if (userFavorite) {
                isFavorite = true
            }
        }

        [
                'success'     : '1',
                'qId'         : questionnaire?.accuracy == null ? questionnaire?.id : 0,
                'id'          : questionnaire?.selfResultOption?.id,
                'resultTitle' : questionnaire?.selfResultOption?.selfResult?.title,
                'content'     : questionnaire?.selfResultOption?.content,
                'optionTitle' : questionnaire?.selfResultOption?.title,
                'resultRemark': questionnaire?.selfResultOption?.selfResult?.remark,
                'optionNum'   : questionnaire?.selfResultOption?.resultNum,
                'isFavorite'  : isFavorite
        ]
    }

    /**
     * 获取历史
     * @param pageable
     * @param user
     * @return
     */
    @RequestMapping(value = 'retrieveHistory', method = RequestMethod.GET)
    retrieveHistory(
            @PageableDefault(page = 0, size = 20, sort = 'time', direction = Sort.Direction.DESC) Pageable pageable,
            @ModelAttribute('currentUser') User user) {

        // 查找groupids
        def groupIds = selfUserQuestionnaireRepository.findGroupId(user, pageable)
        def more = true, list = []

        if (groupIds.size() > 0) {

            if (groupIds.size() < pageable.getPageSize()) {
                more = false
            }
            def groups = selfGroupRepository.findByGroupIds(groupIds)
            // 查找答案
            def selfResultVos = selfUserQuestionnaireRepository.findSelfResultVo(user, groupIds)


            def map = [:]
            selfResultVos.each {
                if (map.get(it.groupName)) {
                    map.get(it.groupName) << it
                } else {
                    def mList = []
                    mList << it
                    map.put(it.groupName, mList)
                }
            }

            groups.each { g ->
                def groupData = []
                g.selfs.each { s ->
                    def exist = selfResultVos.find { vo ->
                        s.id == Long.valueOf(vo.selfId)
                    }
                    if (exist) {
                        groupData << [
                                'suqId'    : exist.suqId,
                                'selfId'   : exist.selfId,
                                'dailyDate': exist.ddTime,
                                'selfTitle': exist.selfTitle,
                                'suqTime'  : exist.suqTime,
                                'done'     : exist.done
                        ]
                    } else {

                        pageable = new PageRequest(0, 1)
                        def dailyDiscovery = dailyDiscoveryRepository.findBySelfId(s.id, pageable)

                        def time
                        if (dailyDiscovery.size() > 0) {
                            time = dailyDiscovery[0].time
                        }

                        groupData << [
                                'suqId'    : '',
                                'selfId'   : s.id,
                                'dailyDate': time,
                                'selfTitle': s.title,
                                'suqTime'  : '',
                                'done'     : false
                        ]
                    }
                }
                list << [
                        'groupTitle': g.content,
                        'list'      : groupData
                ]
            }

            [
                    'success': '1',
                    'data'   : list,
                    'more'   : more
            ]

        }

    }

    /**
     * 确认准确性
     * @param qId
     * @param user
     * @return
     */
    @RequestMapping(value = 'comfirmUserSelfQuestionnireAccuracy')
    comfirmSelfUserQuestionnireAccuracy(@RequestParam long qId,
                                        @RequestParam(required = false) boolean accuracy,
                                        @ModelAttribute('currentUser') User user) {

        def suq = selfUserQuestionnaireRepository.findOne(qId)

        suq.accuracy = accuracy

        selfUserQuestionnaireRepository.save(suq)
        '{"success" : "1"}'
    }

    /**
     * 获取准确数，总数
     * @param selfId
     */
    @RequestMapping(value = 'retrieveSelfAmount')
    retrieveSelfAmount(@RequestParam long selfId) {

        def self = selfRepository.findOne(selfId),
        // 准确总数
            accuracyAmount = selfUserQuestionnaireRepository.countBySelfAndAccuracy(self, true),
        // 完成总数
            allAmount = selfUserQuestionnaireRepository.countBySelf(self)

        def accuracyPercent = ((self.basicAccuracyAmount + accuracyAmount) / (self.basisAmount + allAmount)) * 100

        [
                success       : '1',
                percent       : accuracyPercent as int,
                accuracyAmount: accuracyAmount,
                allAmount     : allAmount
        ]

    }
}
