package com.qubaopen.doctor.controller.cash
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.cash.DoctorCashLogRepository
import com.qubaopen.doctor.repository.cash.DoctorCashRepository
import com.qubaopen.doctor.repository.cash.DoctorTakeCashRepository
import com.qubaopen.doctor.repository.doctor.DoctorCaptchaRepository
import com.qubaopen.doctor.repository.doctor.DoctorIdCardBindRepository
import com.qubaopen.doctor.repository.doctor.DoctorInfoRepository
import com.qubaopen.doctor.repository.doctor.DoctorRepository
import com.qubaopen.doctor.service.DoctorIdCardBindService
import com.qubaopen.doctor.service.DoctorService
import com.qubaopen.survey.entity.cash.Bank
import com.qubaopen.survey.entity.cash.DoctorCash
import com.qubaopen.survey.entity.cash.DoctorCashLog
import com.qubaopen.survey.entity.cash.DoctorTakeCash
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.doctor.DoctorInfo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.web.PageableDefault
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping('doctorCash')
@SessionAttributes('currentDoctor')
public class DoctorCashController extends AbstractBaseController<DoctorCash, Long> {

	static Logger logger = LoggerFactory.getLogger(DoctorCashController.class)
	
	@Autowired
	DoctorCashRepository doctorCashRepository
	
	@Autowired
	DoctorCashLogRepository doctorCashLogRepository
	
	@Autowired
	DoctorCaptchaRepository doctorCaptchaRepository
	
	@Autowired
	DoctorIdCardBindService doctorIdCardBindService
	
	@Autowired
	DoctorIdCardBindRepository  doctorIdCardBindRepository
	
	@Autowired
	DoctorTakeCashRepository doctorTakeCashRepository
	
	@Autowired
	DoctorService doctorService
	
	@Autowired
	DoctorRepository doctorRepository
	
	@Autowired
	DoctorInfoRepository doctorInfoRepository

	@Override
	MyRepository<DoctorCash, Long> getRepository() {
		doctorCashRepository
	}
	
	/**
	 * 获取医师现金信息
	 * @param doctor
	 * @return
	 */
	@Transactional
	@RequestMapping(value = 'retrieveCashInfo', method = RequestMethod.GET)
	retrieveCashInfo(@PageableDefault(page = 0, size = 20, sort = 'createdDate', direction = Direction.DESC) Pageable pageable,
		@ModelAttribute('currentDoctor') Doctor doctor) {

		logger.trace '--  获取医师现金信息 --'
		
		def cash, cashLogs

		def doctorInfo = doctorInfoRepository.findOne(doctor.id)

		if (pageable.pageNumber == 0) {
			cash = doctorCashRepository.findOne(doctor.id)
			if (!cash) {
				cash = new DoctorCash(
					id : doctor.id
				)
				cash = doctorCashRepository.save(cash)
			}
		}
		cashLogs = doctorCashLogRepository.findByDoctor(doctor, pageable)
		
		def data = []
		
		cashLogs.each {
			data << [
				'userName' : it?.userName,
				'cash' : it?.cash,
				//TODO 2015-03-32 ios 因为已经发布 所以先保留该字段 遗留问题
				'payType' : it?.type?.ordinal(),
				'type' : it?.type?.ordinal(),
				'time' : it?.time,
				'payStatus' : it?.payStatus
			]
		}
		def more = true
		if (data.size() < 20) {
			more = false
		}
		def clearContent
		if (doctorInfo.hospital?.hospitalInfo?.name) {
			clearContent = "与${doctorInfo.hospital.hospitalInfo.name}在${doctorInfo.hospital.hospitalInfo.clearDay}号进行统一打款" as String
		}
		if (pageable.pageNumber == 0) {
			return [
					"success"     : "1",
					'inCash'      : cash?.inCash,
					'outCash'     : cash?.outCash,
					'currentCash' : cash?.currentCash,
					'clearContent': clearContent,
					'more'        : more,
					'data'        : data
			]
		} else {
			return [
					"success": "1",
					'more'   : more,
					'data'   : data
			]
		}
		
	}
	
	/**
	 * 提现
	 * @param curCash
	 * @param bankId
	 * @param bankCard
	 * @param name
	 * @param idCard
	 * @param captcha
	 * @param doctor
	 * @return
	 */
	@Transactional
	@RequestMapping(value = 'takeCash', method = RequestMethod.POST)
	takeCash(@RequestParam(required = false) Double curCash,
		@RequestParam(required = false) Integer type,
		@RequestParam(required = false) String alipayNum,
		@RequestParam(required = false) Long bankId,
		@RequestParam(required = false) String bankCard,
//		@RequestParam(required = false) String name,
//		@RequestParam(required = false) String idCard,
		@RequestParam(required = false) String captcha,
		@ModelAttribute('currentDoctor') Doctor doctor) {
		
		logger.trace '-- 提现请求 --'
		
		def di = doctorInfoRepository.findOne(doctor.id)
		
		if (di.loginStatus != DoctorInfo.LoginStatus.Audited) {
			return '{"success" : "0", "message" : "err916"}'
		}
		// 验证码对比
		def dCaptcha = doctorCaptchaRepository.findOne(doctor.id)
		
		if (captcha != dCaptcha.captcha) {
			return '{"success" : "0", "message" : "err007"}'
		}
		
		// 身份证绑定
		/*def idCardBind = doctorIdCardBindRepository.findOne(doctor.id),
			result
		
		if (!idCardBind) {
			result = doctorIdCardBindService.submitUserIdCard(idCard, name, doctor)
			if (result) {
				def json = (Map<String, ?>) new JsonSlurper().parseText(result)
				if ('1' != json['success'] || !'1'.equals(json['success'])) {
					return result
				}
			} else {
				return '{"success" : "0", "message" : "err900"}' // 该医师还未认证
			}
		}*/
		
		def cash = doctorCashRepository.findOne(doctor.id)
		if (type == null) {
			return '{"success" : "0", "message" : "err901"}' // 支付方式不正确
		}
		if (curCash != null && curCash > cash?.currentCash) {
			return '{"success" : "0", "message" : "err911"}' // 取款金额超过当前余额
		}
		
		cash.currentCash -= curCash
//		cash.outCash += curCash
		
		def takeCash = new DoctorTakeCash(
			doctor : doctor,
			cash : curCash,
			status : DoctorTakeCash.Status.Auditing
		)
//		def payType = TakeCash.Type.values[type]
		if (DoctorTakeCash.Type.Alipay.ordinal() == type) {
			takeCash.type = DoctorTakeCash.Type.Alipay
			if (!alipayNum) {
				return '{"success" : "0", "message" : "err902"}' // 支付宝帐号为空
			}
			takeCash.alipayNum = alipayNum
			
		} else if (DoctorTakeCash.Type.BackCard.ordinal() == type) {
			takeCash.type = DoctorTakeCash.Type.BackCard
			
			if (bankId == null) {
				return '{"success" : "0", "message" : "err912"}' // 没有选择银行
			}
			if (bankCard == null) {
				return '{"success" : "0", "message" : "err913"}' // 没有银行卡号
			}
			takeCash.bank = new Bank(id : bankId)
			takeCash.bankCard = bankCard
		}
		
		def cashLog = new DoctorCashLog(
			doctor : doctor,
			type : DoctorCashLog.Type.Out,
			cash : curCash,
			payType : DoctorCashLog.PayType.values()[type],
			time : new Date(),
			payStatus : DoctorCashLog.PayStatus.Processing
		)
		cashLog.doctorTakeCash=takeCash;
		doctorCashLogRepository.save(cashLog)
		dCaptcha.captcha = null
		doctorCaptchaRepository.save(dCaptcha)
		doctorCashRepository.save(cash)
		doctorTakeCashRepository.save(takeCash)
		'{"success" : "1"}'
	}
	
	/**
	 * @param doctor
	 * @return
	 * 提现验证码发送
	 */
	@RequestMapping(value = 'retireveCashCaptcha', method = RequestMethod.GET)
	retireveCashCaptcha(@ModelAttribute('currentDoctor') Doctor doctor){
		
		def di = doctorInfoRepository.findOne(doctor.id)
		
		if (di.loginStatus != DoctorInfo.LoginStatus.Audited) {
			return '{"success" : "0", "message" : "err916"}'
		}
		 
		def phone = doctor.phone
		if (!phone) {
			def d = doctorRepository.findOne(doctor.id)
			phone = d.phone
		}
		
		doctorService.sendCaptcha(phone, true)
	}
	
}
