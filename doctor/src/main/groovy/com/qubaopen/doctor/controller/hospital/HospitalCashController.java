package com.qubaopen.doctor.controller.hospital;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.doctor.repository.cash.BankRepository;
import com.qubaopen.doctor.repository.cash.HospitalCashRepository;
import com.qubaopen.doctor.repository.hospital.HospitalCaptchaRepository;
import com.qubaopen.doctor.repository.hospital.HospitalCashLogRepository;
import com.qubaopen.doctor.repository.hospital.HospitalInfoRepository;
import com.qubaopen.doctor.repository.hospital.HospitalTakeCashRepository;
import com.qubaopen.survey.entity.hospital.Hospital;
import com.qubaopen.survey.entity.hospital.HospitalCaptcha;
import com.qubaopen.survey.entity.hospital.HospitalCash;
import com.qubaopen.survey.entity.hospital.HospitalCashLog;
import com.qubaopen.survey.entity.hospital.HospitalTakeCash;

@RestController
@RequestMapping("hospitalCash")
@SessionAttributes()
public class HospitalCashController extends AbstractBaseController<HospitalCash, Long> {

	private static Logger logger = LoggerFactory.getLogger(HospitalCashController.class);

	@Autowired
	private HospitalCashRepository hospitalCashRepository;

	@Autowired
	private HospitalInfoRepository hospitalInfoRepository;

	@Autowired
	private HospitalCaptchaRepository hospitalCaptchaRepository;
	
	@Autowired
	private HospitalCashLogRepository hospitalCashLogRepository;
	
	@Autowired
	private HospitalTakeCashRepository hospitalTakeCashRepository;
	
	@Autowired
	private BankRepository bankRepository;

	@Override
	protected MyRepository<HospitalCash, Long> getRepository() {
		return hospitalCashRepository;
	}

	@RequestMapping(value = "takeCash", method = RequestMethod.POST)
	private String takeCash(@RequestParam(required = false) Double curCash, @RequestParam(required = false) Integer type, @RequestParam(required = false) String alipayNum,
			@RequestParam(required = false) Long bankId, @RequestParam(required = false) String bankCard, @RequestParam(required = false) String captcha,
			@ModelAttribute("currentHospital") Hospital hospital) {

		logger.trace("-- 医院取现 --");

//		HospitalInfo hi = hospitalInfoRepository.findOne(hospital.getId());

		HospitalCaptcha hCaptcha = hospitalCaptchaRepository.findOne(hospital.getId());

		if (captcha != hCaptcha.getCaptcha()) {
			return "{\"success\" : \"0\", \"message\" : \"err007\"}";
		}

		HospitalCash hc = hospitalCashRepository.findOne(hospital.getId());
		if (type == null) {
			return "{\"success\" : \"0\", \"message\" : \"err901\"}"; // 支付方式不正确
		}
		if (curCash != null && curCash > hc.getCurrentCash()) {
			return "{\"success\" : \"0\", \"message\" : \"err911\"}"; // 取款金额超过当前余额
		}

		hc.setCurrentCash(hc.getCurrentCash() - curCash);

		HospitalTakeCash hospitalTakeCash = new HospitalTakeCash();
		hospitalTakeCash.setHospital(hospital);
		hospitalTakeCash.setCash(curCash);
		hospitalTakeCash.setStatus(HospitalTakeCash.Status.Auditing);

		if (HospitalTakeCash.Type.Alipay.ordinal() == type) {
			hospitalTakeCash.setType(HospitalTakeCash.Type.Alipay);
			if (alipayNum == null) {
				return "{\"success\" : \"0\", \"message\" : \"err902\"}"; // 支付宝帐号为空
			}
			hospitalTakeCash.setAlipayNum(alipayNum);
		} else if (HospitalTakeCash.Type.BackCard.ordinal() == type) {
			hospitalTakeCash.setType(HospitalTakeCash.Type.BackCard);
			if (bankId == null) {
				return "{\"success\" : \"0\", \"message\" : \"err912\"}"; // 没有选择银行
			}
			if (bankCard == null) {
				return "{\"success\" : \"0\", \"message\" : \"err913\"}"; // 没有银行卡号
			}
			hospitalTakeCash.setBank(bankRepository.findOne(bankId));
			hospitalTakeCash.setBankCard(bankCard);
		}
		
		HospitalCashLog hospitalCashLog = new HospitalCashLog();
		hospitalCashLog.setHospital(hospital);
		hospitalCashLog.setType(HospitalCashLog.Type.Out);
		hospitalCashLog.setCash(curCash);
		hospitalCashLog.setPayType(HospitalCashLog.PayType.values()[type]);
		hospitalCashLog.setTime(new Date());
		
		hospitalCashLogRepository.save(hospitalCashLog);
		hCaptcha.setCaptcha(null);
		hospitalCaptchaRepository.save(hCaptcha);
		hospitalCashRepository.save(hc);
		hospitalTakeCashRepository.save(hospitalTakeCash);
		return "{\"success\" : \"1\"}";
	}
	
//	/**
//	 * @param doctor
//	 * @return
//	 * 提现验证码发送
//	 */
//	@RequestMapping(value = 'retireveCashCaptcha', method = RequestMethod.GET)
//	retireveCashCaptcha(@ModelAttribute('currentDoctor') Doctor doctor){
//		
//		def di = doctorInfoRepository.findOne(doctor.id)
//		
//		if (di.loginStatus != DoctorInfo.LoginStatus.Audited) {
//			return '{"success" : "0", "message" : "err916"}'
//		}
//		 
//		def phone = doctor.phone
//		if (!phone) {
//			def d = doctorRepository.findOne(doctor.id)
//			phone = d.phone
//		}
//		
//		doctorService.sendCaptcha(phone, true)
//	}

}
