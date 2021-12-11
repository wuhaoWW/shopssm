package com.mytest.ssm.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mytest.ssm.entity.Brand;
@Mapper
public interface BrandMapper extends BaseMapper<Brand> {
	
}
