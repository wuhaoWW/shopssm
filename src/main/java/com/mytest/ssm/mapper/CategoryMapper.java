package com.mytest.ssm.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mytest.ssm.entity.Category;
@Mapper
public interface CategoryMapper  extends BaseMapper<Category>{
	
}
