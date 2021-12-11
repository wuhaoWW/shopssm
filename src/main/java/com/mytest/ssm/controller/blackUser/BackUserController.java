package com.mytest.ssm.controller.blackUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mytest.ssm.entity.PageData;
import com.mytest.ssm.entity.User;
import com.mytest.ssm.exception.EmailExistException;
import com.mytest.ssm.exception.UsernameExistException;
import com.mytest.ssm.service.IUserService;

@Controller
@RequestMapping("/back/user")
public class BackUserController {
	private final static String toRegister = "/back/user/toAdd.action";
	private final static String index = "/back/user/list.action";
	private static final String userLIST = "/back/user/list.action";
	private static final String BACKPAGE = "BACKPAGE";
	@Autowired
	private IUserService userService;
	
	@RequestMapping("/toEDit")
	public void toEdit(HttpServletRequest req, HttpServletResponse resp) 
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
	@RequestMapping("/del")
	public void del(HttpServletRequest req, HttpServletResponse resp) 
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
			req.setAttribute("nextUrl", "/back/user/list.action");
			req.setAttribute("global_message", "用户删除成功！");
			req.getRequestDispatcher("/WEB-INF/pages/back/back_global_message.jsp").forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
}
	@RequestMapping("/edit")
	public void edit(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		try {
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
	@RequestMapping("/list")
	public void list(HttpServletRequest req, HttpServletResponse resp) 
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
	@RequestMapping("/toAdd")
	public void toAdd(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/pages/back/user/user_add.jsp").forward(req, resp);
		
	}
	@RequestMapping("/add")
	public void add(HttpServletRequest req, HttpServletResponse resp) 

			throws ServletException, IOException {
		//设置请求对象字符编码！
		req.setCharacterEncoding("utf-8"); 
		Map<String, String> errors = new HashMap<String, String>();
		
		String username = req.getParameter("username");  
		String pwd = req.getParameter("pwd");
		String pwd2 = req.getParameter("pwd2");
		String mobile = req.getParameter("mobile");
		String email = req.getParameter("email");
		
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
		}
		 
		//验证手机
		if(null==mobile || mobile.isEmpty()){
			errors.put("mobile", "手机不能为空！！");
		}
		
		
		if(errors.size()>0){ 
			req.setAttribute("errors", errors);
			//转发器,有错误就转发!!!!!! 
			req.getRequestDispatcher("/WEB-INF/pages/back/user/user_add.jsp").forward(req, resp);
		}else{
			User user = new User();
			
			user.setUsername(username);
			user.setPassword(pwd);
			user.setEmail(email);
			user.setMobile(mobile);
			 
			try {   
				String state = userService.register(user); 
				if(state.equals("UsernameAlreadyExist")){
					msg(req, resp, this.toRegister,"添加失败，用户名已经被注册！");  
					
				}else if(state.equals("EmailAlreadyExist")){
					 msg(req, resp, this.toRegister, "添加失败，邮箱已经被注册！");
					 
				}else if(state.equals("MobileAlreadyExist")){
					 msg(req, resp, this.toRegister, "添加失败，手机已经被注册！");
					 
				}else if(state.equals("success")){
					msg(req, resp,this.index , "恭喜您，添加成功！");
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
	
	
	
	private void msg(HttpServletRequest req, HttpServletResponse resp, 
	          String nextUrl, String global_message) throws ServletException, IOException {
					//设置nextUrl与global_message
					req.setAttribute("nextUrl", nextUrl);
					req.setAttribute("global_message", global_message);
					//转发器
					req.getRequestDispatcher("/WEB-INF/pages/back/back_global_message.jsp").forward(req, resp);
		}
}
