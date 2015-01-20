package com.qubaopen.doctor.controller.hospital;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.doctor.repository.hospital.HospitalDoctorRecordRepository;
import com.qubaopen.doctor.repository.hospital.HospitalInfoRepository;
import com.qubaopen.survey.entity.hospital.Hospital;
import com.qubaopen.survey.entity.hospital.HospitalDoctorRecord;
import com.qubaopen.survey.entity.hospital.HospitalInfo;

@RestController
@RequestMapping("hospitalDoctorRecord")
@SessionAttributes("currentHospital")
public class HospitalDoctorRecordController extends AbstractBaseController<HospitalDoctorRecord, Long> {

	private static Logger logger = LoggerFactory.getLogger(HospitalDoctorRecordController.class);
	
	@Autowired
	private HospitalDoctorRecordRepository hospitalDoctorRecordRepository;
	
	@Autowired
	private HospitalInfoRepository hospitalInfoRepository;
	
	@Override
	protected MyRepository<HospitalDoctorRecord, Long> getRepository() {
		return hospitalDoctorRecordRepository;
	}

	@Transactional
	@RequestMapping(value = "uploadHospitalDoctorRecord", method = RequestMethod.POST)
	private String uploadHospitalDoctorRecord(
			@RequestParam(required = false) MultipartFile record,
			HttpServletRequest request,
			@ModelAttribute("currentHospital") Hospital hospital) {
		
		logger.trace("-- 上传资质 --");
		
		HospitalInfo hospitalInfo = hospitalInfoRepository.findOne(hospital.getId());
		
		Set<HospitalDoctorRecord> hospitalDoctorRecords = hospitalInfo.getHospitalDoctorRecords();
		
		String hdRecord = "hdRecord";
		String path = request.getServletContext().getRealPath("/") + hdRecord;
		File file = new File(path);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdir();
		}
		HospitalDoctorRecord hdr = new HospitalDoctorRecord();
		
		String fileName = "" + hospital.getId() + System.currentTimeMillis()  + ".png";
		String recordPath = path + "/" + fileName;
		try {
			saveFile(record.getBytes(), recordPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		hdr.setHospitalInfo(hospitalInfo);
		hdr.setDoctorRecordPath("/" + hdRecord + "/" + fileName);
		hospitalDoctorRecords.add(hdr);
		
		hospitalDoctorRecordRepository.save(hospitalDoctorRecords);
		
		return "{\"success\" : \"1\"}";
	}
	
	private void saveFile(byte[] bytes, String fileName) {
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			fos.write(bytes);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
//	if (avatar) {
//		def doctorDir = 'doctorDir'
//		def file = new File("${request.getServletContext().getRealPath('/')}$doctorDir");
//		if (!file.exists() && !file.isDirectory()) {
//			file.mkdir()
//		}
//		def fileName = "${doctor.id}_${DateFormatUtils.format(new Date(), 'yyyyMMdd-HHmmss')}.png",
//			avatarPath = "${request.getServletContext().getRealPath('/')}$doctorDir/$fileName"
//		
//		saveFile(avatar.bytes, avatarPath)
//		doctorInfo.avatarPath = "/$doctorDir/$fileName"
//	}
	
}
