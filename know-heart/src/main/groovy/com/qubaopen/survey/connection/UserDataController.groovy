package com.qubaopen.survey.connection;

import java.sql.Connection
import java.sql.Date
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserInfo
import com.qubaopen.survey.repository.user.UserRepository
import com.qubaopen.survey.utils.DateCommons;

@RestController
@RequestMapping('userData')
public class UserDataController{
	
	@Autowired
	UserRepository userRepository

	@RequestMapping(value = 'getData', method = RequestMethod.GET)
	getData(){
		Class.forName("com.mysql.jdbc.Driver");
		
		Connection conn = DriverManager.getConnection("jdbc:mysql://115.28.210.110:3306/survey", "surveyadmin", "x7d91jd9lkx81");
		def sql = "select u.yhm,u.mm,u.sjhm, u.nc,u.yx, ui.xm,ui.xb,ui.xx,ui.csrq,ui.is_xlwb,ui.is_txwb,ui.is_pyq,ui.is_qqkj,ui.is_wx,ui.is_mzgkwj,ui.is_sllms,ui.is_gkdt,ui.is_hygkwj from yh_z u left join yh_xx_s ui on u.id = ui.yh_z_id where u.is_jh =1";

		println sql
		PreparedStatement pstmt = conn.prepareStatement(sql) ;
		ResultSet resultSet = pstmt.executeQuery();
		
		def users = userRepository.findAll()
		
		def userBs = new ArrayList<User>()
		
		while (resultSet.next()) {
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
				publicAnswersToFriend : resultSet.getString('is_hygkwj') ?: true
			)
			
			def user = new User(
				userName : resultSet.getString('yhm'),
				password : resultSet.getString('mm'),
				phone : resultSet.getString('sjhm'),
				email : resultSet.getString('yx'),
				activated : true,
				userInfo : userInfo
			)
			
			userBs << user
		}
		conn.close();
		
		pstmt.close();
		resultSet.close();
		println userBs.size()
		userBs.each { ub ->
			def u = users.find { us ->
				ub.phone == us.phone
			}
			if (u) {
				userBs.remove(ub)
			}
		}
		userRepository.save(userBs)
		
		
		'success'
	}
	
}
