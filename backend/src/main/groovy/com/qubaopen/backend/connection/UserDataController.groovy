package com.qubaopen.backend.connection;

import java.sql.Connection
import java.sql.Date;
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.backend.repository.user.UserRepository
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserInfo

@RestController
@RequestMapping('userData')
public class UserDataController{
	
	@Autowired
	UserRepository userRepository

	@RequestMapping(value = 'getData', method = RequestMethod.GET)
	getData(){
		Class.forName("com.mysql.jdbc.Driver");
		
		Connection conn = DriverManager.getConnection("jdbc:mysql://115.28.210.110:3306/survey", "surveyadmin", "x7d91jd9lkx81");
		String sql = "select u.yhm,u.mm,u.sjhm, u.nc,u.yx, ui.xm,ui.xb,ui.xx,ui.csrq,ui.is_xlwb,ui.is_txwb,ui.is_pyq,ui.is_qqkj,ui.is_wx,ui.is_mzgkwj,ui.is_sllms,"
				+ "ui.is_gkdt,ui.is_hygkwj from yh_z u left join yh_xx_s ui on u.id = ui.yh_z_id where u.is_jh =1";

		PreparedStatement pstmt = conn.prepareStatement(sql) ;
		ResultSet resultSet = pstmt.executeQuery();
		
		def users = userRepository.findAll()
		
		def userBs = []
		
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
				birthday : (Date)resultSet.getString('csrq'),
				sharedSina : resultSet.getString('is_xlwb'),
				sharedTencent : resultSet.getString('is_txwb'),
				sharedWeChatFriend : resultSet.getString('is_pyq'),
				sharedQQSpace : resultSet.getString('is_qqkj'),
				sharedWeChat : resultSet.getString('is_wx'),
				saveFlow : resultSet.getString('is_sllms'),
				publicAnswersToChief : resultSet.getString('is_mzgkwj'),
				publicMovementToFriend : resultSet.getString('is_gkdt'),
				publicAnswersToFriend : resultSet.getString('is_hygkwj')
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
		userBs.each { ub ->
			def u = users.find { us ->
				ub.phone == us.phone
			}
			if (u) {
				userBs.remove(ub)
			}
		}
		conn.close();
		
		pstmt.close();
		resultSet.close();
		userRepository.save(userBs)
	}
	
}
