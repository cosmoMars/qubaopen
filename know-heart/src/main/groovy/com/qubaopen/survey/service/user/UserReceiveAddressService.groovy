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

	@Transactional
	modifyAddress(UserReceiveAddress userReceiveAddress) {

		def addressList = userReceiveAddressRepository.findByUserAndIsDefaultAddress(userReceiveAddress.user, true)

		addressList.each {
			it.defaultAddress = false
		}

		userReceiveAddressRepository.save(userReceiveAddress)
	}

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
