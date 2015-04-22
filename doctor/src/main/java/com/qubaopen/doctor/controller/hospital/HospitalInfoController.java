package com.qubaopen.doctor.controller.hospital;

import static com.qubaopen.doctor.utils.ValidateUtil.validatePhone;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.doctor.repository.hospital.HospitalInfoRepository;
import com.qubaopen.doctor.utils.UploadUtils;
import com.qubaopen.survey.entity.hospital.Hospital;
import com.qubaopen.survey.entity.hospital.HospitalDoctorRecord;
import com.qubaopen.survey.entity.hospital.HospitalInfo;

@RestController
@RequestMapping("hospitalInfo")
@SessionAttributes("currentHospital")
public class HospitalInfoController extends AbstractBaseController<HospitalInfo, Long> {

	private static Logger logger = LoggerFactory.getLogger(HospitalInfoController.class);
	
	@Autowired
	private HospitalInfoRepository hospitalInfoRepository;
	
	@Autowired
	private ObjectMapper objectMapper;

    @Value("${hospital_url}")
    private String hospital_url;
	
	@Override
	protected MyRepository<HospitalInfo, Long> getRepository() {
		return hospitalInfoRepository;
	}
	
	/**
	 * @param hospital
	 * @return
	 * 获取诊所信息
	 */
	@RequestMapping(value = "retrieveHospitalInfo", method = RequestMethod.GET)
	public Map<String, Object> retrieveHospitalInfo(@ModelAttribute("currentHospital") Hospital hospital) {
		
		logger.trace("-- 获取诊所信息 --");
		
		HospitalInfo hospitalInfo = hospitalInfoRepository.findOne(hospital.getId());
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("success", "1");
		
		map.put("hospitalId", hospitalInfo.getId());
		map.put("name", hospitalInfo.getName() != null ? hospitalInfo.getName() : "");
		map.put("address", hospitalInfo.getAddress() != null ? hospitalInfo.getAddress() : "");
		map.put("establishTime", hospitalInfo.getEstablishTime() != null ? hospitalInfo.getEstablishTime() : "");
		map.put("bookingTime", hospitalInfo.getBookingTime());
		map.put("phone", hospitalInfo.getPhone());
		map.put("urgentPhone", hospitalInfo.getUrgentPhone());
		map.put("qq", hospitalInfo.getQq());
		map.put("introduce", hospitalInfo.getIntroduce());
		map.put("wordsConsult", hospitalInfo.isWordsConsult());
		map.put("minCharge", hospitalInfo.getMinCharge());
		map.put("maxCharge", hospitalInfo.getMaxCharge());
		map.put("genre", hospitalInfo.getGenre());
		map.put("targetUser", hospitalInfo.getTargetUser());
		map.put("avatar", hospitalInfo.getHospitalAvatar());
		map.put("record", hospitalInfo.getHospitalRecordPath());
		
		List<String> records = new ArrayList<String>();
		
		for (HospitalDoctorRecord doctorRecord : hospitalInfo.getHospitalDoctorRecords()) {
			records.add(doctorRecord.getDoctorRecordPath());
		}
		map.put("records", records.toString());
		return map;
	}
	
	/**
	 * @param name
	 * @param address
	 * @param establishTime
	 * @param phone
	 * @param urgentPhone
	 * @param qq
	 * @param introduce
	 * @param wordsConsult
	 * @param minCharge
	 * @param maxCharge
	 * @param timeJson
	 * @param hospital
	 * @return 修改诊所信息
	 */
	@RequestMapping(value = "modifyHosptialInfo", method = RequestMethod.POST)	
	private String modifyHosptialInfo(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String address,
			@RequestParam(required = false) String establishTime,
			@RequestParam(required = false) String phone,
			@RequestParam(required = false) String urgentPhone,
			@RequestParam(required = false) String qq,
			@RequestParam(required = false) String introduce,
			@RequestParam(required = false) Boolean wordsConsult,
			@RequestParam(required = false) Integer minCharge,
			@RequestParam(required = false) Integer maxCharge,
			@RequestParam(required = false) String timeJson,
			@RequestParam(required = false) String timeExp,
			@RequestParam(required = false) String genre,
			@RequestParam(required = false) String targetUser,
            @RequestParam(required = false) MultipartFile avatar,
			@ModelAttribute("currentHospital") Hospital hospital
			) {
		
		logger.trace("-- 修改诊所信息 --");
		
		HospitalInfo hi = hospitalInfoRepository.findOne(hospital.getId());

        boolean isChange = false;
        HospitalInfo hiReview = new HospitalInfo();
        BeanUtils.copyProperties(hi, hiReview);

		if (hi.getLoginStatus() != HospitalInfo.LoginStatus.Unaudited) {
		}

		if (name != null) {
			hi.setName(name);
		}
		if (address != null) {
			hi.setAddress(address);
		}
		if (establishTime != null) {
			try {
				hi.setEstablishTime(DateUtils.parseDate(establishTime, "yyyy-MM-dd"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (phone != null) {
			if (!validatePhone(phone)) {
				return "{\"success\": \"0\", \"message\" : \"err003\"}";
			}
			hi.setPhone(phone);
		}
		if (urgentPhone != null) {
			if (!validatePhone(urgentPhone)) {
				return "{\"success\": \"0\", \"message\" : \"err003\"}";
			}
			hi.setUrgentPhone(urgentPhone);
		}
		if (qq != null) {
			hi.setQq(qq);
		}
		if (introduce != null) {
			hi.setIntroduce(introduce);
		}
		if (genre != null) {
			hi.setGenre(genre);
		}
		if (targetUser != null) {
			hi.setTargetUser(targetUser);
		}
		if (wordsConsult != null) {
			hi.setWordsConsult(wordsConsult);
		}
		if (minCharge != null) {
			hi.setMinCharge(minCharge);
		}
		if (maxCharge != null) {
			hi.setMaxCharge(maxCharge);
		}
		if (timeExp != null) {
			hi.setBookingTime(timeExp);
		}
		if (timeJson != null) {
			String[] models = hi.getBookingTime().split(",");
			
			try {
				JsonNode jsonNodes = objectMapper.readTree(timeJson);
				
				for (JsonNode jsonNode : jsonNodes) {
					if (jsonNode.get("type") == null) {
						return "{\"success\" : \"0\", \"message\" : \"err905\"}";
					}
					if (jsonNode.get("day") == null) {
						return "{\"success\" : \"0\", \"message\" : \"err910\"}";
					}
					int index = jsonNode.get("day").asInt();
					int type = jsonNode.get("type").asInt();
					JsonNode hours = jsonNode.path("times");
					
					if (type == 0) {
						for (JsonNode hour : hours) {
							int idx = hour.asInt();
							models[index - 1] = models[index - 1].substring(0, idx) + "0" + models[index - 1].substring(idx + 1);
						}
					} else if (type == 1) {
						for (JsonNode hour : hours) {
							int idx = hour.asInt();
							models[index - 1] = models[index - 1].substring(0, idx) + "1" + models[index - 1].substring(idx + 1);
						}
					}
				}
				StringBuffer resultModel = new StringBuffer();
				for (int i = 0; i < models.length; i++) {
					resultModel.append(models[i]);
					if (i < models.length - 1) {
						resultModel.append(",");
					}
				}
				hi.setBookingTime(resultModel.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

        if (avatar != null) {
            String hName = hospital_url + hospital.getId();
            String url;
            try {
                url = UploadUtils.uploadTo7niu(1, hName, avatar.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            hi.setHospitalAvatar(url);
        }
        if (!hi.equals(hiReview)) {
            isChange = true;
        }

        if (isPass(hospital, hi) || (hi.getLoginStatus() == HospitalInfo.LoginStatus.Refusal && isChange)) {
            hi.setLoginStatus(HospitalInfo.LoginStatus.Auditing);
        }

        if (isChange && hi.getLoginStatus() == HospitalInfo.LoginStatus.Audited) {
            hi.setReview(true);
        }

        hospitalInfoRepository.save(hi);
		
		return "{\"success\": \"1\"}";
	}
	
	
	public boolean isPass(Hospital hospital, HospitalInfo hospitalInfo) {
		 
		if (hospitalInfo.getLoginStatus() != HospitalInfo.LoginStatus.Unaudited) {
			return false;
		}

		if (hospitalInfo.getName()==null) {
			return false;
		}
		
		if (hospitalInfo.getAddress()==null) {
			return false;
		}
		
		if (hospitalInfo.getPhone() == null) {
			return false;
		}
		
		if (hospitalInfo.getUrgentPhone() == null) {
			return false;
		}

		if (hospitalInfo.getQq() == null) {
			return false;
		}

		if (hospitalInfo.getIntroduce() == null) {
			return false;
		}

		if (hospitalInfo.getGenre() == null) {
			return false;
		}

		if (hospitalInfo.getTargetUser() == null) {
			return false;
		}

		if (hospitalInfo.getBookingTime() == null) {
			return false;
		}

		if (hospitalInfo.getHospitalRecordPath() == null) {
			return false;
		}

		if (hospitalInfo.getHospitalAvatar() == null) {
			return false;
		}

        return true;
	} 

}
