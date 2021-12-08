package com.mytest.ssm.mapper;
import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mytest.ssm.entity.User;

public interface UserMapper  extends BaseMapper<User> {
	
	public List<User> userLogin(User user);
}
