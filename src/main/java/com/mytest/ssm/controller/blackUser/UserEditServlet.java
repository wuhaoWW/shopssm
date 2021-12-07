package com.mytest.ssm.controller.blackUser;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mytest.ssm.entity.User;
import com.mytest.ssm.service.impl.UserServiceImpl;


public class UserEditServlet extends HttpServlet{ 
	private UserServiceImpl userService = new UserServiceImpl();
	private static final String userLIST = "/back/user/list.do";
	private static final String BACKPAGE = "BACKPAGE";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp); 
	} 
   
	@Override 
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException { 
		try {
			//修改数据库记录
			User user = getUserFromRequest(req);     
			boolean flag = userService.edit(user);
			if(flag){
				msg(req, resp, userLIST, "管理员信息修改成功！");
			}else{ 
				msg(req, resp, BACKPAGE, "管理员信息修改失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	private void msg(HttpServletRequest req, HttpServletResponse resp, 
	          String nextUrl, String global_message) throws ServletException, IOException {
					//设置nextUrl与global_message 
					req.setAttribute("nextUrl", nextUrl);
					req.setAttribute("global_message", global_message);
					//转发器
					req.getRequestDispatcher("/WEB-INF/pages/back/back_global_message.jsp").forward(req, resp);
	}
	
	private User getUserFromRequest(HttpServletRequest req){
		 
		String idStr = req.getParameter("userId");
		Integer id = Integer.valueOf(idStr);
		String userName = req.getParameter("username");  
		String mobile =  req.getParameter("mobile");
		String email = req.getParameter("email");   
		
		User user = new User();
		
		user.setId(id);
		user.setUsername(userName); 
		user.setEmail(email); 
		user.setMobile(mobile);
		
		return user;  
	} 
}   
