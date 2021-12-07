package com.mytest.ssm.controller.blackUser;



	import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mytest.ssm.service.IUserService;
import com.mytest.ssm.service.impl.UserServiceImpl;
	public class UserDelServlet  extends HttpServlet{
		IUserService userService 
		                      = new UserServiceImpl();
		
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			this.doPost(req, resp);
		}

		@Override
		protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
				throws ServletException, IOException {
				//获得用户从页面传来的数据
				String[] ids = req.getParameterValues("id");
				List<Integer> idList = new ArrayList<Integer>();
				if(null!=ids){
					for (int i = 0; i < ids.length; i++) {
						Integer id = Integer.valueOf(ids[i]);
						idList.add(id);
					}
				}
				try {
					userService.delete(idList.toArray(new Integer[]{}));
					req.setAttribute("nextUrl", "/back/user/list.do");
					req.setAttribute("global_message", "用户删除成功！");
					req.getRequestDispatcher("/WEB-INF/pages/back/back_global_message.jsp").forward(req, resp);
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		
		
	}
