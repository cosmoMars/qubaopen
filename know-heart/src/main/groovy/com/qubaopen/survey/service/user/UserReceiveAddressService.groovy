package com.qubaopen.survey.service.user

import static com.qubaopen.survey.utils.ValidateUtil.*

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserReceiveAddress
import com.qubaopen.survey.repository.base.AreaCodeRepository;
import com.qubaopen.survey.repository.user.UserReceiveAddressRepository

@Service
public class UserReceiveAddressService {

	@Autowired
	UserReceiveAddressRepository userReceiveAddressRepository

	@Autowired
	AreaCodeRepository areaCodeRepository
	
	/**
	 * 新增收获地址
	 * @param userReceiveAddress
	 * @return
	 */
	@Transactional
	add(UserReceiveAddress userReceiveAddress) {
		def count = userReceiveAddressRepository.countByUser(userReceiveAddress.user)

		if (count >= 10) {
			return '{"success": "0", "message": "err401"}'
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

		if (userReceiveAddress.phone && !validatePhone(userReceiveAddress.phone)) {
			return '{"success" : "0", "message": "err003"}'
		}

		if (!userReceiveAddress.defaultAddress) {
			userReceiveAddressRepository.modify(userReceiveAddress)
			return '{"success": "1"}'
		}

		def address = userReceiveAddressRepository.findOne(userReceiveAddress.id)
		if (!address) {
			return '{"success": "0", "message": "err402"}'
		}

		if (address.defaultAddress == userReceiveAddress.defaultAddress) {
			userReceiveAddressRepository.modify(userReceiveAddress)
			return '{"success": "1"}'
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
			userReceiveAddressRepository.save(it);
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

		if(!userReceiveAddress) {
			return '{"success": "0", "message": "err402"}'
		}

		if (userReceiveAddress && !userReceiveAddress.defaultAddress) {
			userReceiveAddressRepository.delete(userReceiveAddress)
			return '{"success": "1"}'
		}

		def userReceiveAddresses = userReceiveAddressRepository.findByUser(userReceiveAddress.user)
		if (userReceiveAddresses.size == 1) {
			userReceiveAddressRepository.delete(userReceiveAddress)
			return '{"success": "1"}'
		}

		userReceiveAddresses.remove(userReceiveAddress)
		userReceiveAddressRepository.delete(userReceiveAddress)
		userReceiveAddresses[0].defaultAddress = true

		'{"success": "1"}'
	}

	/**
	 * 获取地址列表
	 * @param id
	 * @return
	 */
	@Transactional
	getUserReceiveAddressList(User user) {
		def addressList = userReceiveAddressRepository.findByUser(user)
		
		def content=[]
		addressList.each {
			def data = [ 
				"id":it.id,
				"phone":it.phone,
				"postCode":it.postCode,
				"consignee":it.consignee,
				"detialAddress":it.detialAddress,
				"defaultAddress":it.defaultAddress
			]
			def area;
			def codeArray=[];
			if(it.areaCode){
				area=it.areaCode;
				codeArray << area.code;
				if(area.parent){
					area=area.parent;
					codeArray << area.code;
					if(area.parent){
						area=area.parent;
						codeArray << area.code;
					}
				}
				codeArray.sort();
				for(int i=0;i<codeArray.size();i++){
					if(i==0)
						data << ["firstCode":codeArray.get(0)]
					if(i==1)
						data << ["secondCode":codeArray.get(1)]
					if(i==2)
						data << ["thirdCode":codeArray.get(2)]
				}
				content << data
			}
		}

		[
			'success' : '1',
			'message' : '成功',
			'content' : content
		]
		
	}

}
