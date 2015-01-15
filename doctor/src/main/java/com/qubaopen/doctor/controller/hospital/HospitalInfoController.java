package com.qubaopen.doctor.controller.hospital;

import static com.qubaopen.doctor.utils.ValidateUtil.validatePhone;

import java.io.IOException;
import java.text.ParseException;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.doctor.repository.hospital.HospitalInfoRepository;
import com.qubaopen.survey.entity.hospital.Hospital;
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
	
	@Override
	protected MyRepository<HospitalInfo, Long> getRepository() {
		return hospitalInfoRepository;
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
	@RequestMapping(value = "modifyHosptialInfo")	
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
			@ModelAttribute("currentHospital") Hospital hospital
			) {
		
		logger.trace("-- 修改诊所信息 --");
		
		HospitalInfo hi = hospitalInfoRepository.findOne(hospital.getId());
		
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
//			Assert.isTrue(validatePhone(phone), "{\"success\": \"0\", \"message\" : \"err003\"}");
			if (!validatePhone(phone)) {
				return "{\"success\": \"0\", \"message\" : \"err003\"}";
			}
		}
		if (urgentPhone != null) {
			if (!validatePhone(urgentPhone)) {
				return "{\"success\": \"0\", \"message\" : \"err003\"}";
			}
		}
		if (qq != null) {
			hi.setQq(qq);
		}
		if (introduce != null) {
			hi.setIntroduce(introduce);
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
		if (timeJson != null) {
			String[] models = hi.getBookTime().split(",");
			
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
				hi.setBookTime(resultModel.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		hospitalInfoRepository.save(hi);
		
		return "{\"success\": \"1\"}";
	}

}
