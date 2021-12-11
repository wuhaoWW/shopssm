package com.mytest.ssm.service.impl;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mytest.ssm.entity.Category;
import com.mytest.ssm.entity.PageData;
import com.mytest.ssm.mapper.CategoryMapper;
import com.mytest.ssm.service.ICategoryService;
import com.mytest.ssm.utils.PageModel;
import org.springframework.util.StringUtils;

@Service
public class CategoryServiceImpl implements ICategoryService {
	@Autowired
	private CategoryMapper categoryMapper;
	@Override
	public void add(Category category) throws Exception {
		try{
			categoryMapper.insert(category);

		}catch(Exception e){ 
			e.printStackTrace();
		}  
	}   
 
	@Override
	public void delete(Integer[] ids){
		try {
			HashSet<Integer> delSet = new HashSet<Integer>();
			for (int i = 0; i < ids.length; i++) {
				Category category = findById(ids[i]);
				delSet.add(ids[i]);
				//递归删除,ids要重新构造成delList
				recursionQuery(category, delSet);
			}
			//删除delSet
			categoryMapper.deleteBatchIds(delSet);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 递归查询子分类的Id！
	 */
	@Override
	public void recursionQuery(Category category ,HashSet<Integer> delSet){
		HashSet<Category> chindsSet = (HashSet<Category>) category.getChildsSet();
		if(null==chindsSet || chindsSet.size()<=0){
			return ;
		}
		for (Category c : chindsSet) {
			delSet.add(c.getId());
			recursionQuery(c, delSet);
		}
	}
	
	@Override
	public List<Category> listAllCategory(int level) throws Exception {
		if(level<1 && level>3){
			level = 1;
		}
			QueryWrapper<Category> queryWrapper = new QueryWrapper<Category>();
			queryWrapper.eq("C_LEVEL", level);
			List<Category> categoryList = categoryMapper.selectList(null).stream().peek(v -> v.setChildsSet(new HashSet<>())).collect(Collectors.toList());
		List<Category> rootCategory = categoryList.stream().filter(v -> Objects.isNull(v.getPid())).collect(Collectors.toList());
		List<Category> childCategory = categoryList.stream().filter(v -> Objects.nonNull(v.getPid())).collect(Collectors.toList());

//		for (Category category :categoryList) {
//				List<Category> findSubCategorys = findSubCategorys(category);
//					category.setChildsSet(new HashSet<Category>(findSubCategorys));
//			}
		findChild(childCategory, rootCategory);
			 return rootCategory;
	
	}

	private static void findChild(List<Category>childrenNode ,List<Category> topNode){
		//遍历根节点
		topNode.forEach(p->{
			//筛选如果子节点的parentId与父节点的id相等  则进入if分支
			if(childrenNode.stream().anyMatch(t->t.getPid().equals(p.getId()))){
				//筛选出当前父节点的儿子节点
				List<Category> collect = childrenNode.stream().filter(t -> t.getPid()
						.equals(p.getId())).collect(Collectors.toList());
				//为当前父节点添加儿子
				p.setChildsSet(new HashSet<>(collect));
			}
			//将当前的父节点的儿子节点  递归调用：其中，子节点还是所有子节点，当前父节点的儿子节点进入寻找子节点
			findChild(childrenNode,new ArrayList<>(p.getChildsSet()));
		});
	}
	//递归，寻找子分类
	private List<Category> findSubCategorys(Category parent) {
		List<Category> categoryList = null;
		//parent为category
		if(null!=parent){
			QueryWrapper<Category> queryWrapper = new QueryWrapper<Category>();
			queryWrapper.eq("C_PID", parent.getId());
				 categoryList = categoryMapper.selectList(queryWrapper);
			//当categoryList长度为0的时候递归结束
			if(null!=categoryList && categoryList.size()>0){
				for(Category c:  categoryList){
					List<Category> subCategorys = findSubCategorys(c);
					c.setChildsSet(new HashSet<Category>(subCategorys));//设置子分类
					c.setParentCategory(parent);//设置父分类
				}     
			}
		} 
		return categoryList;
	}

	/**
	 * 根据Id查找一个分类！
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public Category findById(Integer id) throws Exception {
		try{
			Category categoryList = categoryMapper.selectById(id);
			Category selectById = categoryMapper.selectById(categoryList.getPid());
			categoryList.setParentCategory(selectById);
			return categoryList;
		}catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	/**
	 * category分页查询
	 * @param conditions
	 * @param orderBy
	 * @return
	 * @throws SQLException 
	 */
	@Override
	public PageData listPage(Map<String, Object> conditions, int page, int rows,
			LinkedHashMap<String, String> orderBy) throws SQLException {
		List<Object> paramsList = new ArrayList<Object>();
		 QueryWrapper<Category> queryWrapper = new QueryWrapper<Category>();
		if(null!=conditions && conditions.size()>0){
			Object categoryName = conditions.get("categoryName");
			if(null!=categoryName && !"".equals(categoryName)){
				queryWrapper.like("C_NAME", categoryName);
			}
			
			Object level = conditions.get("level"); 
			if(null!=level && !"".equals(level)){   
				queryWrapper.eq("C_LEVEL", level);
			} 
		}     
		List<Category> dataList = categoryMapper.selectList(queryWrapper);
		PageModel pageModel = new PageModel(dataList, rows);
		List objects = pageModel.getObjects(page);
		PageData pageData = new PageData(dataList.size(), page, rows, objects);
		  
		return pageData;
	}
    
	@Override
	public void edit(Category category) {
		try {
			categoryMapper.updateById(category);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
