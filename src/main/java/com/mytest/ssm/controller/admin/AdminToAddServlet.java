package com.mytest.ssm.controller.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminToAddServlet extends HttpServlet{ 

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}
 
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		//转发器
		System.out.println("AdminToAddServlet");
		req.getRequestDispatcher("/WEB-INF/pages/back/admin/admin_add.jsp").forward(req, resp);
	} 
	 
}   