package com.qubaopen.backend.controller.doctor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.backend.repository.assistant.AssistantRepository;
import com.qubaopen.backend.repository.doctor.DoctorInfoRepository;
import com.qubaopen.backend.repository.doctor.DoctorRecordRepository;
import com.qubaopen.backend.repository.doctor.DoctorRepository;
import com.qubaopen.backend.repository.genre.GenreRepository;
import com.qubaopen.backend.repository.genre.TargetUserRepository;
import com.qubaopen.backend.service.SmsService;
import com.qubaopen.backend.utils.UploadUtils;
import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.Assistant;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.doctor.DoctorInfo;
import com.qubaopen.survey.entity.doctor.DoctorRecord;
import com.qubaopen.survey.entity.doctor.Genre;
import com.qubaopen.survey.entity.doctor.TargetUser;


@RestController
@RequestMapping("doctor")
public class DoctorController extends AbstractBaseController<Doctor, Long>{

	@Autowired
	private DoctorRepository doctorRepository;

    @Autowired
    private DoctorInfoRepository doctorInfoRepository;

	@Autowired
	DoctorRecordRepository doctorRecordRepository;

	@Autowired
	private SmsService smsService;

    @Autowired
    private AssistantRepository assistantRepository;
    
	@Autowired
	private GenreRepository genreRepository;
	
	@Autowired
	private TargetUserRepository targetUserRepository;

	
	@Override
	protected MyRepository<Doctor, Long> getRepository() {
		return doctorRepository;
	}

	
	/**
	 * 获取医师列表
	 * @param pageable
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "retrieveDoctor", method = RequestMethod.GET)
	private Object retrieveDoctor(@PageableDefault(page = 0, size = 20)
			Pageable pageable,
			@RequestParam(required = false) Integer status) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		DoctorInfo.LoginStatus s = DoctorInfo.LoginStatus.values()[status];
		List<Doctor> doctors = doctorRepository.getList(pageable, s);

		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map ;
		for (Doctor doctor : doctors) {
			map = new HashMap<String, Object>();
			map.put("id", doctor.getId());
			map.put("phone", doctor.getPhone());
			map.put("loginStatus", doctor.getDoctorInfo().getLoginStatus().ordinal());
			map.put("name", doctor.getDoctorInfo().getName());
			map.put("sex", doctor.getDoctorInfo().getSex()==null?"":doctor.getDoctorInfo().getSex().ordinal());
			map.put("createTime", doctor.getDoctorInfo().getCreatedDate()==null?"":doctor.getDoctorInfo().getCreatedDate().toDate());
			map.put("experiece", doctor.getDoctorInfo().getExperience()==null?"":doctor.getDoctorInfo().getExperience());
			map.put("field", doctor.getDoctorInfo().getField()==null?"":doctor.getDoctorInfo().getField());
			map.put("genre", doctor.getDoctorInfo().getGenre()==null?"":doctor.getDoctorInfo().getGenre());
			map.put("targetUser", doctor.getDoctorInfo().getTargetUser()==null?"":doctor.getDoctorInfo().getTargetUser());
			map.put("avatar", doctor.getDoctorInfo().getAvatarPath()==null?"":doctor.getDoctorInfo().getAvatarPath());
			map.put("record", doctor.getDoctorInfo().getRecordPath()==null?"":UploadUtils.retrievePriavteUrl(doctor.getDoctorInfo().getRecordPath()));
			map.put("introduce", doctor.getDoctorInfo().getIntroduce()==null?"":doctor.getDoctorInfo().getIntroduce());
			map.put("review", doctor.getDoctorInfo().isReview());
			map.put("reviewReason", doctor.getDoctorInfo().getReviewReason()==null?"":doctor.getDoctorInfo().getReviewReason());
			map.put("refusalReason", doctor.getDoctorInfo().getRefusalReason()==null?"":doctor.getDoctorInfo().getRefusalReason());
			
			DoctorRecord dr=doctorRecordRepository.findOne(doctor.getId());
		    StringBuffer autoIntroduce = new StringBuffer();

			
			if(doctor.getDoctorInfo().getIntroduce()==null && dr!=null){
				
				//流派
				autoIntroduce.append("流派:");
				autoIntroduce.append(doctor.getDoctorInfo().getGenre()==null?"":"\n"+doctor.getDoctorInfo().getGenre());
				//对象
				autoIntroduce.append("\n\n擅长对象:");
				autoIntroduce.append(doctor.getDoctorInfo().getTargetUser()==null?"":"\n"+doctor.getDoctorInfo().getTargetUser());

				//学位和学历
				autoIntroduce.append("\n\n【学位和学历】");
				autoIntroduce.append(dr.getEducationalStart()==null?"":"\n"+dr.getEducationalStart()+"至");
				autoIntroduce.append(dr.getEducationalEnd()==null?"":dr.getEducationalEnd());
				autoIntroduce.append(dr.getSchool()==null?"":"\n"+dr.getSchool());
				autoIntroduce.append(dr.getProfession()==null?"":"\n"+dr.getProfession());
				autoIntroduce.append(dr.getDegree()==null?"":"\n"+dr.getDegree());
				autoIntroduce.append(dr.getEducationalIntroduction()==null?"":"\n"+dr.getEducationalIntroduction());
				
				//培训经历
				autoIntroduce.append("\n\n【培训经历】");
				autoIntroduce.append(dr.getTrainStart()==null?"":"\n"+dr.getTrainStart()+"至");
				autoIntroduce.append(dr.getTrainEnd()==null?"":dr.getTrainEnd());
				autoIntroduce.append(dr.getCourse()==null?"":"\n"+dr.getCourse());
				autoIntroduce.append(dr.getOrganization()==null?"":"\n"+dr.getOrganization());
				autoIntroduce.append(dr.getTrainIntroduction()==null?"":"\n"+dr.getTrainIntroduction());

				//接收督导经历
				autoIntroduce.append("\n\n【接收督导经历】");
				autoIntroduce.append(dr.getSupervise()==null?"":"\n"+dr.getSupervise());
				autoIntroduce.append(dr.getOrientation()==null?"":"\n"+dr.getOrientation());
				autoIntroduce.append(dr.getSuperviseHour()==null?"":"\n"+dr.getSuperviseHour());
				autoIntroduce.append(dr.getContactMethod()==null?"":"\n"+dr.getContactMethod());
				autoIntroduce.append(dr.getSuperviseIntroduction()==null?"":"\n"+dr.getSuperviseIntroduction());
				
				//自我经历
				autoIntroduce.append("\n\n【自我经历】");
				autoIntroduce.append(dr.getSelfStart()==null?"":"\n"+dr.getSelfStart()+"至");
				autoIntroduce.append(dr.getSelfEnd()==null?"":dr.getSelfEnd());
				autoIntroduce.append(dr.getTotalHour()==null?"":"\n"+dr.getTotalHour());
				autoIntroduce.append(dr.getSelfIntroduction()==null?"":"\n"+dr.getSelfIntroduction());
				
				
				map.put("introduce", autoIntroduce);
			}
			//获取个人经历信息
			
			list.add(map);
		}
		
		result.put("success", "1");
		result.put("list", list);
		return result;
	}
	
	
	/**
	 * 咨询师审核
	 * @param id
	 * @param loginStatus
	 * @param refusalReason
	 * @param review
	 * @param reviewReason
	 * @param genres
	 * @param targetUsers
	 * @param introduce
	 * @return
	 */
	@RequestMapping(value = "modifyDoctorStatus", method = RequestMethod.POST)
	private Object modifyDoctorStatus(
			@RequestParam(required = false) Long id,
			@RequestParam(required = false) int loginStatus,
			@RequestParam(required = false) String refusalReason,
			@RequestParam(required = false) Boolean review,
			@RequestParam(required = false) String reviewReason,
			@RequestParam(required = false) List<Long> genres,
			@RequestParam(required = false) List<Long> targetUsers,
			@RequestParam(required = false) String introduce
			) {

        DoctorInfo di = doctorInfoRepository.findOne(id);
        
		
		if(null != di){
            DoctorInfo.LoginStatus status = DoctorInfo.LoginStatus.values()[loginStatus];


            Assistant assistant;
            if (di.getAssistant() != null) {
                assistant = di.getAssistant();
            } else {
                Long assistantId = assistantRepository.findSpaceAssistant();

				if (assistantId == 0) {
					return "没有可用的助手";
				}
                assistant = assistantRepository.findOne(assistantId);
//				assistant = assistantRepository.findSpaceAssistant();
                di.setAssistant(assistant);
            }
          
            //修改咨询师info
            //添加咨询师 流派 以及对象
            di.setIntroduce(introduce);
            
            List<Genre> genreList=genreRepository.findByIds(genres);
            Set<Genre> genreSet = new HashSet<Genre>(genreList);
            di.setGenres(genreSet);
            
            List<TargetUser> targetList=targetUserRepository.findByIds(targetUsers);
            Set<TargetUser> targetSet = new HashSet<TargetUser>(targetList);
            di.setTargetUsers(targetSet);

			String param = "{\"name\" : \"" + assistant.getName() + "\",\"phone\" : \"" + assistant.getPhone() + "\"}";
            Map<String, Object> result;
			if(status == DoctorInfo.LoginStatus.Refusal && di.getLoginStatus() == DoctorInfo.LoginStatus.Auditing){
				//拒绝
                result = smsService.sendSmsMessage(di.getPhone(), 3, param);
                di.setRefusalReason(refusalReason);
			} else if (status == DoctorInfo.LoginStatus.Audited && (di.getLoginStatus() == DoctorInfo.LoginStatus.Auditing || di.getLoginStatus() == DoctorInfo.LoginStatus.Audited)){
				//通过
				
				if(di.getLoginStatus() == DoctorInfo.LoginStatus.Auditing){
					//如果是第一次审核通过的 发送短信
	                result = smsService.sendSmsMessage(di.getPhone(), 2, param);
	                if (!StringUtils.equals((String)result.get("resCode"), "0")) {
	                    return "{\"success\" : \"0\", \"message\" : \"resCode = " + result.get("resCode") + "\"}";
	                }
				}
				
                if (review != null) {
                    di.setReview(review);
                    if (review) {
                        di.setReviewReason(reviewReason);
                    }
                }
			} else {
                return "{\"success\" : \"0\", \"message\" : \"修改医师状态不正确\"}";
            }
          

			di.setLoginStatus(status);
			doctorInfoRepository.save(di);
			return "{\"success\" : \"1\"}";
		}

		return "{\"success\" : \"0\"}";
	}
	
	
	@RequestMapping(value = "getGenreTarget", method = RequestMethod.GET)
	private Object getGenreTarget(
			@RequestParam(required = false) Long id) {
		
		
		Map<String, Object> result = new HashMap<String, Object>();

		List<Genre> genres = genreRepository.findAll();
		List<TargetUser> targetUsers = targetUserRepository.findAll();

		DoctorInfo doctorInfo =doctorInfoRepository.findOne(id);


		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map1 ;
		for (Genre genre : genres) {
			map1 = new HashMap<String, Object>();
			map1.put("id", genre.getId());
			map1.put("name", genre.getName());
			map1.put("checked",doctorInfo.getGenres().contains(genre));
			list.add(map1);
		}
		

		List<Map<String, Object>> list2 = new ArrayList<Map<String,Object>>();
		Map<String, Object> map2 ;
		for (TargetUser targetUser : targetUsers) {
			map2 = new HashMap<String, Object>();
			map2.put("id", targetUser.getId());
			map2.put("name", targetUser.getName());
			map2.put("checked",doctorInfo.getTargetUsers().contains(targetUser));
			list2.add(map2);
		}

		
		result.put("success", "1");
		result.put("genres", list);
		result.put("targetUsers", list2);
		return result;
	}
}
