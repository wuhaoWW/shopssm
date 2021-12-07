package com.mytest.ssm.controller.blackUser;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mytest.ssm.entity.User;
import com.mytest.ssm.service.IUserService;
import com.mytest.ssm.service.impl.UserServiceImpl;

public class UserToEditServlet extends HttpServlet{
	private IUserService userService 
						= new UserServiceImpl();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		 
		String UserName = req.getParameter("userName");
		if(null!=UserName && UserName.length()>0){
			try {
				//根据UserName查找记录 
				User user = userService.findByName(UserName);
				req.setAttribute("user", user);
				//转发器  
				req.getRequestDispatcher("/WEB-INF/pages/back/user/user_edit.jsp").forward(req, resp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
