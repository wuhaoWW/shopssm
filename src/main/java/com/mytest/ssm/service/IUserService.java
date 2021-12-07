package com.mytest.ssm.service;

import java.util.LinkedHashMap;
import java.util.Map;

import com.mytest.ssm.entity.PageData;
import com.mytest.ssm.entity.User;
import com.mytest.ssm.exception.EmailExistException;
import com.mytest.ssm.exception.UsernameExistException;

public interface IUserService {
		/**
		 * 注册用户！
		 * @param user
		 * @return
		 * @throws Exception 
		 */
	     public String register(User user) throws 
	          UsernameExistException,EmailExistException,Exception; 
	     
		/**
		 * 用户登陆！
		 * @param user
		 * @return
		 * @throws Exception 
		 */
	     public boolean login(String username,String pwd) throws Exception; 
	     /** 
	 	 * 用户分页！
	 	 * @param  user
	 	 * @return
	 	 * @throws Exception
	 	 */
	 	  public PageData listPage(Map<String, Object> conditions, 
	 			  int pageInt, int rowsInt,LinkedHashMap<String, String> orderBy);
	 	 /** 
	 		 * 管理员查找
	 		 * @param admin
	 		 * @return
	 		 * @throws Exception
	 		 */
	 		 public User findByName(String userName);
	 	 /** 
	 		 * 用户删除
	 		 * @param  user
	 		 * @return
	 		 * @throws Exception
	 		 */
	 		 public void delete(Integer[] array);
	 		 /** 
	 		 * 管理员修改
	 		 * @param admin
	 		 * @return
	 		 * @throws Exception
	 		 */
	 		 public boolean edit(User admin);
}
