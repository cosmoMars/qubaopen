package com.qubaopen.doctor.controller.hospital;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.doctor.repository.cash.BankRepository;
import com.qubaopen.doctor.repository.hospital.HospitalCaptchaRepository;
import com.qubaopen.doctor.repository.hospital.HospitalCashLogRepository;
import com.qubaopen.doctor.repository.hospital.HospitalCashRepository;
import com.qubaopen.doctor.repository.hospital.HospitalInfoRepository;
import com.qubaopen.doctor.repository.hospital.HospitalRepository;
import com.qubaopen.doctor.repository.hospital.HospitalTakeCashRepository;
import com.qubaopen.doctor.service.HospitalService;
import com.qubaopen.survey.entity.hospital.Hospital;
import com.qubaopen.survey.entity.hospital.HospitalCaptcha;
import com.qubaopen.survey.entity.hospital.HospitalCash;
import com.qubaopen.survey.entity.hospital.HospitalCashLog;
import com.qubaopen.survey.entity.hospital.HospitalCashLog.Type;
import com.qubaopen.survey.entity.hospital.HospitalInfo;
import com.qubaopen.survey.entity.hospital.HospitalTakeCash;

@RestController
@RequestMapping("hospitalCash")
@SessionAttributes("currentHospital")
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
	private HospitalRepository hospitalRepository;

	@Autowired
	private BankRepository bankRepository;
	
	@Autowired
	private HospitalService hospitalService;

	@Override
	protected MyRepository<HospitalCash, Long> getRepository() {
		return hospitalCashRepository;
	}

	/**
	 * @param index
	 * @param pageable
	 * @param hospital
	 * @return
	 * 获取诊所取现日志
	 */
	@RequestMapping(value = "retrieveHospitalCashLog", method = RequestMethod.POST)
	private Map<String, Object> retrieveHospitalCashLog(@RequestParam(required = false) Integer index,
			@PageableDefault(page = 0, size = 20, sort = "createdDate", direction = Direction.DESC)
			Pageable pageable,
			@ModelAttribute("currentHospital") Hospital hospital) {
		
		logger.trace("-- 获取取款日志 --");
		
		// 取7天前开始的数据
//		Date now = new Date();
//		Calendar c = Calendar.getInstance();
//		c.setTime(now);
//		c.add(Calendar.DATE, -7);
//		Date before = c.getTime();
		
		Map<String, Object> filters = new HashMap<String, Object>();
	
		HospitalCash cash = null;

		if (0 == pageable.getPageNumber()) {
			cash = hospitalCashRepository.findOne(hospital.getId());
			if (cash == null) {
				cash = new HospitalCash();
				cash.setId(hospital.getId());
				cash = hospitalCashRepository.save(cash);
			}
		}
		
		Type type = null;
		if (index == null) { // 默认查询收入
			type = HospitalCashLog.Type.In;
		} else {
			type = HospitalCashLog.Type.values()[index];
		}

		filters.put("type_equal", type);
//		filters.put("time_greaterThanOrEqualTo", before);

		Page<HospitalCashLog> page = hospitalCashLogRepository.findAll(filters, pageable);
		
		List<HospitalCashLog> cashLogs = page.getContent();
		
		List<Map<String, Object>> inDetail = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> outDetail =  new ArrayList<Map<String,Object>>();
		
		if (HospitalCashLog.Type.In == type) {
			for (HospitalCashLog log : cashLogs) {
				Map<String, Object> in = new HashMap<String, Object>();
				
				in.put("userName", log.getUserName() != null ? log.getUserName() : "");
				in.put("cash", log.getCash());
				in.put("payType", log.getPayType() != null ? log.getPayType().ordinal() : "");
				in.put("time", log.getTime() != null ? log.getTime() : "");
				in.put("payStatus", log.getPayStatus() != null ? log.getPayStatus().ordinal() : "");
				inDetail.add(in);
			}
		} else if (HospitalCashLog.Type.Out == type) {
			for (HospitalCashLog log : cashLogs) {
				Map<String, Object> out = new HashMap<String, Object>();
				out.put("cash", log.getCash());
				out.put("payType", log.getPayType() != null ? log.getPayType().ordinal() : "");
				out.put("time", log.getTime() != null ? log.getTime() : "");
				out.put("payStatus", log.getPayStatus() != null ? log.getPayStatus().ordinal() : "");
				outDetail.add(out);
			}
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		if (0 == pageable.getPageNumber()) {
			result.put("success", "1");
			result.put("inCash", cash.getInCash());
			result.put("outCash", cash.getOutCash());
			result.put("currentCash", cash.getCurrentCash());
			result.put("more", page.hasNext());
			result.put("inDetail", inDetail);
			result.put("outDetail", outDetail);
		} else {
			result.put("success", "1");
			result.put("more", page.hasNext());
			result.put("inDetail", inDetail);
			result.put("outDetail", outDetail);
		}
		
		return result;
	}

	@RequestMapping(value = "takeCash", method = RequestMethod.POST)
	private String takeCash(@RequestParam(required = false) Double curCash,
			@RequestParam(required = false) Integer type,
			@RequestParam(required = false) String alipayNum,
			@RequestParam(required = false) Long bankId,
			@RequestParam(required = false) String bankCard,
			@RequestParam(required = false) String captcha,
			@ModelAttribute("currentHospital") Hospital hospital) {

		logger.trace("-- 医院取现 --");

		// HospitalInfo hi = hospitalInfoRepository.findOne(hospital.getId());

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
		hospitalCashLog.setPayStatus(HospitalCashLog.PayStatus.Processing);

		hospitalCashLogRepository.save(hospitalCashLog);
		hCaptcha.setCaptcha(null);
		hospitalCaptchaRepository.save(hCaptcha);
		hospitalCashRepository.save(hc);
		hospitalTakeCashRepository.save(hospitalTakeCash);
		return "{\"success\" : \"1\"}";
	}

	/**
	 * @param hospital
	 * @return
	 * 获取验证码
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "retireveCashCaptcha", method = RequestMethod.GET)
	private Map<String, Object> retireveCashCaptcha(@ModelAttribute("currentHospital") Hospital hospital){
		
		
		logger.trace("-- 诊所获取验证码 --");
		HospitalInfo hospitalInfo = hospitalInfoRepository.findOne(hospital.getId());
		
		
		Map<String, Object> result = new HashMap<String, Object>();
		if (hospitalInfo.getLoginStatus() != HospitalInfo.LoginStatus.Audited) {
			result.put("success", "1");
			result.put("message", "err916");
			return result;
		}
		 
		if (hospital.getEmail() == null) {
			hospital = hospitalRepository.findOne(hospital.getId());
		}
		
		result = (Map<String, Object>) hospitalService.sendCaptcha(hospital.getEmail());
		
		return result;
		
	}
	
}
