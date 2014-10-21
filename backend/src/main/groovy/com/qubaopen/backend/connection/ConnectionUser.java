package com.qubaopen.backend.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.qubaopen.backend.connection.entity.UserB;
import com.qubaopen.backend.connection.entity.UserInfoB;

public class ConnectionUser {

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			Connection conn = DriverManager.getConnection("jdbc:mysql://115.28.210.110:3306/survey", "surveyadmin", "x7d91jd9lkx81");
			String sql = "select u.yhm,u.mm,u.sjhm, u.nc,u.yx, ui.xm,ui.xb,ui.xx,ui.csrq,ui.is_xlwb,ui.is_txwb,ui.is_pyq,ui.is_qqkj,ui.is_wx,ui.is_mzgkwj,ui.is_sllms,"
					+ "ui.is_gkdt,ui.is_hygkwj from yh_z u left join yh_xx_s ui on u.id = ui.yh_z_id where u.is_jh =1";

			PreparedStatement pstmt = conn.prepareStatement(sql) ;  
			ResultSet resultSet = pstmt.executeQuery();
			
			List<UserB> users = new ArrayList<UserB>();
			while (resultSet.next()) {
				UserB userBasic = new UserB();
				userBasic.setId(Long.parseLong(resultSet.getString("id")));
				userBasic.setEmail(resultSet.getString("yx"));
				userBasic.setNickName(resultSet.getString("nc"));
				userBasic.setPassword(resultSet.getString("mm"));
				userBasic.setPhone(resultSet.getString("sjhm"));
				users.add(userBasic);
			}
			
			
			
			String sql2 = "select * from yh_xx_s";
			PreparedStatement pstmt2 = conn.prepareStatement(sql2) ;  
			ResultSet resultSet2 = pstmt2.executeQuery();
			
			List<UserInfoB> userInfoBs = new ArrayList<UserInfoB>();
			while (resultSet2.next()) {
				UserInfoB userInfoB = new UserInfoB();
				userInfoB.setId(Long.parseLong(resultSet2.getString("yh_z_id")));
				userInfoB.setName(resultSet2.getString("xm"));
				userInfoB.setSex(resultSet2.getString("xb"));
				userInfoB.setBloodType(resultSet2.getString("xx"));
				userInfoB.setSinaShare(Boolean.parseBoolean(resultSet2.getString("is_xlwb")));
				userInfoB.setTencentShare(Boolean.parseBoolean(resultSet2.getString("is_txwb")));
				userInfoB.setWeChatFriendShare(Boolean.parseBoolean(resultSet2.getString("is_pyq")));
				userInfoB.setQqSpaceShare(Boolean.parseBoolean(resultSet2.getString("is_qqkj")));
				userInfoB.setWeChatShare(Boolean.parseBoolean(resultSet2.getString("is_wx")));
				userInfoB.setPublicToCheif(Boolean.parseBoolean(resultSet2.getString("is_mzgkwj")));
				userInfoB.setSaveFlow(Boolean.parseBoolean(resultSet2.getString("is_sllms")));
				userInfoB.setPublicMovement(Boolean.parseBoolean(resultSet2.getString("is_gkdt")));
				userInfoB.setPublicAnswers(Boolean.parseBoolean(resultSet2.getString("is_hygkwj")));
				userInfoBs.add(userInfoB);
				System.out.println(userInfoBs.size());
			}
			
			System.out.println(users.size());
			conn.close();
			
			pstmt.close();
			resultSet.close();
			
//			pstmt2.close();
//			resultSet2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
