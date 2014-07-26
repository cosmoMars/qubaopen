package com.qubaopen.survey.controller.user

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserReceiveAddress
import com.qubaopen.survey.repository.user.UserReceiveAddressRepository
import com.qubaopen.survey.service.user.UserReceiveAddressService

@RestController
@RequestMapping('userReceiveAddresses')
public class UserReceiveAddressController extends AbstractBaseController<UserReceiveAddress, Long>  {

	@Autowired
	UserReceiveAddressRepository userReceiveAddressRepository

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

		def count = userReceiveAddressRepository.countByUser(userReceiveAddress.user)

		if (count >= 10) {
			return '{"success": 0, "message": "收获地址过多"}'
		}

		if (count == 0) { // 没有收货地址
			userReceiveAddress.defaultAddress = true
			return userReceiveAddressRepository.save(userReceiveAddress)
		}

		if (!userReceiveAddress.defaultAddress) { // 不是默认收货地址
			return userReceiveAddressRepository.save(userReceiveAddress)
		}

		userReceiveAddressService.modifyAddress(userReceiveAddress)
	}

	/**
	 * 修改收货地址
	 */
	@Override
	@RequestMapping(method = RequestMethod.PUT)
	modify(@RequestBody @Valid UserReceiveAddress userReceiveAddress, BindingResult result) {

		logger.trace ' -- 修改收货地址 -- '

		if(!userReceiveAddress.defaultAddress) {
			userReceiveAddressRepository.save(userReceiveAddress)
		}

		def address = userReceiveAddressRepository.findOne(userReceiveAddress.id)

		if (address.defaultAddress == userReceiveAddress.defaultAddress) {
			userReceiveAddressRepository.save(userReceiveAddress)
		}

		userReceiveAddressService.modifyAddress(userReceiveAddress)

	}

	/**
	 * 删除收货地址
	 */
	@Override
	@RequestMapping(value = '{id}', method = RequestMethod.DELETE)
	void delete(@PathVariable Long id) {

		logger.trace ' -- 删除收货地址 -- '

		userReceiveAddressService.deleteUserReceiveAddress(id)
	}

}
