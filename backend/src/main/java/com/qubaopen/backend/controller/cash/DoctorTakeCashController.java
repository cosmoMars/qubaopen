package com.qubaopen.backend.controller.cash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.backend.repository.cash.DoctorCashLogRepository;
import com.qubaopen.backend.repository.cash.DoctorCashRepository;
import com.qubaopen.backend.repository.cash.DoctorTakeCashRepository;
import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.cash.DoctorCash;
import com.qubaopen.survey.entity.cash.DoctorCashLog;
import com.qubaopen.survey.entity.cash.DoctorTakeCash;

@RestController
@RequestMapping("doctorCash")
public class DoctorTakeCashController  extends AbstractBaseController<DoctorTakeCash, Long>{

	@Autowired
	private DoctorTakeCashRepository doctorTakeCashRepository;
	
	@Autowired
	private DoctorCashLogRepository doctorCashLogRepository;
	
	@Autowired
	private DoctorCashRepository doctorCashRepository;
	
	@Override
	protected MyRepository<DoctorTakeCash, Long> getRepository() {
		return doctorTakeCashRepository;
	}
	
	/**
	 * 获取提现记录
	 * @param pageable
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "retrieveDoctorCashs", method = RequestMethod.GET)
	private Object retrieveDoctorCashs(@PageableDefault(page = 0, size = 20)
			Pageable pageable,
			@RequestParam(required = false)  int status
			) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<DoctorTakeCash> doctorTakeCashs= doctorTakeCashRepository.findDoctorTakeCash(pageable,DoctorTakeCash.Status.values()[status]);
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		String bankName;
		for (DoctorTakeCash doctorTakeCash : doctorTakeCashs) {
			Map<String, Object> map = new HashMap<String, Object>();
			bankName="";
			map.put("id", doctorTakeCash.getId());
			map.put("doctorId", doctorTakeCash.getDoctor().getId());
			map.put("status", doctorTakeCash.getStatus().ordinal());
			map.put("alipayNum", doctorTakeCash.getAlipayNum());
			map.put("bankCard", doctorTakeCash.getBankCard());
			map.put("cash", doctorTakeCash.getCash());
			map.put("failureReason", doctorTakeCash.getFailureReason());
			if(doctorTakeCash.getBank()!=null){
				bankName=doctorTakeCash.getBank().getName();
			}
			map.put("bank", bankName);
			map.put("transactionNum", doctorTakeCash.getTransactionNum());
			map.put("type", doctorTakeCash.getType());
			map.put("createDate", doctorTakeCash.getCreatedDate().toDate());
			list.add(map);
		}
		
		result.put("success", "1");
		result.put("list", list);
		return result;

	}
	
	
	/**
	 * 修改提现订单状态
	 * @param id
	 * @param status
	 * @param failureReason
	 * @param transactionNum
	 * @return
	 */
	@RequestMapping(value = "modifyDoctorTakeCash", method = RequestMethod.POST)
	private Object modifyDoctorTakeCash(@RequestParam long id,
			@RequestParam(required = false) int status,
			@RequestParam(required = false) String failureReason,
			@RequestParam(required = false) String transactionNum
			) {
		
		Map<String, Object> result = new HashMap<String, Object>();	
		DoctorTakeCash doctorTakeCash = doctorTakeCashRepository.findOne(id);
		
		if(null != doctorTakeCash && doctorTakeCash.getStatus()==DoctorTakeCash.Status.Auditing){
			if(DoctorTakeCash.Status.values()[status]==DoctorTakeCash.Status.Failure){
				if(failureReason==null || failureReason==""){
					result.put("success", "0");
					result.put("message", "要设置提现失败的话，失败理由不能为空");
					return result;
				}else{
					doctorTakeCash.setFailureReason(failureReason);
					doctorTakeCash.setStatus(DoctorTakeCash.Status.Failure);
					DoctorCashLog doctorCashLog=doctorCashLogRepository.findByDoctorTakeCash(doctorTakeCash);
					DoctorCash doctorCash=doctorCashRepository.findOne(doctorTakeCash.getDoctor().getId());
					if(doctorCashLog!=null){
						//现金日志记录 状态改为失败（但是前台暂时不使用此状态，先直接删除）
						doctorCashLogRepository.delete(doctorCashLog);
					}
					if(doctorCash!=null){
						//用户金额的退还
						doctorCash.setCurrentCash(doctorCash.getCurrentCash()+doctorTakeCash.getCash());
						doctorCashRepository.save(doctorCash);
					}
				}
			}else if(DoctorTakeCash.Status.values()[status]==DoctorTakeCash.Status.Success){
				if(transactionNum==null || transactionNum==""){
					result.put("success", "0");
					result.put("message", "要设置提现成功的话，成功交易号不能为空");
					return result;
				}else{
					doctorTakeCash.setTransactionNum(transactionNum);
					doctorTakeCash.setStatus(DoctorTakeCash.Status.Success);
					DoctorCashLog doctorCashLog=doctorCashLogRepository.findByDoctorTakeCash(doctorTakeCash);
					if(doctorCashLog!=null){
						//现金日志记录 状态变成完成（但前台暂时不使用此状态）
						doctorCashLog.setPayStatus(DoctorCashLog.PayStatus.Completed);
						doctorCashLogRepository.save(doctorCashLog);
					}
				}
			}else{
				result.put("success", "1");
				return result;
			}
			doctorTakeCashRepository.save(doctorTakeCash);
		}else{
			result.put("success", "0");
			result.put("message", "该提现订单不是审核中状态，只能对审核中的订单进行操作");
			return result;
		}
		
		result.put("success", "1");
		return result;

	}

}
