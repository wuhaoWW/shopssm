package com.mytest.ssm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mytest.ssm.entity.Product;
@Mapper
public interface ProductMapper  extends BaseMapper<Product>{

	List<Product>  selectListByQuery(@Param("productName") String productName,@Param("brandName") String brandName,@Param("categoryName")String categoryName);
}
