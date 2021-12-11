package com.mytest.ssm.controller.category;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mytest.ssm.entity.Category;
import com.mytest.ssm.entity.PageData;
import com.mytest.ssm.service.ICategoryService;

@Controller
@RequestMapping("/back/category")
public class CategoryController {

	@Autowired
	private ICategoryService categoryService;
	
	@RequestMapping("/toAdd")
	public void toAdd(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		//转发器  
		req.getRequestDispatcher("/WEB-INF/pages/back/category/Category_add.jsp").forward(req, resp);
	}
	@RequestMapping("/add")
	public void add(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		Map<String,String> errors = new HashMap<String, String>();
		//检验代码 
		String name = req.getParameter("name");
		String levelStr = req.getParameter("level"); 
		String P_IDStr = req.getParameter("P_ID");
		String desc = req.getParameter("desc");
		if (name==null || name.isEmpty()) {
			errors.put("name", "名称不得为空");
		}
		if (levelStr==null || levelStr.isEmpty()) {
			errors.put("levelStr", "级别不得为空");
		}
		if (P_IDStr==null || P_IDStr.isEmpty()) {
			errors.put("P_IDStr", "上一级别ID不得为空");
		}   
		if(errors.size()>0){
			//有错误就转发 
			req.setAttribute("errors", errors);
			req.getRequestDispatcher("/WEB-INF/pages/back/category/Category_add.jsp").forward(req, resp);
		}else{
			Integer P_ID = Integer.valueOf(P_IDStr);
			Integer level = Integer.valueOf(levelStr); 
			 
			Category category = new Category();
			//cls查询 
			Category parentCategory = null;
			try {
				parentCategory = categoryService.findById(P_ID);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String parentCls = parentCategory.getCls();
			String finalCls = "|"+P_IDStr+"|,"+parentCls;   
			
			category.setId(null);
			category.setName(name);
			category.setLevel(level);    
			category.setCls(finalCls);
			category.setDescs(desc);
			category.setParentCategory(parentCategory);
		 	 
			//Dao,把对象存进数据库，然后页面跳转  
			try {
				categoryService.add(category);//添加到数据库
				req.setAttribute("global_message", "分类添加成功！"); 
				req.setAttribute("nextUrl", "/back/category/list.action");
				req.getRequestDispatcher("/WEB-INF/pages/back/back_global_message.jsp").forward(req, resp);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		} 
		
	}
	@RequestMapping("/toEdit")
	public void toEdit(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String idStr = req.getParameter("id");//1
		Integer id = Integer.valueOf(idStr);  //1
		try {
			//根据id查找记录
			Category category = categoryService.findById(id);
			req.setAttribute("category", category);
			//请求转发
			req.getRequestDispatcher("/WEB-INF/pages/back/category/Category_edit.jsp").forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@RequestMapping("/edit")
	public void edit(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String idStr = req.getParameter("id");
		String name = req.getParameter("name");
		String levelStr = req.getParameter("level"); 
		String P_IDStr = req.getParameter("P_ID");
		String desc = req.getParameter("desc");
		
		Integer id = Integer.valueOf(idStr);
		Integer P_ID = Integer.valueOf(P_IDStr);
		Integer level = Integer.valueOf(levelStr); 
		
		Category category = new Category();
		//cls查询 
		Category parentCategory = null;
		try {
			parentCategory = categoryService.findById(P_ID); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		String parentCls = parentCategory.getCls();
		String finalCls = "|"+P_ID+"|,"+parentCls;   
		
		category.setId(id);
		category.setName(name);
		category.setLevel(level);    
		category.setCls(finalCls);
		category.setDescs(desc);
		category.setParentCategory(parentCategory);
		//Dao,把对象存进数据库，然后页面跳转  
		try {
			categoryService.edit(category);//添加到数据库
			req.setAttribute("global_message", "分类修改成功！"); 
			req.setAttribute("nextUrl", "/back/category/list.action");
			req.getRequestDispatcher("/WEB-INF/pages/back/back_global_message.jsp").forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		} }

	@RequestMapping("/del")
	public void del(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String[] ids = req.getParameterValues("id");
		List<Integer> idList = new ArrayList<Integer>();
		if(null!=ids && ids.length>0){
			for (int i = 0; i < ids.length; i++) {
				Integer id = Integer.valueOf(ids[i]);
				idList.add(id);
			}
		}
		try {
			categoryService.delete(idList.toArray(new Integer[]{}));
			req.setAttribute("nextUrl", "/back/category/list.action");
			req.setAttribute("global_message", "分类删除成功！");
			req.getRequestDispatcher("/WEB-INF/pages/back/back_global_message.jsp").forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}	}
	@RequestMapping("/list")
	public void list(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		try { 
			Map<String,Object> conditions = new HashMap<String,Object>();
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String,String>();		
			
			String categoryName = req.getParameter("categoryName"); 
			String level = req.getParameter("level"); 
			String page = req.getParameter("page");
			String rows = req.getParameter("rows");

			//默认第一页和10条记录数
			int pageInt = PageData.PAGE;//1
			int rowsInt = PageData.ROWS;//10 
			if(null!=categoryName && categoryName.length()>0){ 
				conditions.put("categoryName", categoryName);
			}
			if(null!=level && level.length()>0){
				conditions.put("level", level);
			}
			if(null!=page && page.length()>0){
				pageInt = Integer.valueOf(page);
			} 
			if(null!=rows && rows.length()>0){
				rowsInt = Integer.valueOf(rows);
			} 
			orderBy.put("C_ID", "asc");    
			
			PageData pageData =  					 //1      //10
					categoryService.listPage(conditions,pageInt,rowsInt,orderBy);
			 
			String reqUri = req.getRequestURI();
			req.setAttribute("reqUri", reqUri);
			req.setAttribute("pageData", pageData);
			req.setAttribute("categoryName", categoryName);  
			req.setAttribute("level", level); 
			
			req.getRequestDispatcher("/WEB-INF/pages/back/category/Category_list.jsp").forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}  }
}
