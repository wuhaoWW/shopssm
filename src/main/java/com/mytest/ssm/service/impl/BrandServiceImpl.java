package com.mytest.ssm.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mytest.ssm.entity.Brand;
import com.mytest.ssm.entity.PageData;
import com.mytest.ssm.mapper.BrandMapper;
import com.mytest.ssm.service.IBrandService;
import com.mytest.ssm.utils.PageModel;


@Service
public class BrandServiceImpl extends BaseServiceImpl implements IBrandService {
	
	@Autowired
	private BrandMapper brandMapper;
	
	@Override
	public void add(Brand brand) throws Exception {
		try{
			System.out.println(brand.toString());
			brandMapper.insert(brand);
		}catch(Exception e){
				e.printStackTrace();  
		}
	}  
        
	@Override
	public void delete(Integer[] ids) throws Exception {
		try {
			for (int i = 0; i < ids.length; i++) {
				brandMapper.deleteById(ids[i]);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void edit(Brand newBrand) throws Exception {
		try{
			Brand oldBrand = findById(newBrand.getId());
			String spPath = oldBrand.getSmallPhoto();
			//图片路径不相同，要删除旧的路径
			if(!spPath.equals(newBrand.getSmallPhoto())){
				//删除旧的图片！
				deletePhoto(spPath);
			}
			
			String bpPath = oldBrand.getBigPhoto();
			if(!bpPath.equals(newBrand.getBigPhoto())){
				deletePhoto(bpPath);
			}
			
			//再修改数据库信息！
			brandMapper.updateById(newBrand);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	 
	@Override
	public Brand findById(Integer id) throws Exception{
		
		try {
			return brandMapper.selectById(id);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    /**
     * 分页查询  
     */
	@Override
	public PageData listPage(Map<String, Object> conditions, int page, int rows,
			LinkedHashMap<String, String> orderBy) throws Exception {
		QueryWrapper<Brand> queryWrapper = new QueryWrapper<Brand>();
		
		  
		if(null!=conditions && conditions.size()>0){
			Object enName = conditions.get("B_ENNAME");
			//判断enName是否为空 ,不为空才添加到whereSql
			if(null!=enName && !"".equals(enName)){
				queryWrapper.like("B_ENNAME", enName);
			
			}
			
			Object cnName = conditions.get("B_CNNAME"); 
			//判断cnName是否为空,不为空才添加到whereSql
			if(null!=cnName && !"".equals(cnName)){  
				queryWrapper.like("B_CNNAME", cnName);
			}else {
				queryWrapper.like("B_CNNAME", "");
			}
		}
		try { 
			List<Brand> dataList = brandMapper.selectList(queryWrapper);
			 
			PageModel pageModel = new PageModel(dataList, rows);
			List objects = pageModel.getObjects(page);
			//创建pageData对象(totalRecordes,1,10,dataList)
			PageData pageData = new PageData(dataList.size(), page, rows, objects);
			  
			return pageData;
			
		} catch (Exception e) {  
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Brand> listAllBrand() throws Exception {
		try {
			return brandMapper.selectList(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
