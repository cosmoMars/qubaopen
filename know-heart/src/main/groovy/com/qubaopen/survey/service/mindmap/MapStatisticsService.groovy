package com.qubaopen.survey.service.mindmap

import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.fasterxml.jackson.databind.ObjectMapper
import com.qubaopen.survey.entity.mindmap.MapRecord
import com.qubaopen.survey.entity.self.SelfManagementType
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.EPQBasicRepository
import com.qubaopen.survey.repository.mindmap.MapCoefficientRepository;
import com.qubaopen.survey.repository.mindmap.MapRecordRepository
import com.qubaopen.survey.repository.mindmap.MapStatisticsRepository
import com.qubaopen.survey.repository.mindmap.MapStatisticsTypeRepository
import com.qubaopen.survey.repository.self.SelfGroupRepository
import com.qubaopen.survey.repository.self.SelfRepository
import com.qubaopen.survey.repository.self.SelfResultOptionRepository
import com.qubaopen.survey.service.user.UserIDCardBindService

@Service
public class MapStatisticsService {

	@Autowired
	MapStatisticsRepository mapStatisticsRepository

	@Autowired
	MapStatisticsTypeRepository mapStatisticsTypeRepository

	@Autowired
	SelfGroupRepository selfGroupRepository

	@Autowired
	ObjectMapper objectMapper
	
	@Autowired
	CalculateT calculateT
	
	@Autowired
	UserIDCardBindService userIDCardBindService
	
	@Autowired
	EPQBasicRepository epqBasicRepository
	
	@Autowired
	SelfRepository selfRepository

	@Autowired
	MapRecordRepository mapRecordRepository
	
	@Autowired
	CalculatePoint calculatePoint
	
	@Autowired
	SelfResultOptionRepository selfResultOptionRepository
	
	@Autowired
	MapCoefficientRepository mapCoefficientRepository
	
	/**
	 * 获取心理地图
	 * @param user
	 * @param type
	 * @return
	 */
	@Transactional
	newRetrieveMapStatistics(User user, Long typeId) {

		def data = [], existMaps = []
			
//		def specialSelf = selfRepository.findSpecialSelf()
		def	specialGroup = selfGroupRepository.findSpecialSelfGroup()
		
		if (typeId != null) {
			
			def selfManagementType = new SelfManagementType(id : typeId)
//			def specialMaps = mapStatisticsRepository.findOneByFilters(
//					[
//						self_equal : specialSelf,
//						user_equal : user,
//						selfManagementType_equal : selfManagementType
//					]
//				)
			
//			if (specialMaps) {
//				existMaps += specialMaps
//			}
			def groupMaps = mapStatisticsRepository.findMapWithoutSpecialGroup(selfManagementType, user, specialGroup)
//			existMaps += existGroupMaps
			
			def singleMaps = []
//			if (existMaps) {
//				singleMaps = mapStatisticsRepository.findMapWithoutExists(existMaps, selfManagementType, user)
//			} else {
//				singleMaps = mapStatisticsRepository.findBySelfManagementTypeAndUser(selfManagementType, user)
//			}
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
				if (v.size() < k.selfs.size() && v != null) {
					
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
				} else if (v.size() == 1 && k.selfs.size() == 1) {
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
						def resultStr = "根据您对EPQ量表的测试，经过量表的标准分数换算得到你的性格偏向，如上图"
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
				
				
				if ((it?.self?.id == 13l || it?.self?.id == 10l) && selfResultCount < 1) {
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
				}
				
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
		
//			def specialMaps = mapStatisticsRepository.findOneByFilters(
//				[
//					self_equal : specialSelf,
//					user_equal : user
//				]
//			)// 4小时题目
//			if (specialMaps) {
//				existMaps += specialMaps
//			}
			def groupMaps = mapStatisticsRepository.findMapWithoutSpecialGroup(user, specialGroup)
			
//			existMaps += groupMaps
			def singleMaps = []
			
//			if (existMaps) {
//				singleMaps = mapStatisticsRepository.findMapWithoutExists(existMaps, user)
//			} else {
//				singleMaps = mapStatisticsRepository.findAll(
//					['user_equal' : user]
//				)
//			}
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
				if (v.size() < k.selfs.size() && v != null) {
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
				} else if (v.size() == 1 && k.selfs.size() == 1) {
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
						def resultStr = "根据您对EPQ量表的测试，经过量表的标准分数换算得到你的性格偏向，如上图"
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
				
				if ((it?.self?.id == 13l || it?.self?.id == 10l) && selfResultCount < 1) {
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
				}
				
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
	
	@Transactional
	retrieveSpecialMap(User user) {
		def data = []
		def specialSelf = selfRepository.findSpecialSelf()
		def specialMap = mapStatisticsRepository.findOneByFilters(
			[
				self_equal : specialSelf,
				user_equal : user
			]
		)// 4小时题目
		def specialMapRecords
		if (specialMap) {
			specialMapRecords = mapRecordRepository.findEveryDayMapRecords(specialMap)
		}
		
		if (specialMap && specialMapRecords.size() >= 7) { // 特殊题
			def chart
			def c = []
			def resultContent = "err"
			if (specialMap?.self?.graphicsType) {
				def timeChart = [], paChart = [], naChart = [], midChart = []
//					def mapRecords = specialMaps.mapRecords as List
				Collections.sort(specialMapRecords, new MapRecordComparator())
				specialMapRecords.each {
					def thatTime = new Date(it.name as long),
						time = DateUtils.parseDate(DateFormatUtils.format(thatTime, 'yyyy-MM-dd') + ' 12:00', 'yyyy-MM-dd HH:mm')
					timeChart << time.getTime()
					paChart << it.value
					naChart << -it.naValue
					midChart << (it.value - it.naValue)
				}

				def paChartC = [], naChartC = [], midChartC = []
				
				def coefficient = mapCoefficientRepository.findOne(user.id)
				
				if (coefficient) {
					def chartTime = new Date(timeChart[timeChart.size() - 1] as long) 
					if (chartTime - coefficient.time >= 0) {
						paChartC = calculatePoint.getPoint(timeChart, paChart)
						naChartC = calculatePoint.getPoint(timeChart, naChart)
						midChartC = calculatePoint.getPoint(timeChart, midChart)
						coefficient.pa1 = paChartC[0]
						coefficient.pa2 = paChartC[1]
						coefficient.pa3 = paChartC[2]
						coefficient.pa4 = paChartC[3]
						coefficient.na1 = naChartC[0]
						coefficient.na2 = naChartC[1]
						coefficient.na3 = naChartC[2]
						coefficient.na4 = naChartC[3]
						coefficient.mid1 = midChartC[0]
						coefficient.mid2 = midChartC[1]
						coefficient.mid3 = midChartC[2]
						coefficient.mid4 = midChartC[3]
						coefficient.time = chartTime
						mapCoefficientRepository.save(coefficient)
					}
				} else {
					paChartC = calculatePoint.getPoint(timeChart, paChart)
					naChartC = calculatePoint.getPoint(timeChart, naChart)
					midChartC = calculatePoint.getPoint(timeChart, midChart)
					coefficient.pa1 = paChartC[0]
					coefficient.pa2 = paChartC[1]
					coefficient.pa3 = paChartC[2]
					coefficient.pa4 = paChartC[3]
					coefficient.na1 = naChartC[0]
					coefficient.na2 = naChartC[1]
					coefficient.na3 = naChartC[2]
					coefficient.na4 = naChartC[3]
					coefficient.mid1 = midChartC[0]
					coefficient.mid2 = midChartC[1]
					coefficient.mid3 = midChartC[2]
					coefficient.mid4 = midChartC[3]
					coefficient.time = new Date(timeChart[timeChart.size() - 1] as long)
					mapCoefficientRepository.save(coefficient)
				}
				
				//计算 正负情感趋势 上升 下降
				def time=System.currentTimeMillis() 
				def timeBefore = time - 60 * 60 * 24 * 1000 * 2
				def timeAfter = time + 60 * 60 * 24 * 1000 * 2
				
				def resultToday = coefficient.mid1 + coefficient.mid2 * Math.cos(time * coefficient.mid4) + coefficient.mid3 * Math.sin(time * coefficient.mid4)
				def resultBefore = coefficient.mid1 + coefficient.mid2 * Math.cos(timeBefore * coefficient.mid4) + coefficient.mid3 * Math.sin(timeBefore * coefficient.mid4)
				def resultAfter = coefficient.mid1 + coefficient.mid2 * Math.cos(timeAfter * coefficient.mid4) + coefficient.mid3 * Math.sin(timeAfter * coefficient.mid4)
								
				if( resultBefore <= resultToday  && resultToday<resultAfter){
					resultContent = "up"
				}else if( resultBefore > resultToday  && resultToday >=resultAfter){
					resultContent = "down"
				}else if( resultBefore <= resultToday  && resultToday >=resultAfter){
					resultContent = "top"
				}else if( resultBefore > resultToday  && resultToday < resultAfter){
					resultContent = "bottom"
				}
				
				chart = [
					timeChart : timeChart,
					midChart : midChart,
					paChartC : [coefficient.pa1, coefficient.pa2, coefficient.pa3, coefficient.pa4],
					naChartC : [coefficient.na1, coefficient.na2, coefficient.na3, coefficient.na4],
					midChartC : [coefficient.mid1, coefficient.mid2, coefficient.mid3, coefficient.mid4]
				]
				
				c << chart
			}
			data << [
				'groupId' : specialMap?.self?.selfGroup?.id,
				'mapTitle' : specialMap?.self?.title,
				'chart' : c,
				'mapMax' : specialMap?.mapMax,
				'resultName' : specialMap?.selfResultOption?.name,
				'resultScore' : '',
				'resultContent' : resultContent,
				'managementType' : specialMap?.selfManagementType?.id,
				'recommendedValue' : specialMap?.recommendedValue,
				'graphicsType' : specialMap?.self?.graphicsType?.id,
				'special' : true,
				'lock' : false
			]
		} else if (specialMap && specialMapRecords.size() < 7) {
			data << [
				'groupId' : specialMap?.self?.selfGroup?.id,
				'mapTitle' : specialMap?.self?.title,
				'chart' : '',
				'mapMax' : '',
				'resultName' : '',
				'resultScore' : '',
				'resultContent' : '',
				'managementType' : specialMap?.selfManagementType?.id,
				'recommendedValue' : specialMap?.recommendedValue,
				'graphicsType' : specialMap?.self?.graphicsType?.id,
				'special' : true,
				'lock' : true,
				'tips' : "该问卷需要答满7天方可得出结果，您已完成［${specialMapRecords.size()}］天" as String
			]
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
	@Transactional
	retrieveMapStatistics(User user, long typeId) {

		def data = [], existMaps = []
			
		def om = new ObjectMapper()
		def specialSelf = selfRepository.findSpecialSelf()
		if (typeId == 4l) {
			
			def specialMaps = mapStatisticsRepository.findOneByFilters(
				[
					self_equal : specialSelf,
					user_equal : user
				]
			)// 4小时题目
			def specialMapRecords
			if (specialMaps) {
				existMaps += specialMaps
				specialMapRecords = mapRecordRepository.findEveryDayMapRecords(specialMaps)
			}
			def existGroupMaps = mapStatisticsRepository.findMapByGroupSelfs(user)
			
			existMaps += existGroupMaps
			def singleMaps
			
			if (existMaps) {
				singleMaps = mapStatisticsRepository.findMapWithoutExists(existMaps, user)
			} else {
				singleMaps = mapStatisticsRepository.findAll(
					['user_equal' : user]
				)
			}
			
			if (specialMaps && specialMapRecords.size() >= 7) { // 特殊题
				def chart
				def c = []
				if (specialMaps?.self?.graphicsType) {
					def timeChart = [], paChart = [], naChart = [], midChart = []
//					def mapRecords = specialMaps.mapRecords as List
					Collections.sort(specialMapRecords, new MapRecordComparator())
					specialMapRecords.each {
						def thatTime = new Date(it.name as long),
							time = DateUtils.parseDate(DateFormatUtils.format(thatTime, 'yyyy-MM-dd') + ' 12:00', 'yyyy-MM-dd HH:mm')
						timeChart << time.getTime()
						paChart << it.value
						naChart << -it.naValue
						midChart << (it.value - it.naValue)
					}
					
					def paChartC = calculatePoint.getPoint(timeChart, paChart)
					def naChartC = calculatePoint.getPoint(timeChart, naChart)
					def midChartC = calculatePoint.getPoint(timeChart, midChart)
					
					chart = [
						timeChart : timeChart,
						midChart : midChart,
						paChartC : paChartC,
						naChartC : naChartC,
						midChartC : midChartC
					]
					
					c << chart
				}
				data << [
					'mapTitle' : specialMaps?.self?.title,
					'chart' : c,
					'mapMax' : specialMaps?.mapMax,
					'resultName' : specialMaps?.selfResultOption?.name,
					'resultScore' : '',
					'resultContent' : '',
					'managementType' : specialMaps?.selfManagementType?.id,
					'recommendedValue' : specialMaps?.recommendedValue,
					'graphicsType' : specialMaps?.self?.graphicsType?.id,
					'special' : true,
					'lock' : false
				]
			} else if (specialMaps && specialMapRecords.size() < 7) {
				data << [
					'mapTitle' : specialMaps?.self?.title,
					'chart' : '',
					'mapMax' : '',
					'resultName' : '',
					'resultScore' : '',
					'resultContent' : '',
					'managementType' : specialMaps?.selfManagementType?.id,
					'recommendedValue' : specialMaps?.recommendedValue,
					'graphicsType' : specialMaps?.self?.graphicsType?.id,
					'special' : true,
					'lock' : true,
					'tips' : "该问卷需要答满7天方可得出结果，您已完成［${specialMapRecords.size()}］天" as String
				]
			}
			
			def groupResultMaps = [:]
			
			existGroupMaps.each {
				if (groupResultMaps.get(it.self.selfGroup)) {
					groupResultMaps.get(it.self.selfGroup) << it
				} else {
					def tempMap = []
					tempMap << it
					groupResultMaps.put(it.self.selfGroup, tempMap)
				}
			}
			
			groupResultMaps.each { k, v -> // k -> selfGroup, v -> map
				if (v.size() < k.selfs.size() && v != null) {
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
				} else if (v.size() == k.selfs.size()) {
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
						def resultStr = "根据您对EPQ量表的测试，经过量表的标准分数换算得到你的性格偏向，如上图"
						if (recordMaps.get('L') > 60) {
							resultStr = '由于量表中测谎题总分过高，故此次结果具有不针对性' as String
						}
						def level = calculateT.calLevel(recordMaps.get('E'), recordMaps.get('N'))
						data << [
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
				
				if ((it?.self?.id == 13l || it?.self?.id == 10l) && selfResultCount < 1) {
					selfResultCount ++
					def selfResult = selfResultOptionRepository.findOne(145l)
					data << [
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
				}
				
				data << [
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
			def selfManagementType = new SelfManagementType(id : typeId)
			def specialMaps = mapStatisticsRepository.findOneByFilters(
					[
						self_equal : specialSelf,
						user_equal : user,
						selfManagementType_equal : selfManagementType
					]
				)
			
			def specialMapRecords
			if (specialMaps) {
				existMaps += specialMaps
				specialMapRecords = mapRecordRepository.findEveryDayMapRecords(specialMaps)
			}
			def existGroupMaps = mapStatisticsRepository.findMapByGroupSelfs(selfManagementType, user)
			existMaps += existGroupMaps
			
			def singleMaps
			if (existMaps) {
				singleMaps = mapStatisticsRepository.findMapWithoutExists(existMaps, selfManagementType, user)
			} else {
				singleMaps = mapStatisticsRepository.findBySelfManagementTypeAndUser(selfManagementType, user)
			}
			if (specialMaps && specialMapRecords.size() >= 7) { // 特殊题
				def chart
				def c = []
				if (specialMaps?.self?.graphicsType) {
					def timeChart = [], paChart = [], naChart = [], midChart = []
//					def mapRecords = specialMaps.mapRecords as List
					Collections.sort(specialMapRecords, new MapRecordComparator())
					specialMapRecords.each {
						def thatTime = new Date(it.name as long),
							time = DateUtils.parseDate(DateFormatUtils.format(thatTime, 'yyyy-MM-dd') + ' 12:00', 'yyyy-MM-dd HH:mm')
						timeChart << time.getTime()
						paChart << it.value
						naChart << -it.naValue
						midChart << (it.value - it.naValue)
					}
						def paChartC = calculatePoint.getPoint(timeChart, paChart)
					def naChartC = calculatePoint.getPoint(timeChart, naChart)
					def midChartC = calculatePoint.getPoint(timeChart, midChart)
					
					chart = [
						timeChart : timeChart,
						midChart : midChart,
						paChartC : paChartC,
						naChartC : naChartC,
						midChartC : midChartC
					]
					c << chart
				}
				data << [
					'mapTitle' : specialMaps?.self?.title,
					'chart' : c,
					'mapMax' : specialMaps?.mapMax,
					'resultName' : specialMaps?.selfResultOption?.name,
					'resultScore' : '',
					'resultContent' : '',
					'managementType' : specialMaps?.selfManagementType?.id,
					'recommendedValue' : specialMaps?.recommendedValue,
					'graphicsType' : specialMaps?.self?.graphicsType?.id,
					'special' : true,
					'lock' : false
				]
			} else if (specialMaps && specialMapRecords.size() < 7) {
				data << [
					'mapTitle' : specialMaps.self.title,
					'chart' : '',
					'mapMax' : '',
					'resultName' : '',
					'resultScore' : '',
					'resultContent' : '',
					'managementType' : specialMaps?.selfManagementType?.id,
					'recommendedValue' : specialMaps?.recommendedValue,
					'graphicsType' : specialMaps?.self?.graphicsType?.id,
					'special' : true,
					'lock' : true,
					'tips' : "该问卷需要答满7天方可得出结果，您已完成［${specialMapRecords.size()}］天" as String
				]
			}
			
			def groupResultMaps = [:]
			
			existGroupMaps.each {
				if (groupResultMaps.get(it.self.selfGroup)) {
					groupResultMaps.get(it.self.selfGroup) << it
				} else {
					def tempMap = []
					tempMap << it
					groupResultMaps.put(it.self.selfGroup, tempMap)
				}
			}
			
			groupResultMaps.each { k, v -> // k -> selfGroup, v -> map
				if (v.size() < k.selfs.size() && v != null) {
					
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
				} else if (v.size() == k.selfs.size()) {
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
						def resultStr = "根据您对EPQ量表的测试，经过量表的标准分数换算得到你的性格偏向，如上图"
						if (recordMaps.get('L') > 60) {
							resultStr = '由于量表中测谎题总分过高，故此次结果具有不针对性' as String
						}
						data << [
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
				
				
				if ((it?.self?.id == 13l || it?.self?.id == 10l) && selfResultCount < 1) {
					selfResultCount ++
					def selfResult = selfResultOptionRepository.findOne(145l)
					data << [
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
				}
				
				data << [
					
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
}
