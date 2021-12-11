package com.mytest.ssm.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mytest.ssm.entity.Brand;
import com.mytest.ssm.entity.Category;
import com.mytest.ssm.entity.PageData;
import com.mytest.ssm.entity.Product;
import com.mytest.ssm.entity.ProductImage;
import com.mytest.ssm.mapper.BrandMapper;
import com.mytest.ssm.mapper.CategoryMapper;
import com.mytest.ssm.mapper.ProductImageMapper;
import com.mytest.ssm.mapper.ProductMapper;
import com.mytest.ssm.service.IProductService;
import com.mytest.ssm.utils.PageModel;

@Service
public class ProductServiceImpl extends BaseServiceImpl implements IProductService {
	 @Autowired
	 private ProductMapper productMapper;
	 @Autowired
	 private BrandMapper brandMapper;
	 @Autowired
	 private CategoryMapper categoryMapper;
	 @Autowired
	 private ProductImageMapper productImageMapper;
	@Override 
	public void add(Product p) throws Exception {
		try{
			productMapper.insert(p);
		}catch(Exception e){
			e.printStackTrace();
		}
	} 
	/**  
	 * 首页查询！！！
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> listFrontProduct(Map<String,Object> conditions,
			    int page, int rows, LinkedHashMap<String, String> orderBy) {
		QueryWrapper<Product> queryWrapper = new QueryWrapper<Product>();
		
		if(null!=conditions && conditions.size()>0){
			Object recommend = conditions.get("P_RECOMMEND");//true
			//判断recommend是否为空 
			if(null!=recommend && !"".equals(recommend)){
				queryWrapper.eq("P_RECOMMEND", recommend);
			}
			Object promotion = conditions.get("P_PROMOTION");//true
			//判断promotion是否为空
			if(null!=promotion && !"".equals(promotion)){ 
				queryWrapper.eq("P_PROMOTION", promotion);
			}
		}   
		try {
			List<Product> selectList = productMapper.selectList(queryWrapper);
			PageModel pageModel = new PageModel(selectList, rows);
			List objects = pageModel.getObjects(page);
			
			return objects;
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return null;
	} 
	
	@Override  
	public Product findById(Integer id) throws Exception {
		try {
			Product selectById = productMapper.selectById(id);
			List<ProductImage> findPImages = findPImages(selectById.getId());
			Brand selectById2 = brandMapper.selectById(selectById.getBrandId());
			Category selectById3 = categoryMapper.selectById(selectById.getCategoryId());
			selectById.setBrand(selectById2);
			selectById.setCategory(selectById3);
			selectById.setProductImagesList(findPImages);
			return selectById;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 查询副图片 
	 */
	@Override
	public List<ProductImage> findPImages(Integer id) throws Exception {
		QueryWrapper<ProductImage> queryWrapper = new QueryWrapper<ProductImage>();
		try {
			queryWrapper.eq("PI_PRODUCT_ID", id);
			List<ProductImage> PImageList = productImageMapper.selectList(queryWrapper);
			if(null!=PImageList && PImageList.size()>0){
				return PImageList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	} 
	
	/**  
	 * 后台分页查询！！！
	 */ 
	@Override
	public PageData listPage(Map<String, Object> conditions, int page, int rows,
			LinkedHashMap<String, String> orderBy) {
		String productName = (String) conditions.get("productName");

		String brandName = (String) conditions.get("brandName"); 

		String categoryName = (String) conditions.get("categoryName"); 
	
		try { 
			//查出dataList  
			List<Product> dataList = productMapper.selectListByQuery(productName,brandName,categoryName);
			
			PageModel pageModel = new PageModel(dataList, rows);
			List<Product> objects = pageModel.getObjects(page);
			if (objects!=null ) {
				//处理
				for (int i = 0; i < objects.size(); i++) {
					Product product = objects.get(i);
					//处理productImagesList
					List<ProductImage> PImageList = findPImages(product.getId());
					product.setProductImagesList(PImageList);
					//处理产品
					product.setBrand(brandMapper.selectById(product.getBrandId()));
					//处理分类
					product.setCategory(categoryMapper.selectById(product.getCategoryId()));
					
				}
			}
			
		
			//创建pageData对象(totalRecordes,1,30,dataList)
			PageData pageData = new PageData(dataList.size(), page, rows, objects);
			  
			return pageData;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void edit(Product newProduct) throws Exception {
		//修改数据库信息！
		productMapper.updateById(newProduct);
	}
	
	@Override
	public void delete(Integer[] ids) {
//		try {
//			for (int i = 0; i < ids.length; i++) {
//				Product product = findById(ids[i]);
//				List<ProductImage> pImageList = product.getProductImagesList();
//				if (pImageList!=null) {
//					for (int j = 0; j < pImageList.size(); j++) {
//						//先删除副图片！
//						deletePhoto(pImageList.get(j).getUrl());
//					}
//					//再删除主图片！
//					deletePhoto(product.getMainImage());
//				}
//				
//			}
			
			//再删除数据库产品信息！
		for(int i=0;i<ids.length;i++) {
			productMapper.deleteById(ids[i]);
		}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}
}

