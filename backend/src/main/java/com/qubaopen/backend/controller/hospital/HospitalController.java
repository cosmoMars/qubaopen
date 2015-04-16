package com.qubaopen.backend.controller.hospital;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.backend.repository.genre.GenreRepository;
import com.qubaopen.backend.repository.genre.TargetUserRepository;
import com.qubaopen.backend.repository.hospital.HospitalInfoRepository;
import com.qubaopen.backend.repository.hospital.HospitalRepository;
import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.Genre;
import com.qubaopen.survey.entity.doctor.TargetUser;
import com.qubaopen.survey.entity.hospital.Hospital;
import com.qubaopen.survey.entity.hospital.HospitalInfo;


@RestController
@RequestMapping("hospital")
public class HospitalController extends AbstractBaseController<Hospital,Long>{
	
	@Autowired
	private HospitalRepository hospitalRepository;

    @Autowired
    private HospitalInfoRepository hospitalInfoRepository;

	@Autowired
	private GenreRepository genreRepository;
	
	@Autowired
	private TargetUserRepository targetUserRepository;
	
	@Override
	protected MyRepository<Hospital, Long> getRepository() {
		return hospitalRepository;
	}

	
	/**
	 * 获取医师列表
	 * @param pageable
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "retrieveHospital", method = RequestMethod.GET)
	private Object retrieveHospital(@PageableDefault(page = 0, size = 20)
			Pageable pageable,
			@RequestParam(required = false) Integer status) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		HospitalInfo.LoginStatus s = HospitalInfo.LoginStatus.values()[status];
		List<Hospital> hospitals = hospitalRepository.getList(pageable, s);

		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map ;
		for (Hospital hospital : hospitals) {
			map = new HashMap<String, Object>();
			map.put("id", hospital.getId());
			map.put("email", hospital.getEmail());
			map.put("loginStatus", hospital.getHospitalInfo().getLoginStatus().ordinal());
			map.put("name", hospital.getHospitalInfo().getName());
			map.put("createTime", hospital.getHospitalInfo().getCreatedDate()==null?"":hospital.getHospitalInfo().getCreatedDate().toDate());
			map.put("genre", hospital.getHospitalInfo().getGenre()==null?"":hospital.getHospitalInfo().getGenre());
			map.put("targetUser", hospital.getHospitalInfo().getTargetUser()==null?"":hospital.getHospitalInfo().getTargetUser());
			map.put("review", hospital.getHospitalInfo().isReview());
			map.put("reviewReason", hospital.getHospitalInfo().getReviewReason()==null?"":hospital.getHospitalInfo().getReviewReason());
			map.put("refusalReason", hospital.getHospitalInfo().getRefusalReason()==null?"":hospital.getHospitalInfo().getRefusalReason());
			map.put("avatar", hospital.getHospitalInfo().getHospitalAvatar()==null?"":hospital.getHospitalInfo().getHospitalAvatar());
			map.put("record", hospital.getHospitalInfo().getHospitalRecordPath()==null?"":hospital.getHospitalInfo().getHospitalRecordPath());
			map.put("introduce", hospital.getHospitalInfo().getIntroduce()==null?"":hospital.getHospitalInfo().getIntroduce());
			map.put("doctorRecords",hospital.getHospitalInfo().getHospitalDoctorRecords());
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
	@RequestMapping(value = "modifyHospitalStatus", method = RequestMethod.POST)
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

        HospitalInfo di = hospitalInfoRepository.findOne(id);
        
		
		if(null != di){
			HospitalInfo.LoginStatus status = HospitalInfo.LoginStatus.values()[loginStatus];


            
            //修改咨询师info
            //添加咨询师 流派 以及对象
            di.setIntroduce(introduce);
            
            List<Genre> genreList=genreRepository.findByIds(genres);
            Set<Genre> genreSet = new HashSet<Genre>(genreList);
            di.setGenres(genreSet);
            
            List<TargetUser> targetList=targetUserRepository.findByIds(targetUsers);
            Set<TargetUser> targetSet = new HashSet<TargetUser>(targetList);
            di.setTargetUsers(targetSet);

            Map<String, Object> result;
			if(status == HospitalInfo.LoginStatus.Refusal && di.getLoginStatus() == HospitalInfo.LoginStatus.Auditing){
				//拒绝
				//TODO 下面的发短信改为发邮件
//                result = smsService.sendSmsMessage(di.getPhone(), 3, param);
                di.setRefusalReason(refusalReason);
			} else if (status == HospitalInfo.LoginStatus.Audited && (di.getLoginStatus() == HospitalInfo.LoginStatus.Auditing || di.getLoginStatus() == HospitalInfo.LoginStatus.Audited)){
				//通过
				
				if(di.getLoginStatus() == HospitalInfo.LoginStatus.Auditing){
					//如果是第一次审核通过的 发送短信
					//TODO 下面的发短信改为发邮件
//	                result = smsService.sendSmsMessage(di.getPhone(), 2, param);
//	               
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
			hospitalInfoRepository.save(di);
			return "{\"success\" : \"1\"}";
		}

		return "{\"success\" : \"0\"}";
	}
	
	/**
	 * 获取 诊所 流派和对象
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "getGenreTarget", method = RequestMethod.GET)
	private Object getGenreTarget(
			@RequestParam(required = false) Long id) {
		
		
		Map<String, Object> result = new HashMap<String, Object>();

		List<Genre> genres = genreRepository.findAll();
		List<TargetUser> targetUsers = targetUserRepository.findAll();

		HospitalInfo hospitalInfo =hospitalInfoRepository.findOne(id);


		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map1 ;
		for (Genre genre : genres) {
			map1 = new HashMap<String, Object>();
			map1.put("id", genre.getId());
			map1.put("name", genre.getName());
			map1.put("checked",hospitalInfo.getGenres().contains(genre));
			list.add(map1);
		}
		

		List<Map<String, Object>> list2 = new ArrayList<Map<String,Object>>();
		Map<String, Object> map2 ;
		for (TargetUser targetUser : targetUsers) {
			map2 = new HashMap<String, Object>();
			map2.put("id", targetUser.getId());
			map2.put("name", targetUser.getName());
			map2.put("checked",hospitalInfo.getTargetUsers().contains(targetUser));
			list2.add(map2);
		}

		
		result.put("success", "1");
		result.put("genres", list);
		result.put("targetUsers", list2);
		return result;
	}
}
