package com.qubaopen.survey.service.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.qubaopen.survey.entity.user.UserReceiveAddress
import com.qubaopen.survey.repository.user.UserReceiveAddressRepository

@Service
public class UserReceiveAddressService {

	@Autowired
	UserReceiveAddressRepository userReceiveAddressRepository

	/**
	 * 新增收获地址
	 * @param userReceiveAddress
	 * @return
	 */
	@Transactional
	add(UserReceiveAddress userReceiveAddress) {
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

		modifyAddress(userReceiveAddress)

	}

	/**
	 * 修改收获地址
	 * @param userReceiveAddress
	 * @return
	 */
	@Transactional
	modify(UserReceiveAddress userReceiveAddress) {
		if(!userReceiveAddress.defaultAddress) {
			userReceiveAddressRepository.save(userReceiveAddress)
		}

		def address = userReceiveAddressRepository.findOne(userReceiveAddress.id)

		if (address.defaultAddress == userReceiveAddress.defaultAddress) {
			userReceiveAddressRepository.save(userReceiveAddress)
		}

		modifyAddress(userReceiveAddress)

	}
	/**
	 * @param userReceiveAddress
	 * @return
	 */
	@Transactional
	modifyAddress(UserReceiveAddress userReceiveAddress) {

		def addressList = userReceiveAddressRepository.findByUserAndDefaultAddress(userReceiveAddress.user, true)

		addressList.each {
			it.defaultAddress = false
		}

		userReceiveAddressRepository.save(userReceiveAddress)
	}

	/**
	 * 删除地址
	 * @param id
	 * @return
	 */
	@Transactional
	deleteUserReceiveAddress(long id) {
		def userReceiveAddress = userReceiveAddressRepository.findOne(id)
		if (userReceiveAddress && !userReceiveAddress.defaultAddress) {
			userReceiveAddressRepository.delete(userReceiveAddress)
			return
		}

		def userReceiveAddresses = userReceiveAddressRepository.findByUser(userReceiveAddress.user)
		if (userReceiveAddresses.size == 1) {
			userReceiveAddressRepository.delete(userReceiveAddress)
			return
		}

		userReceiveAddresses.remove(userReceiveAddress)
		userReceiveAddressRepository.delete(userReceiveAddress)
		userReceiveAddresses[0].defaultAddress = true
	}

}
