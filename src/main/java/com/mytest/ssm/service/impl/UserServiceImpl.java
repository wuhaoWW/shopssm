package com.mytest.ssm.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytest.ssm.dao.impl.UserDaoImpl;
import com.mytest.ssm.entity.PageData;
import com.mytest.ssm.entity.User;
import com.mytest.ssm.exception.EmailExistException;
import com.mytest.ssm.exception.UsernameExistException;
import com.mytest.ssm.mapper.UserMapper;
import com.mytest.ssm.service.IUserService;
import com.mytest.ssm.utils.mapper.UserRowMapper;
@Service
public class UserServiceImpl implements IUserService {
	private UserDaoImpl userDao = new UserDaoImpl();
	@Autowired
	UserMapper userMapper;
	/**
	 * 注册用户！
	 * @param user
	 * @return
	 */
	@Override
	public String register(User user) throws UsernameExistException,EmailExistException,Exception{
		//用户
		User u = userDao.query(user.getUsername(),userDao.findByUsername);
		if(null!=u){
			//用户已经存在
			return "UsernameAlreadyExist";
		}  
		
		//邮箱
		u = userDao.query(user.getEmail(),userDao.findByEmail);
		if(null!=u){   
			//邮箱已经存在 
			return "EmailAlreadyExist";  
		}
		
		//手机
		u = userDao.query(user.getMobile(), userDao.findByMobile);
		if(null!=u){
			return "MobileAlreadyExist";
		}
		
		//最后保存
			userDao.save(user); 
			return "success";
	}
	
	@Override
	public boolean login(String param, String pwd) throws Exception {
        //根据用户名查询
//		User u = userDao.query(param, userDao.findByUsername); 
		User user = new User();
		user.setUsername(param);
		user.setPassword(pwd);
		
		  List<User> userLogin = userMapper.userLogin(user);
		  
		  System.out.println(userLogin.toString());
		User u=userLogin.get(0);
			if(null==u){ 
	        //根据邮箱查询 
				u = userDao.query(param, userDao.findByEmail);
					if(null==u){
						 //根据手机查询
						u = userDao.query(param, userDao.findByMobile);
					}
			} 
		
		if(null!=u ){
			return true;
		}else{ 
			return false;
		}
		
	}

	/**
	 *
	 */
	/**
	 *
	 */
	@Override
	public PageData listPage(Map<String, Object> conditions, int page, int rows,
			LinkedHashMap<String, String> orderBy) {
		// TODO Auto-generated method stub
		StringBuffer whereSql = new StringBuffer();
		List<String> paramsList = new ArrayList<String>();
		  
		if(null!=conditions && conditions.size()>0){
			Object userName = conditions.get("userName");
			//判断userName是否为空 ,不为空才添加到whereSql
			if(null!=userName && !"".equals(userName)){
				whereSql.append(" and U_USERNAME like ?");
				paramsList.add("%"+userName+"%"); //添加enName到paramsList
			}
			
		}     
		try { 
			//查出dataList  
			List<User> dataList = userDao.queryList(whereSql,paramsList.toArray(),
						page,rows,orderBy,userDao.UserQueryList, new UserRowMapper());
			
			//查出totalRecordes总记录数
			int totalRecordes = userDao.getTotalRecords(whereSql,paramsList,userDao.queryCount);
			
			//创建pageData对象(totalRecordes,1,10,dataList)
			PageData pageData = new PageData(totalRecordes, page, rows, dataList);
			  
			return pageData; 
			
		} catch (Exception e) {  
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void delete(Integer[] array) {
		// TODO Auto-generated method stub
		userDao.delete(array);
	}

	@Override
	public User findByName(String userName) {
		// TODO Auto-generated method stub

		try {
			StringBuffer whereSql = new StringBuffer(" and U_USERNAME=? ");
			List<String> paramsList = new ArrayList<String>(); 
			paramsList.add(userName);
			List<User> userList = userDao.queryList(whereSql, paramsList.toArray(),
					-1, -1, null, userDao.UserQueryList, new UserRowMapper());
			if(null!=userList && userList.size()>0){
				return userList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean edit(User admin) {
		// TODO Auto-generated method stub
		try {
			 return userDao.update(admin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false; 
	}
}
