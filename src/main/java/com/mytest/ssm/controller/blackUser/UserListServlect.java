package com.mytest.ssm.controller.blackUser;


	import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mytest.ssm.entity.PageData;
import com.mytest.ssm.service.IUserService;
import com.mytest.ssm.service.impl.UserServiceImpl;
	//用户列表
	public class UserListServlect extends HttpServlet{
		private IUserService userService = new UserServiceImpl();
		
		@Override 
		protected void doGet(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			    this.doPost(req, resp);
		} 
	 
		@Override
		protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
				throws ServletException, IOException {
				try {
					Map<String,Object> conditions = new HashMap<String,Object>();
					LinkedHashMap<String, String> orderBy = new LinkedHashMap<String,String>();		
					//默认第一页和10条记录数
					int pageInt = PageData.PAGE;//1
					int rowsInt = PageData.ROWS;//10
					
					String userName = req.getParameter("userName"); 
					String page = req.getParameter("page");
					String rows = req.getParameter("rows");
					
					if(null!=userName && userName.length()>0){
						conditions.put("userName", userName); 
					}
					if(null!=page && page.length()>0){
						//更新pageInt
						pageInt = Integer.valueOf(page);
					} 
					if(null!=rows && rows.length()>0){
						//更新rowsInt
						rowsInt = Integer.valueOf(rows); 
					}   
					orderBy.put("U_ID", "asc");
					
					PageData pageData =  					 //1      //10
							userService.listPage(conditions,pageInt,rowsInt,orderBy);
					  
					String reqUri = req.getRequestURI();
					req.setAttribute("reqUri", reqUri);
					req.setAttribute("pageData", pageData);
					req.setAttribute("userName", userName);  
					
					req.getRequestDispatcher("/WEB-INF/pages/back/user/user_list.jsp").forward(req, resp);
				} catch (Exception e) {
					e.printStackTrace();
				} 
		} 
	} 
