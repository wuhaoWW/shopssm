package com.mytest.ssm.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mytest.ssm.entity.PageData;
import com.mytest.ssm.entity.User;
import com.mytest.ssm.exception.EmailExistException;
import com.mytest.ssm.exception.UsernameExistException;
import com.mytest.ssm.mapper.UserMapper;
import com.mytest.ssm.service.IUserService;
import com.mytest.ssm.utils.PageModel;

@Service
public class UserServiceImpl implements IUserService {
	@Autowired
	private UserMapper userMapper;
	/**
	 * 注册用户！
	 * @param user
	 * @return
	 */
	@Override
	public String register(User user) throws UsernameExistException,EmailExistException,Exception{
		//用户
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
		queryWrapper.eq("U_USERNAME", user.getUsername());
		User u = userMapper.selectOne(queryWrapper);
		if(null!=u){
			//用户已经存在
			return "UsernameAlreadyExist";
		}  
		QueryWrapper<User> queryWrapper1 = new QueryWrapper<User>();
		queryWrapper1.eq("U_EMAIL", user.getEmail());
		User u1 = userMapper.selectOne(queryWrapper1);
		//邮箱
		
		if(null!=u1){   
			//邮箱已经存在 
			return "EmailAlreadyExist";  
		}
		
		//手机
		QueryWrapper<User> queryWrapper2 = new QueryWrapper<User>();
		queryWrapper2.eq("U_MOBILE", user.getMobile());
		User u2 = userMapper.selectOne(queryWrapper2);
		if(null!=u2){
			return "MobileAlreadyExist";
		}
		
		//最后保存
		int userReginst = userMapper.userReginst(user);
	
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
		  User u=null;
		  try {
			   u=userLogin.get(0);
		} catch (Exception e) {
			// TODO: handle exception
			if(null==u){ 
		        //根据邮箱查询 
					QueryWrapper<User> queryWrapper1 = new QueryWrapper<User>();
					queryWrapper1.eq("U_EMAIL", param);
					 u = userMapper.selectOne(queryWrapper1);
						if(null==u){
							 //根据手机查询
							QueryWrapper<User> queryWrapper2 = new QueryWrapper<User>();
							queryWrapper2.eq("U_MOBILE", param);
							u = userMapper.selectOne(queryWrapper2);
						}
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
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
		
	
		  
		if(null!=conditions && conditions.size()>0){
			Object userName = conditions.get("userName");
			//判断userName是否为空 ,不为空才添加到whereSql
			if(null!=userName && !"".equals(userName)){
				queryWrapper.like("U_USERNAME", userName);
			}
			
		}     
		try { 
			//查出dataList  
			List<User> selectList = userMapper.selectList(queryWrapper);
			PageModel pageModel = new PageModel(selectList, rows);
			List objects = pageModel.getObjects(page);
			
			//创建pageData对象(totalRecordes,1,10,dataList)
			PageData pageData = new PageData(selectList.size(), page, rows, objects);
			  
			return pageData; 
			
		} catch (Exception e) {  
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void delete(Integer[] array) {
		// TODO Auto-generated method stub
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		for (int i = 0; i < array.length; i++) {
			arrayList.add(array[i]);
		}
		userMapper.deleteBatchIds(arrayList);
	}

	@Override
	public User findByName(String userName) {
		// TODO Auto-generated method stub
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
		try {
			queryWrapper.eq("U_USERNAME", userName);
			List<User> userList = userMapper.selectList(queryWrapper);
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
			int updateById = userMapper.updateById(admin);
			if (updateById != 0) {
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false; 
	}
}
