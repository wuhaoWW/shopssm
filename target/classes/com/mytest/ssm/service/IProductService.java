package com.mytest.ssm.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mytest.ssm.entity.PageData;
import com.mytest.ssm.entity.Product;
import com.mytest.ssm.entity.ProductImage;

public interface IProductService {
	
	public void add(Product p) throws Exception;

	public List<Product> listFrontProduct(Map<String, Object> conditions, int page,
			  int rows, LinkedHashMap<String, String> orderBy);
	
	public Product findById(Integer id)  throws Exception;

	public PageData listPage(Map<String, Object> conditions, int page, int rows,
			LinkedHashMap<String, String> orderBy) throws Exception;

	public List<ProductImage> findPImages(Integer id) throws Exception;

	public void edit(Product product) throws Exception;

	public void delete(Integer[] array);
 

}
  