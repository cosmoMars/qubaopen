package com.qubaopen.survey.controller.user

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.user.User;
import com.qubaopen.survey.entity.user.UserReceiveAddress
import com.qubaopen.survey.repository.base.AreaCodeRepository;
import com.qubaopen.survey.repository.user.UserReceiveAddressRepository
import com.qubaopen.survey.service.user.UserReceiveAddressService
import static com.qubaopen.survey.utils.ValidateUtil.*

@RestController
@RequestMapping('userReceiveAddresses')
@SessionAttributes('currentUser')
public class UserReceiveAddressController extends AbstractBaseController<UserReceiveAddress, Long>  {

	@Autowired
	UserReceiveAddressRepository userReceiveAddressRepository
	
	@Autowired
	AreaCodeRepository areaCodeRepository

	@Autowired
	UserReceiveAddressService userReceiveAddressService

	@Override
	protected MyRepository<UserReceiveAddress, Long> getRepository() {
		userReceiveAddressRepository
	}

	/**
	 * 新增收货地址
	 */
	@Override
	@RequestMapping(method = RequestMethod.POST)
	add(@RequestBody @Valid UserReceiveAddress userReceiveAddress, BindingResult result) {

		logger.trace ' -- 新增收货地址 -- '

		if (!userReceiveAddress) {
			return '{"success": "0", "message": "err400"}'
		}

		def address = userReceiveAddressService.add(userReceiveAddress)

		[
			'success': "1",
			'addressId' :address.id
		]
	}

	/**
	 * 修改收货地址
	 */
	@RequestMapping(method = RequestMethod.PUT)
	modify(@RequestBody(required = false) UserReceiveAddress userReceiveAddress) {

		logger.trace ' -- 修改收货地址 -- '

		userReceiveAddressService.modify(userReceiveAddress)
		'{"success": "1"}'

	}

	/**
	 * 删除收货地址
	 */
	@Override
	@RequestMapping(value = '{id}', method = RequestMethod.DELETE)
	delete(@PathVariable Long id) {

		logger.trace ' -- 删除收货地址 -- '
		userReceiveAddressService.deleteUserReceiveAddress(id)
	}
	
	
	/**
	 * 获取收货地址列表
	 */
	@RequestMapping(value = 'getAddressList', method = RequestMethod.GET)
	getAddressList(@ModelAttribute('currentUser') User user){
		
		return userReceiveAddressService.getUserReceiveAddressList(user);
		
	}
	
	/**
	 * 新增收货地址 v2
	 */
	@RequestMapping(value = 'addAddress', method = RequestMethod.POST)
	addAddress(@RequestParam String consignee,
			@RequestParam String detialAddress,
			@RequestParam String phone,
			@RequestParam(required = false) Long areaId,
			@RequestParam(required = false) String areaCode,
			@RequestParam(required = false) String postCode,
			@RequestParam(required = false) Boolean defaultAddress,
			@ModelAttribute('currentUser') User user) {
			
			UserReceiveAddress userReceiveAddress=new UserReceiveAddress();
			userReceiveAddress.setUser(user)
			userReceiveAddress.setConsignee(consignee)
			userReceiveAddress.setDetialAddress(detialAddress)
			userReceiveAddress.setPhone(phone)
			userReceiveAddress.setPostCode(postCode)
			
			// 判断地区
//			if (areaId == null && areaCode == null) {
//				return '{"success" : "0", "message" : "亲，你还没有选择地区哦！"}'
//			}
			
			def area;
			if(areaId){
				area=areaCodeRepository.findOne(areaId);
			}else if(areaCode){
				area=areaCodeRepository.findByCode(areaCode)
			
			}
			if(area){
				userReceiveAddress.setAreaCode(area);
			}
			
			if(!defaultAddress){
				defaultAddress=false
			}
			userReceiveAddress.setDefaultAddress(defaultAddress);
			
			
			def address = userReceiveAddressService.add(userReceiveAddress)
	
			if(address.hasProperty("id")){
				[
					'success': "1",
					'addressId' :address.id
				]
			}else{
				[
					'success' : "1",
					'address' : address
				]
				
			}
			
		
	}
			
	/**
	 * 修改收货地址 v2
	 */
	@RequestMapping(value = 'modifyAddress',method = RequestMethod.POST)
	modifyAddress(@RequestParam long id,
			@RequestParam(required = false) String consignee,
			@RequestParam(required = false) String detialAddress,
			@RequestParam(required = false) String phone,
			@RequestParam(required = false) Long areaId,
			@RequestParam(required = false) String areaCode,
			@RequestParam(required = false) String postCode,
			@RequestParam(required = false) Boolean defaultAddress,
			@ModelAttribute('currentUser') User user) {
			
		logger.trace ' -- 修改收货地址 -- '
		def userReceiveAddress=userReceiveAddressRepository.findOne(id)
		
		if(!userReceiveAddress){
			return '{"success": "0", "message": "err402"}'
		}
		if(userReceiveAddress.user.id!=user.id){
			return '{"success": "0", "message": "err403"}'
		}
		if(consignee){
			userReceiveAddress.setConsignee(consignee)
		}
		if(detialAddress){
			userReceiveAddress.setDetialAddress(detialAddress)
		}
		if(phone){
			if (phone && !validatePhone(phone)) {
				return '{"success" : "0", "message": "err003"}'
			}
			userReceiveAddress.setPhone(phone)
		}
		if(postCode){
			userReceiveAddress.setPostCode(postCode)
		}	
		
		def area;
		if(areaId){
			area=areaCodeRepository.findOne(areaId);
		}else if(areaCode){
			area=areaCodeRepository.findByCode(areaCode)
		}
		if(area){
			userReceiveAddress.setAreaCode(area);
		}
		
		if (defaultAddress != null) {
			userReceiveAddress.setDefaultAddress(defaultAddress);
			def otherAddrees = userReceiveAddressRepository.findByUserAndOtherAddress(user, userReceiveAddress)
			otherAddrees.each {
				it.defaultAddress = false
			}
			otherAddrees << userReceiveAddress
			userReceiveAddressRepository.save(userReceiveAddress)
		} else {
			userReceiveAddressRepository.save(userReceiveAddress)
		}
		'{"success" : "1"}'
	}	
			
			
	/**
	 * 删除收货地址 v2
	 */
	@RequestMapping(value = 'deleteAddress',method = RequestMethod.DELETE)
	deleteAddress(@RequestParam long id,
			@ModelAttribute('currentUser') User user) {
			
		logger.trace ' -- 修改收货地址 -- '
		def userReceiveAddress=userReceiveAddressRepository.findOne(id)
		
		if(!userReceiveAddress){
			return '{"success": "0", "message": "err402"}'
		}
		if(userReceiveAddress.user.id!=user.id){
			return '{"success": "0", "message": "err403"}'
		}
		userReceiveAddressService.deleteUserReceiveAddress(id)
	}				
			
}
