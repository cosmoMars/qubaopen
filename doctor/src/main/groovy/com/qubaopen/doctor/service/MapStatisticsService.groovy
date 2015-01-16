package com.qubaopen.doctor.service

import org.apache.commons.lang3.time.DateFormatUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.qubaopen.doctor.controller.user.UserIDCardBindService
import com.qubaopen.doctor.repository.map.CalculateT
import com.qubaopen.doctor.repository.map.EPQBasicRepository;
import com.qubaopen.doctor.repository.map.MapStatisticsRepository
import com.qubaopen.doctor.repository.map.SelfGroupRepository
import com.qubaopen.doctor.repository.map.SelfResultOptionRepository
import com.qubaopen.doctor.repository.user.UserIDCardBindRepository
import com.qubaopen.survey.entity.mindmap.MapRecord
import com.qubaopen.survey.entity.self.SelfManagementType
import com.qubaopen.survey.entity.user.User

@Service
public class MapStatisticsService {

	@Autowired
	MapStatisticsRepository mapStatisticsRepository

	@Autowired
	SelfResultOptionRepository selfResultOptionRepository
	
	@Autowired
	UserIDCardBindService userIDCardBindService
	
	@Autowired
	CalculateT calculateT
	
	@Autowired
	SelfGroupRepository selfGroupRepository

	@Autowired
	UserIDCardBindRepository userIDCardBindRepository
	
	@Autowired
	EPQBasicRepository epqBasicRepository
	
	/**
	 * 获取心理地图
	 * @param user
	 * @param type
	 * @return
	 */
	@Transactional
	newRetrieveMapStatistics(User user, Long typeId) {

		def data = [], existMaps = []
			
		def	specialGroup = selfGroupRepository.findSpecialSelfGroup()
		
		if (typeId != null) {
			
			def selfManagementType = new SelfManagementType(id : typeId)
			def groupMaps = mapStatisticsRepository.findMapWithoutSpecialGroup(selfManagementType, user, specialGroup)
			def singleMaps = []
			def groupResultMaps = [:]
			groupMaps.each {
				if (groupResultMaps.get(it.self.selfGroup)) {
					groupResultMaps.get(it.self.selfGroup) << it
				} else {
					def tempMap = []
					tempMap << it
					groupResultMaps.put(it.self.selfGroup, tempMap)
				}
			}
			
			groupResultMaps.each { k, v -> // k -> selfGroup, v -> map
				/*if (v.size() < k.selfs.size() && v != null) {
					
					def allName = []
					def completeName = [] as Set
					k.selfs.each {
						allName << it.abbreviation
					}
					v.each { s ->
						s.mapRecords.each {
							if (allName.contains(it.name)) {
								completeName << it.name
							}
						}
					}
					def strName = "本问卷共［${allName.join(",")}］ $allName.size 套问卷， 您已完成［${completeName.join(",")}］问卷，请完成其他问卷得出结果" as String
					data << [
						'groupId' : k?.id,
						'mapTitle' : k?.title,
						'chart' : '',
						'mapMax' : k?.mapMax,
						'resultName' : '',
						'resultScore' : '',
						'resultContent' : '',
						'managementType' : k?.selfManagementType?.id,
						'recommendedValue' : k?.recommendedValue,
						'graphicsType' : k?.graphicsType?.id,
						'special' : false,
						'lock' : true,
						'tips' : strName
					]
				} else */
				if (v.size() == 1 && k.selfs.size() == 1) {
					singleMaps += v
				} else if (v.size() == k.selfs.size() && k.selfs.size() > 1) {
					def records = []
					def recordMaps = [:]
					def chart = []
					if (k.name == 'EPQ') {
						v.each { s ->
							records += s.mapRecords
						}
						
						v.each { s ->
							s.mapRecords.each {
								if (recordMaps.get(it.name)) {
									recordMaps.get(it.name) << it
								} else {
									def tempList = []
									tempList << it
									recordMaps.put(it.name, tempList)
								}
							}
						}
						
						recordMaps.each { rk, rv ->
							def score = 0
							rv.each {
								score += it.value
							}
							def idMap = userIDCardBindService.calculateAgeByIdCard(user)
							
							def age, sex
							if (idMap) {
								age = idMap.get('age')
								sex = idMap.get('sex')
							} else {
								def c = Calendar.getInstance()
								c.setTime new Date()
								age = c.get(Calendar.YEAR) - (DateFormatUtils.format(user.userInfo.birthday, 'yyyy') as int)
								sex = user.userInfo.sex.ordinal()
							}
							
							def epqBasic = epqBasicRepository.findOneByFilters(
								[
									'minAge_lessThanOrEqualTo' : age,
									'maxAge_greaterThanOrEqualTo' : age,
									'sex_equal' : sex,
									'name_equal' : rk
								]
							)
							def tScore = calculateT.calT(score, epqBasic.mValue, epqBasic.sdValue)
							recordMaps.get(rk).clear()
							recordMaps.put(rk, tScore)
						}
						
						def level = calculateT.calLevel(recordMaps.get('E'), recordMaps.get('N'))
						def resultStr = "根据您对EPQ量表的测试，经过量表的标准分数换算得到该用户的性格偏向，如上图"
						if (recordMaps.get('L') > 60) {
							resultStr = '由于量表中测谎题总分过高，故此次结果具有不针对性' as String
						}
						data << [
							'groupId' : k?.id,
							'mapTitle' : k?.title,
							'chart' : chart,
							'mapMax' : k?.mapMax,
							'resultName' : k?.content,
							'resultScore' : '',
							'resultContent' : resultStr,
							'managementType' : k?.selfManagementType?.id,
							'recommendedValue' : k?.recommendedValue,
							'graphicsType' : k?.graphicsType?.id,
							'special' : false,
							'lock' : false,
							'point' : [E : recordMaps.get('E'), N : recordMaps.get('N')],
							'level' : level
						]
					} else {
						chart = []
						
						if (k?.graphicsType) {
							v.each { s ->
								s.mapRecords.each {
									chart << [name : it.name, value : it.value]
								}
							}
						}
						data << [
							'groupId' : k?.id,
							'mapTitle' : k?.title,
							'chart' : chart,
							'mapMax' : k?.mapMax,
							'resultName' : k?.content,
							'resultScore' : '',
							'resultContent' : '',
							'managementType' : k?.selfManagementType?.id,
							'recommendedValue' : k?.recommendedValue,
							'graphicsType' : k?.graphicsType?.id,
							'special' : false,
							'lock' : false
						]
					}
				}
					
			}
			def selfResultCount = 0
			singleMaps.each { // 单个题目出答案
				def chart = []
				if (it?.self?.graphicsType) {
					it.mapRecords.each {
						chart << [name : it.name, value : it.value]
					}
				}
				
				
				/*if ((it?.self?.id == 13l || it?.self?.id == 10l) && selfResultCount < 1) {
					selfResultCount ++
					def selfResult = selfResultOptionRepository.findOne(145l)
					data << [
						'groupId' : '',
						'mapTitle' : selfResult.title,
						'chart' : [],
						'mapMax' : '',
						'resultName' : selfResult.name,
						'resultScore' : '',
						'resultContent' : selfResult?.content,
						'managementType' : it?.selfManagementType?.id,
						'recommendedValue' : it?.recommendedValue,
						'graphicsType' : '',
						'special' : false,
						'lock' : false,
						'picPath' : ''
					]
				}*/
				
				data << [
					'groupId' : it?.self?.selfGroup?.id,
					'mapTitle' : it?.self?.title,
					'chart' : chart,
					'mapMax' : it?.mapMax,
					'resultName' : it?.selfResultOption?.name,
					'resultScore' : '',
					'resultContent' : it?.selfResultOption?.content,
					'managementType' : it?.selfManagementType?.id,
					'recommendedValue' : it?.recommendedValue,
					'graphicsType' : it?.self?.graphicsType?.id,
					'special' : false,
					'lock' : false,
					'picPath' : it?.selfResultOption?.picPath
				]
			}
		
		} else {
		
			def groupMaps = mapStatisticsRepository.findMapWithoutSpecialGroup(user, specialGroup)
			
			def singleMaps = []
			
			def groupResultMaps = [:]
			
			groupMaps.each {
				if (groupResultMaps.get(it.self.selfGroup)) {
					groupResultMaps.get(it.self.selfGroup) << it
				} else {
					def tempMap = []
					tempMap << it
					groupResultMaps.put(it.self.selfGroup, tempMap)
				}
			}
			
			groupResultMaps.each { k, v -> // k -> selfGroup, v -> map
				/*if (v.size() < k.selfs.size() && v != null) {
					def allName = []
					def completeName = [] as Set
					k.selfs.each {
						allName << it.abbreviation
					}
					v.each { s ->
						s.mapRecords.each {
							if (allName.contains(it.name)) {
								completeName << it.name
							}
						}
					}
					def strName = "本问卷共［${allName.join(",")}］ $allName.size 套问卷， 您已完成［${completeName.join(",")}］问卷，请完成其他问卷得出结果" as String
					data << [
						'groupId' : k?.id,
						'mapTitle' : k?.title,
						'chart' : '',
						'mapMax' : k?.mapMax,
						'resultName' : '',
						'resultScore' : '',
						'resultContent' : '',
						'managementType' : k?.selfManagementType?.id,
						'recommendedValue' : k?.recommendedValue,
						'graphicsType' : k?.graphicsType?.id,
						'special' : false,
						'lock' : true,
						'tips' : strName
					]
				} else*/ 
				if (v.size() == 1 && k.selfs.size() == 1) {
					singleMaps += v
				} else if (v.size() == k.selfs.size() && k.selfs.size() > 1) {
					def records = []
					def recordMaps = [:]
					def chart = []
					if (k.name == 'EPQ') {
						v.each { s ->
							records += s.mapRecords
						}
						
						v.each { s ->
							s.mapRecords.each {
								if (recordMaps.get(it.name)) {
									recordMaps.get(it.name) << it
								} else {
									def tempList = []
									tempList << it
									recordMaps.put(it.name, tempList)
								}
							}
						}
						
						recordMaps.each { rk, rv ->
							def score = 0
							rv.each {
								score += it.value
							}
							
							def idMap = userIDCardBindService.calculateAgeByIdCard(user)
							
							def age, sex
							if (idMap) {
								age = idMap.get('age')
								sex = idMap.get('sex')
							} else {
								def c = Calendar.getInstance()
								c.setTime new Date()
								age = c.get(Calendar.YEAR) - (DateFormatUtils.format(user.userInfo.birthday, 'yyyy') as int)
								sex = user.userInfo.sex.ordinal()
							}
							
							def epqBasic = epqBasicRepository.findOneByFilters(
								[
									'minAge_lessThanOrEqualTo' : age,
									'maxAge_greaterThanOrEqualTo' : age,
									'sex_equal' : sex,
									'name_equal' : rk
								]
							)
							def tScore = calculateT.calT(score, epqBasic.mValue, epqBasic.sdValue)
							recordMaps.get(rk).clear()
							recordMaps.put(rk, tScore)
							
						}
						def resultStr = "根据您对EPQ量表的测试，经过量表的标准分数换算得到该用户的性格偏向，如上图"
						if (recordMaps.get('L') > 60) {
							resultStr = '由于量表中测谎题总分过高，故此次结果具有不针对性' as String
						}
						def level = calculateT.calLevel(recordMaps.get('E'), recordMaps.get('N'))
						data << [
							'groupId' : k?.id,
							'mapTitle' : k?.title,
							'chart' : chart,
							'mapMax' : k?.mapMax,
							'resultName' : k?.content,
							'resultScore' : '',
							'resultContent' : resultStr,
							'managementType' : k?.selfManagementType?.id,
							'recommendedValue' : k?.recommendedValue,
							'graphicsType' : k?.graphicsType?.id,
							'special' : false,
							'lock' : false,
							'point' : [E : recordMaps.get('E'), N : recordMaps.get('N')],
							'level' : level
						]
					} else {
						chart = []
						if (k?.graphicsType) {
							v.each { s ->
								s.mapRecords.each {
									chart << [name : it.name, value : it.value]
								}
							}
						}
		
						data << [
							'groupId' : k?.id,
							'mapTitle' : k?.title,
							'chart' : chart,
							'mapMax' : k?.mapMax,
							'resultName' : k?.content,
							'resultScore' : '',
							'resultContent' : '',
							'managementType' : k?.selfManagementType?.id,
							'recommendedValue' : k?.recommendedValue,
							'graphicsType' : k?.graphicsType?.id,
							'special' : false,
							'lock' : false
						]
					}
				}
			}
			def selfResultCount = 0
			singleMaps.each { // 单个题目出答案
				def chart = []
				if (it?.self?.graphicsType) {
					it.mapRecords.each {
						chart << [name : it.name, value : it.value]
					}
				}
				
				/*if ((it?.self?.id == 13l || it?.self?.id == 10l) && selfResultCount < 1) {
					selfResultCount ++
					def selfResult = selfResultOptionRepository.findOne(145l)
					data << [
						'groupId' : '',
						'mapTitle' : selfResult.title,
						'chart' : [],
						'mapMax' : '',
						'resultName' : selfResult.name,
						'resultScore' : '',
						'resultContent' : selfResult?.content,
						'managementType' : it?.selfManagementType?.id,
						'recommendedValue' : it?.recommendedValue,
						'graphicsType' : '',
						'special' : false,
						'lock' : false,
						'picPath' : ''
					]
				}*/
				
				data << [
					'groupId' : it?.self?.selfGroup?.id,
					'mapTitle' : it?.self?.title,
					'chart' : chart,
					'mapMax' : it?.mapMax,
					'resultName' : it?.selfResultOption?.name,
					'resultScore' : '',
					'resultContent' : it?.selfResultOption?.content,
					'managementType' : it?.selfManagementType?.id,
					'recommendedValue' : it?.recommendedValue,
					'graphicsType' : it?.self?.graphicsType?.id,
					'special' : false,
					'lock' : false,
					'picPath' : it?.selfResultOption?.picPath
				]
			}
		}
		[
			'success' : '1',
			'message' : '成功',
			'userId' : user.id,
			'data' : data
		]
	}
	
	class MapRecordComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			MapRecord m1 = (MapRecord) o1
			MapRecord m2 = (MapRecord) o2
			return m1.createdDate.compareTo(m2.createdDate)
		}
	}
	
}
