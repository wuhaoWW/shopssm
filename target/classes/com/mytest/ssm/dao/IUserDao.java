package com.mytest.ssm.dao;

import com.mytest.ssm.entity.User;

public interface IUserDao {
	/**
	 * 保存用户
	 * @param user
	 * @throws Exception
	 */
	public void save(User user) throws Exception;
	/**
	 * 根据参数查询用户
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public User query(String parameter, String sqlStr) throws Exception;
	/**
	 * 删除记录
	 * @param id
	 */
	public void delete(Integer[] id);
	/**
	 * 修改记录
	 * @param User
	 */
	public boolean update(User user);
}
