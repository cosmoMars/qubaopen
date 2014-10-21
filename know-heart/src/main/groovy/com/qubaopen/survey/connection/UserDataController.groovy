package com.qubaopen.survey.connection;

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

import org.apache.tomcat.util.buf.UDecoder;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserGold
import com.qubaopen.survey.entity.user.UserInfo
import com.qubaopen.survey.entity.user.UserUDID
import com.qubaopen.survey.repository.user.UserGoldRepository;
import com.qubaopen.survey.repository.user.UserInfoRepository;
import com.qubaopen.survey.repository.user.UserRepository
import com.qubaopen.survey.repository.user.UserUDIDRepository;
import com.qubaopen.survey.service.user.UserInfoService;
import com.qubaopen.survey.utils.DateCommons

@RestController
@RequestMapping('userData')
public class UserDataController{
	
	@Autowired
	UserRepository userRepository
	
	@Autowired
	UserUDIDRepository userUDIDRepository
	
	@Autowired
	UserGoldRepository userGoldRepository
	
	@Autowired
	UserInfoRepository userInfoRepository

	@Transactional
	@RequestMapping(value = 'getData', method = RequestMethod.GET)
	getData(){
		Class.forName("com.mysql.jdbc.Driver");
		
		Connection conn = DriverManager.getConnection("jdbc:mysql://115.28.210.110:3306/survey", "surveyadmin", "x7d91jd9lkx81");
		def sql = "select u.yhm,u.mm,u.sjhm, u.nc,u.yx, ui.xm,ui.xb,ui.xx,ui.csrq,ui.is_xlwb,ui.is_txwb,ui.is_pyq,ui.is_qqkj,ui.is_wx,ui.is_mzgkwj,ui.is_sllms,ui.is_gkdt,ui.is_hygkwj from yh_z u left join yh_xx_s ui on u.id = ui.yh_z_id where u.is_jh =1";

		println sql
		PreparedStatement pstmt = conn.prepareStatement(sql) ;
		ResultSet resultSet = pstmt.executeQuery();
		
		def users = userRepository.findAll()
		
		def userBs = [], userUdids = [], userGolds = [], userInfos = []
		
		
		while (resultSet.next()) {
			def phone = resultSet.getString('sjhm')
			def user = new User(
				userName : resultSet.getString('yhm') ?: null,
				password : resultSet.getString('mm') ?: null,
				phone : phone,
				email : resultSet.getString('yx') ?: null,
				activated : true
			)
			
			def userP = users.findAll() { 
				it.phone == phone
			}
			
			if (!userP) {
				userBs << user
				
				def sex = resultSet.getString('xb')
				def sexResult
				switch (sex) {
					case 0 : sexResult = UserInfo.Sex.MALE
					case 1 : sexResult = UserInfo.Sex.FEMALE
					case 2 : sexResult = UserInfo.Sex.OTHER
					default : sexResult = null
				}
				
				def bloodType = resultSet.getString('xx')
				def type
				
				switch (bloodType) {
					case 0 : type = UserInfo.BloodType.A
					case 1 : type = UserInfo.BloodType.B
					case 2 : type = UserInfo.BloodType.O
					case 3 : type = UserInfo.BloodType.AB
					case 4 : type = UserInfo.BloodType.OTHER
					default : type = null
				}
				
			
				def userInfo = new UserInfo(
					id : user.id,
					name : resultSet.getString('xm'),
					nickName : resultSet.getString('nc'),
					sex : sexResult,
					bloodType : type,
					birthday : DateCommons.String2Date(resultSet.getString('csrq'), "yyyy-MM-dd"),
					sharedSina : resultSet.getString('is_xlwb') ?: true,
					sharedTencent : resultSet.getString('is_txwb') ?: true,
					sharedWeChatFriend : resultSet.getString('is_pyq') ?: true,
					sharedQQSpace : resultSet.getString('is_qqkj') ?: true,
					sharedWeChat : resultSet.getString('is_wx') ?: true,
					saveFlow : resultSet.getString('is_sllms') ?: true,
					publicAnswersToChief : resultSet.getString('is_mzgkwj') ?: true,
					publicMovementToFriend : resultSet.getString('is_gkdt') ?: true,
					publicAnswersToFriend : resultSet.getString('is_hygkwj') ?: true,
					friendNum : 0
				)
				userInfos << userInfo
			
				def userUdid = new UserUDID(
					id : user.id,
					push : true,
					startTime : DateCommons.String2Date('09:00','HH:mm'),
					endTime : DateCommons.String2Date('22:00','HH:mm')
				)
				userUdids << userUdid
				
				def userGold = new UserGold(
					id : user.id
				)
				userGolds << userUdid
			
			}
		
		}
		userRepository.save(userBs)
		userInfoRepository.save(userInfos)
		userUDIDRepository.save(userUdids)
		userGoldRepository.save(userGolds)
		conn.close();
		
		pstmt.close();
		resultSet.close();
		println userBs.size()
		
		'success'
	}
	
}
