package com.mytest.ssm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mytest.ssm.entity.Admin;
@Mapper
public interface AdminMapper  extends BaseMapper<Admin>{
	
	//管理员登录
	List<Admin> selectUserInfoByNameAndPass(Admin admin);
	//根据用户id修改密码
	int updateUserPWDById(Admin admin);
 	
}
