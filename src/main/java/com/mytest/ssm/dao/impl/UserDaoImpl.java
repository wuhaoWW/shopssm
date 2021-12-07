package com.mytest.ssm.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.mytest.ssm.dao.IUserDao;
import com.mytest.ssm.entity.User;
import com.mytest.ssm.utils.JdbcUtils;
import com.mytest.ssm.utils.mapper.UserRowMapper;

public class UserDaoImpl extends BaseDaoImpl implements IUserDao {
		public static String findByUsername = "select * from T_USER where U_USERNAME =? ";
		public static String save = "insert into T_USER values(?,?,?,?,?)";
		public static String findByEmail = "select * from T_USER where U_EMAIL =? ";
		public static String findByMobile = "select * from T_USER where U_MOBILE =? ";
		public static final String UserQueryList = "select * from T_USER where 1=1";
		public static final String queryCount = "select count(*) from T_USER where 1=1";
		public static final String delete = "delete from T_USER where 1=2";
		public static final String update =  "update T_USER set U_USERNAME=?,"
		        + "U_EMAIL=?,U_MOBILE=? where U_ID=?";
	/**
	 * 保存用户
	 * @param user
	 * @throws Exception
	 */ 
	@Override 
	public void save(User user) throws Exception {
		Connection conn = null;
		try{
			conn = JdbcUtils.getConnection();
			Object[] params = new Object[]{
					null,
					user.getUsername(),
					user.getPwd(),
					user.getEmail(),
					user.getMobile(),
			};
			JdbcUtils.executeUpdate(conn, this.save, params);
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}
	/**
	 * 删除管理员！
	 * @param id
	 */
	@Override
	public void delete(Integer[] id) {
		Connection conn = null;
		String deleteStr = delete;
		try {
			conn = JdbcUtils.getConnection();
			  if(id==null || id.length==0){
				  return;
			  }
			  
			  for (int i = 0; i < id.length; i++) {
				  deleteStr = deleteStr + " or U_ID=?";
			}
			  JdbcUtils.executeUpdate(conn, deleteStr, id);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			JdbcUtils.close(conn);
		}
		
	}
	/**
	 * 根据参数查询用户
	 * @param username
	 * @return
	 * @throws Exception
	 */
	@Override
	public User query(String parameter, String sqlStr) throws Exception {
		Connection conn = null;
		try{
			conn = JdbcUtils.getConnection();
			Object[] params = new Object[]{parameter};
			List<User> userList = JdbcUtils.executeQuery(conn, sqlStr, params,new UserRowMapper());
			if(userList.size()>0){
				return userList.get(0);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public boolean update(User user) {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			conn = JdbcUtils.getConnection();
			Object[] params = new Object[]{
					user.getUsername(),
					user.getEmail(),
					user.getMobile(),
					user.getId()
			}; 
			int rows = JdbcUtils.executeUpdate(conn, update, params);
			if(rows>0){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtils.close(conn);
		}
		return false;
	}
	
}
