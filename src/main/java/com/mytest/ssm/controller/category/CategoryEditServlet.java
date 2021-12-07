package com.mytest.ssm.controller.category;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mytest.ssm.entity.Category;
import com.mytest.ssm.service.impl.CategoryServiceImpl;

public class CategoryEditServlet extends HttpServlet{
	private CategoryServiceImpl categoryService 
							= new CategoryServiceImpl();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	} 

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
			Map<String,String> errors = this.validate(req);   
			if(errors.size()>0){
				//有错误就转发 
				req.getRequestDispatcher("/WEB-INF/pages/back/category/Category_edit.jsp").forward(req, resp);
			}else{
				  
				Category category = getCategoryFromRequest(req); 
				//Dao,把对象存进数据库，然后页面跳转  
				try {
					categoryService.edit(category);//添加到数据库
					req.setAttribute("global_message", "分类修改成功！"); 
					req.setAttribute("nextUrl", "/back/category/categoryList.do");
					req.getRequestDispatcher("/WEB-INF/pages/back/back_global_message.jsp").forward(req, resp);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			} 
	}	
	 
	private Map<String, String> validate(HttpServletRequest req){
		Map<String,String> errors = new HashMap<String, String>();
		//检验代码 
		
		return errors;
	}
	
	private Category getCategoryFromRequest(HttpServletRequest req){
		
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
		category.setDesc(desc);
		category.setParentCategory(parentCategory);
		
		return category; 
	}
	
	
	
}
