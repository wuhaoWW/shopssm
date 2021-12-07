package com.mytest.ssm.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mytest.ssm.entity.Brand;
import com.mytest.ssm.entity.PageData;

public interface IBrandService {
	
	public void add(Brand brand) throws Exception;
	
	public void delete(Integer[] ids) throws Exception;
	
	public void deletePhoto(String path) throws Exception;
	
	public Brand findById(Integer id) throws Exception;
	
	public void edit(Brand newBrand) throws Exception;
 
	public PageData listPage(Map<String, Object> conditions,
			int page, int rows, LinkedHashMap<String, String> orderBy) throws Exception;

	public List<Brand> listAllBrand() throws Exception;
	
}
