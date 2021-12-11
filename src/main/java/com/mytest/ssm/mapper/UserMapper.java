package com.mytest.ssm.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mytest.ssm.entity.User;
@Mapper
public interface UserMapper  extends BaseMapper<User>  {
	//用户登录
	 List<User> userLogin(User user);
	//用户注册
	 int userReginst(User user);
}
