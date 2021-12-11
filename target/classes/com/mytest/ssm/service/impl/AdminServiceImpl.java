package com.mytest.ssm.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mytest.ssm.entity.Admin;
import com.mytest.ssm.entity.PageData;
import com.mytest.ssm.mapper.AdminMapper;
import com.mytest.ssm.service.IAdminService;
import com.mytest.ssm.utils.PageModel;

@Service
public class AdminServiceImpl implements IAdminService {
	
	@Autowired
	private AdminMapper adminMapper;
	/**
	 * 注册用户！
	 */
	@Override  
	public String add(Admin admin) throws Exception{
		//管理员
		Admin adm = findByName(admin.getAdminName());
		if(null!=adm){
			//管理员已经存在   
			return "AdminAlreadyExist"; 
		}else{
			//管理员不存在 
			adminMapper.insert(admin);
			return "success"; 
		}
	}   
	  
	@Override
	public boolean login(String adminName, String password) throws Exception {
        //根据adminName查询 
		Admin admin = new Admin();
		admin.setAdminName(adminName);
		admin.setPassword(password);
		List<Admin> selectUserInfoByNameAndPass = adminMapper.selectUserInfoByNameAndPass(admin);
	
		if(!selectUserInfoByNameAndPass.isEmpty()){
			return true;
		}else{ 
			return false; 
		}
		
	}

	@Override
	public PageData listPage(Map<String, Object> conditions, int page, int rows,
			LinkedHashMap<String, String> orderBy) {
		
		QueryWrapper<Admin> queryWrapper = new QueryWrapper<Admin>();
		  
		if(null!=conditions && conditions.size()>0){
			Object adminName = conditions.get("adminName");
			//判断adminName是否为空 ,不为空才添加到whereSql
			if(null!=adminName && !"".equals(adminName)){
				queryWrapper.like("A_ADMINNAME", adminName);
			}
			
		}     
		try { 
			
				
			//查出dataList  
			List<Admin> selectList = adminMapper.selectList(queryWrapper);
			
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
	public void delete(Integer[] id) {
		ArrayList<Integer> list = new ArrayList<>(Arrays.asList(id));
		adminMapper.deleteBatchIds(list);
	}

	@Override
	public Admin findByName(String adminName) {
		QueryWrapper<Admin> queryWrapper = new QueryWrapper<Admin>();
		
		try {
			queryWrapper.eq("A_ADMINNAME", adminName);
			List<Admin> adminList = adminMapper.selectList(queryWrapper);
			if(null!=adminList && adminList.size()>0){
				return adminList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public boolean edit(Admin admin) {
		try {
			int updateById = adminMapper.updateById(admin);
			if (updateById !=0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false; 
	}
	
	@Override
	public boolean editPwd(Integer id,String newPwd) {
		try {
			Admin admin = new Admin();
			admin.setId(id);
			admin.setPassword(newPwd);
			int updateUserPWDById = adminMapper.updateUserPWDById(admin);
			if (updateUserPWDById !=0) {
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
