package com.qubaopen.backend.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.qubaopen.backend.connection.entity.UserInfoB;

public class ConnectionUserInfo {

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			Connection conn = DriverManager.getConnection("jdbc:mysql://115.28.210.110:3306/survey", "surveyadmin", "x7d91jd9lkx81");

			String sql2 = "select yh_z_id,xm,xb,xx,is_xlwb,is_txwb,is_pyq,is_qqkj,is_wx,is_mzgkwj,is_sllms,is_gkdt,is_hygkwj from yh_xx_s";
			PreparedStatement pstmt2 = conn.prepareStatement(sql2);
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

			conn.close();

			pstmt2.close();
			resultSet2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
