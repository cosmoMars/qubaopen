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
			return '{"success": "0", "message": "err103"}'
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

		try {
			userReceiveAddressService.modify(userReceiveAddress)
			'{"success": "1"}'
		} catch (Exception e) {
			'{"success": "0"}'
		}


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
				address
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
			return '{"success": "0", "message": "err102"}'
		}
		if(userReceiveAddress.user.id!=user.id){
			return '{"success": "0", "message": "不是你的收货地址"}'
		}
		if(consignee){
			userReceiveAddress.setConsignee(consignee)
		}
		if(detialAddress){
			userReceiveAddress.setDetialAddress(detialAddress)
		}
		if(phone){
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
		
		if(defaultAddress){
			userReceiveAddress.setDefaultAddress(defaultAddress);
		}
		
		try {
			userReceiveAddressService.modify(userReceiveAddress)
			//'{"success": "1"}'
		} catch (Exception e) {
			'{"success": "0"}'
		}


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
			return '{"success": "0", "message": "err102"}'
		}
		if(userReceiveAddress.user.id!=user.id){
			return '{"success": "0", "message": "不是你的收货地址"}'
		}
		userReceiveAddressService.deleteUserReceiveAddress(id)


	}				
			
	

}
