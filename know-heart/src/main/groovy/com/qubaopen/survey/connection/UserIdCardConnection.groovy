package com.qubaopen.survey.connection;

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.survey.entity.user.UserIDCard;
import com.qubaopen.survey.repository.user.UserIDCardRepository;

@RestController
@RequestMapping('userIdCardConnection')
public class UserIdCardConnection {
	
	@Autowired
	UserIDCardRepository userIDCardRepository
	
	@RequestMapping(value = 'idCardConnection', method = RequestMethod.GET)
	idCardConnection() {
//		Class.forName("com.mysql.jdbc.Driver");
//		
//		Connection conn = DriverManager.getConnection("jdbc:mysql://115.28.210.110:3306/survey", "surveyadmin", "x7d91jd9lkx81");
//		def sql = "select sfz_hm, xm from ht_yhsfz_s";
//
//		PreparedStatement pstmt = conn.prepareStatement(sql) ;
//		ResultSet resultSet = pstmt.executeQuery();
//		
//		def userIdCards = userIDCardRepository.findAll()
//		
//		def idCards = []
//		
//		while (resultSet.next()) {
//			def  id = resultSet.getString('sfz_hm')
//			def name = resultSet.getString('xm')
//			def userIdCard = new UserIDCard(
//				IDCard : id,
//				name : name
//			)
//			
//			idCards << userIdCard
//		}
		
		
		
	}
}
