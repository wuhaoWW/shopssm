package com.mytest.ssm.utils.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mytest.ssm.entity.User;

public class UserRowMapper implements RowMapper<User> {

	@Override
	public User getEntity(ResultSet rs) throws SQLException {
		User user = new User();
		
		user.setId(rs.getInt("U_ID"));
		user.setUsername(rs.getString("U_USERNAME"));
		user.setPassword(rs.getString("U_PWD"));
		user.setEmail(rs.getString("U_EMAIL"));
		user.setMobile(rs.getString("U_MOBILE"));
		
		return user;
	}

}
