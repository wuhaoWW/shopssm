package com.mytest.ssm.controller.brand;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mytest.ssm.entity.PageData;
import com.mytest.ssm.service.IBrandService;
import com.mytest.ssm.service.impl.BrandServiceImpl;
//品牌列表
public class BrandListServlet extends HttpServlet{
	private IBrandService brandService 
						= new BrandServiceImpl();
	
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
				
				String enName = req.getParameter("B_ENNAME"); 
				String cnName = req.getParameter("B_CNNAME");
				String page = req.getParameter("page");
				String rows = req.getParameter("rows");
				
				//默认第一页和30条记录数
				int pageInt = PageData.PAGE;//1
				int rowsInt = PageData.ROWS;//30
				if(null!=enName && enName.length()>0){
					conditions.put("B_ENNAME", enName);
				}
				if(null!=cnName && cnName.length()>0){
					conditions.put("B_CNNAME", cnName); 
				}
				if(null!=page && page.length()>0){
					pageInt = Integer.valueOf(page);
				} 
				if(null!=rows && rows.length()>0){
					rowsInt = Integer.valueOf(rows); 
				}   
				//通过主键id升序排序
				orderBy.put("B_ID", "asc");    
				
				PageData pageData =  					 //1      //30
						brandService.listPage(conditions,pageInt,rowsInt,orderBy);
				  
				String reqUri = req.getRequestURI();
				req.setAttribute("reqUri", reqUri);
				req.setAttribute("pageData", pageData);
				req.setAttribute("enName", enName);  
				req.setAttribute("cnName", cnName); 
				
				req.getRequestDispatcher("/WEB-INF/pages/back/brand/Brand_list.jsp").forward(req, resp);
			} catch (Exception e) {
				e.printStackTrace();
			} 
	} 
} 
