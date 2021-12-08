package com.mytest.ssm.controller.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mytest.ssm.service.IUserService;

@Controller
@RequestMapping("/front")
public class UserLoginController {

	@Autowired
	private IUserService userService;
	
	@RequestMapping("/userlogin")
	public void Login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String login_user = req.getParameter("username");
		String pwd = req.getParameter("password"); 
			try {
				boolean isOk = userService.login(login_user, pwd);
				if(isOk){
					//创建cookie
					Cookie cookie = new Cookie(login_user, pwd);
					//一个月以内有效
					cookie.setMaxAge(60*60*24*30);
					cookie.setPath(req.getContextPath());
					resp.addCookie(cookie);
					
					HttpSession session = req.getSession();
					//设置单位为秒，设置为-1永不过期
					session.setMaxInactiveInterval(-1);
					//添加login_user到session
					session.setAttribute("login_user", login_user);
					
					this.msg(req, resp,  "http://localhost:8082/shopssm", "恭喜您，登陆成功！");
				}else{ 
					this.msg(req, resp,  "http://localhost:8082/shopssm/front/user/toLogin.do", "登陆失败：用户名或者密码错误！");
					
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
					req.getRequestDispatcher("/WEB-INF/pages/front/front_global_message.jsp").forward(req, resp);
		}
	
}
