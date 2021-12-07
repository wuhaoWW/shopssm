package com.mytest.ssm.controller.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mytest.ssm.entity.Admin;
import com.mytest.ssm.service.IAdminService;
import com.mytest.ssm.service.impl.AdminServiceImpl;

public class AdminToPwdChangeServlet extends HttpServlet{
	private IAdminService adminService = new AdminServiceImpl();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		 
		String adminName = req.getParameter("adminName");
		if(null!=adminName && adminName.length()>0){
			try {
				//根据adminName查找记录
				Admin admin = adminService.findByName(adminName);
				req.setAttribute("admin", admin);
				//转发器  
				req.getRequestDispatcher("/WEB-INF/pages/back/admin/admin_pwdChange.jsp").forward(req, resp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
