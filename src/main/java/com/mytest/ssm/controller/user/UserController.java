package com.mytest.ssm.controller.user;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mytest.ssm.entity.User;
import com.mytest.ssm.exception.EmailExistException;
import com.mytest.ssm.exception.UsernameExistException;
import com.mytest.ssm.service.IUserService;

@Controller
@RequestMapping("/front/user")
public class UserController {
	private final static String toRegister = "http://localhost:8082/shopssm/front/user/toRegister.action";
	private final static String index = "http://localhost:8082/shopssm";
	@Autowired
	private IUserService userService;
	
	@RequestMapping("/login")
	public void Login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//获取form表单内容
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
					this.msg(req, resp,  "http://localhost:8082/shopssm/front/user/toLogin.action", "登陆失败：用户名或者密码错误！");
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	@RequestMapping("/register")
	public void Register( HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		//设置请求对象字符编码！
		req.setCharacterEncoding("utf-8"); 
		Map<String,String> errors = this.validate(req); 
		if(errors.size()>0){ 
			req.setAttribute("errors", errors);
			//转发器,有错误就转发!!!!!! 
			req.getRequestDispatcher("/WEB-INF/pages/front/user/User_register.jsp").forward(req, resp);
		}else{
			User user = new User();
			
			String username = req.getParameter("username");
			String pwd = req.getParameter("pwd");
			String mobile = req.getParameter("mobile");
			String email = req.getParameter("email");
			String authcode = req.getParameter("authcode");
			
			user.setUsername(username);
			user.setPassword(pwd);
			user.setEmail(email);
			user.setMobile(mobile);
			 
			try {   
				String state = userService.register(user); 
				if(state.equals("UsernameAlreadyExist")){
					msg(req, resp, this.toRegister,"注册失败，用户名已经被注册！");  
					
				}else if(state.equals("EmailAlreadyExist")){
					 msg(req, resp, this.toRegister, "注册失败，邮箱已经被注册！");
					 
				}else if(state.equals("MobileAlreadyExist")){
					 msg(req, resp, this.toRegister, "注册失败，手机已经被注册！");
					 
				}else if(state.equals("success")){
					msg(req, resp,this.index , "恭喜您，注册成功！");
				}
				
			} catch (UsernameExistException e) {
				e.printStackTrace();
				errors.put("username", e.getMessage());
			}
			catch (EmailExistException e) {
				e.printStackTrace();
				errors.put("email", e.getMessage());
			}
			catch (Exception e) {
				e.printStackTrace();
			
			}
		} 
	}
	@RequestMapping("/loginOut")
	public void loginOut( HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		//设置请求对象字符编码！

		resp.setHeader("Cache-Control","no-cache"); 
		resp.setHeader("Cache-Control","no-store");   
		resp.setDateHeader("Expires",0); 
		resp.setHeader("Pragma","no-cache"); 
		req.getSession().removeAttribute("login_user"); 
	//转发器
	req.getRequestDispatcher("/").forward(req, resp);
	
	}
	
	
	@RequestMapping("/toLogin")
	public void toLogin( HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
	
		req.getRequestDispatcher("/WEB-INF/pages/front/user/User_login.jsp").forward(req, resp);

	
	}
	
	@RequestMapping("/toRegister")
	public void toRegister( HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
	
		req.getRequestDispatcher("/WEB-INF/pages/front/user/User_register.jsp").forward(req, resp);

	}

	private Map<String,String> validate(HttpServletRequest req){
		Map<String, String> errors = new HashMap<String, String>();
		
		String username = req.getParameter("username");  
		String pwd = req.getParameter("pwd");
		String pwd2 = req.getParameter("pwd2");
		String mobile = req.getParameter("mobile");
		String email = req.getParameter("email");
		String authcode = req.getParameter("authcode");
		
		//验证用户名
		if(null==username || username.isEmpty()){
			errors.put("username", "用户名不能为空！！");
		}else if(username.length()<6 || username.length()>20){
			errors.put("username", "用户名长度为6~20位！！");
		}else{
			//正则表达式校验！
			if(!username.matches("^[A-Za-z0-9_\\-\\u4e00-\\u9fa5]+$")){
				errors.put("username", "用户名只能由中文、英文、数字及“_”、“-”组成");
			}
		}
		
		//验证密码
		if(null==pwd || pwd.isEmpty()){
			errors.put("pwd", "密码不能为空！！");
		}else if(pwd.length()<6 || pwd.length()>20){
			errors.put("pwd", "密码长度为6~20位！！");
		}else{
			if(!pwd.matches("^.*[A-Za-z0-9\\w_-]+.*$")){
				errors.put("pwd", "密码只能由英文、数字及标点符号组成");
			}
		}
		
		//验证重复密码
		if(null==pwd2 || pwd2.isEmpty()){
			errors.put("pwd2", "重复密码不能为空！！");
		}else if(pwd2.length()<6 || pwd2.length()>20){
			errors.put("pwd2", "重复密码长度为6~20位！！");
		}else{
			if(!pwd2.matches("^.*[A-Za-z0-9\\w_-]+.*$")){
				errors.put("pwd2", "重复密码只能由英文、数字及标点符号组成");
			}
		}
		
		//验证邮箱
		if(null==email || email.isEmpty()){
			errors.put("email", "邮箱不能为空！！");
		}else if(email.length()<6 || email.length()>20){
			errors.put("email", "邮件地址只能在50个字符以内！！");
		}else{
			if(!email.matches("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$")){
				errors.put("email", "邮箱格式不正确");
			}
		}
		 
		//验证手机
		if(null==mobile || mobile.isEmpty()){
			errors.put("mobile", "手机不能为空！！");
		}else{
			if(!mobile.matches("^0?(13|15|18|14)[0-9]{9}$")){
				errors.put("mobile", "手机号码不符合规则,请重新填写");
			}
		}
		//验证码
		if(null==authcode || authcode.isEmpty()){ 
			errors.put("authcode", "验证码不能为空！！");
		}else{
			String oldAuthCode = (String) req.getSession().getAttribute("AUTHCODE");
			if(!authcode.equals(oldAuthCode)){
				errors.put("authcode", "验证码输入错误！");
			}
		}
		return errors;
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
