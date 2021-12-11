package com.mytest.ssm.controller.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mytest.ssm.entity.Admin;
import com.mytest.ssm.entity.PageData;
import com.mytest.ssm.service.IAdminService;

@Controller
@RequestMapping("/back/admin")
public class AdminController {
	
	private static final String TOLOGIN = "/back/admin/toLogin.action";
	private static final String BACKINDEX = "/WEB-INF/pages/back/backIndex.jsp";
	private static final String BACKPAGE = "BACKPAGE";
	private static final String BACKHOMEOAGE = "/back/welcome.do";
	private static final String ADMINLIST = "/back/admin/list.action";
	private final static String toADD = "/back/admin/toAdd.action";
	private final static String adminList = "/back/admin/list.action";
	
	@Autowired
	private IAdminService adminService;
	
	@RequestMapping("/login")
	public void adminLogin(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
		try {
			HttpSession session = req.getSession();
			String ln = (String) session.getAttribute("loginName");
			//假如session的loginName不为空
			if(null!=ln && ln.length()>0){
				req.getRequestDispatcher(BACKINDEX).forward(req, resp);
				return;
			}
				
			String loginName = req.getParameter("loginName");
			String password = req.getParameter("password"); 
			String authcode = req.getParameter("authcode");
			System.out.println(loginName);
			//acFromSession不会为null
			String acFromSession = (String) session.getAttribute("AUTHCODE");
			
//			if(authcode==null || authcode.isEmpty()){
//				msg(req, resp, TOLOGIN, "登陆失败：验证码输入不能为空！！");
//				return;
//			}else if(!authcode.equals(acFromSession)){
//				msg(req, resp, TOLOGIN, "登陆失败：验证码输入错误！！");
//				return;
//			}
			boolean loginFlag = adminService.login(loginName, password);
			if(loginFlag){
				//创建cookie 
				Cookie cookie = new Cookie(loginName, password);
				/**
				 * 如果不设置过期时间，则表示这个cookie生命周期为从创建到浏览器关闭止，只要关闭浏览器窗口，cookie就消失了。
				 * 这种生命期为浏览会话期的cookie被称为会话cookie。会话cookie一般不保存在硬盘上而是保存在内存里。
				 */
				/**
				 * 如果设置了过期时间(setMaxAge(606024))，浏览器就会把cookie保存到硬盘上，关闭后再次打开浏览器，
				 * 这些cookie依然有效直到超过设定的过期时间。
				 */
				//一个月以内有效
				cookie.setMaxAge(60*60*24*30); 
				cookie.setPath(req.getContextPath());
				//添加cookie到响应对象
				resp.addCookie(cookie);
				
				/**就算不添加自己创建的cookie，服务器也会返回一个jsessionId给浏览器*/
				//设置单位为秒，设置为-1永不过期
				session.setMaxInactiveInterval(-1);
				//添加adminName到session
				session.setAttribute("loginName", loginName);
				//跳转到后台首页
				req.getRequestDispatcher(BACKINDEX).forward(req, resp);
			}else{
				msg(req, resp, TOLOGIN, "登陆失败：用户名或者密码错误！！");
			} 
				} catch (Exception e) {
					e.printStackTrace();
				}
		
		
	}
	
	@RequestMapping("/pwdChange")
	public void adminPwdChange(HttpServletRequest req,HttpServletResponse resp)throws ServletException, IOException{
		try {
			String idStr = req.getParameter("adminId");
			Integer id = Integer.valueOf(idStr);
			String adminName = req.getParameter("adminName");  
			String oldPwd = req.getParameter("oldPassword");
			
			Admin admin = new Admin();
			admin.setId(id);
			admin.setAdminName(adminName); 
			admin.setPassword(oldPwd);  
			String[] newPwd = req.getParameterValues("newPassword");
			
			Admin adminFromDatabase = adminService.findByName(admin.getAdminName());
			if(!admin.getPassword().equals(adminFromDatabase.getPassword())){
				msg(req, resp, BACKPAGE, "修改密码失败,旧密码不正确！");
			}else if(!newPwd[0].equals(newPwd[1])){
				msg(req, resp, BACKPAGE, "修改密码失败,两次新密码不一致！");
			}else{
				boolean flag = adminService.editPwd(admin.getId(),newPwd[0]);
				if(flag){
					msg(req, resp, BACKHOMEOAGE, "修改密码成功！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/loginOut")
	public void LoginOut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//转发器
		HttpSession session = req.getSession();
		String loginName = (String) session.getAttribute("loginName");
		if(null!=loginName && loginName.length()>0){
			session.removeAttribute("loginName");
		}
		//转发器
		req.getRequestDispatcher("/WEB-INF/pages/back/admin/admin_login.jsp").forward(req, resp);
		
	}
	
	@RequestMapping("/toAdd")
	public void toAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//转发器
				req.getRequestDispatcher("/WEB-INF/pages/back/admin/admin_add.jsp").forward(req, resp);
	}
	@RequestMapping("/toPwdChange")
	public void toPwdChange(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//转发器
		 
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
	@RequestMapping("/toLogin")
	public void toLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//转发器
		req.getRequestDispatcher("/WEB-INF/pages/back/admin/admin_login.jsp").forward(req, resp);
		} 
	@RequestMapping("/add")
	public void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		 
		String adminName = req.getParameter("adminName");  
		String password = req.getParameter("password");  
		String realName = req.getParameter("realName");  
		String gender =  req.getParameterValues("gender")[0];
		String desc = req.getParameter("desc");   
		
		Admin admin = new Admin();
		
		admin.setAdminName(adminName); 
		admin.setPassword(password); 
		admin.setRealName(realName);
		admin.setSex(gender); 
		admin.setDescs(desc);
		try {  
			String password2 = req.getParameter("password2"); 
			if(!admin.getPassword().equals(password2)){  
				msg(req, resp, toADD,"注册失败，两次输入的密码不一致！"); 
			} 
			
			String state = adminService.add(admin); 
			if(state.equals("AdminAlreadyExist")){
				msg(req, resp, toADD,"注册失败，管理员已经被注册！");
				
			}else if(state.equals("success")){  
				msg(req, resp, adminList , "恭喜您，注册成功！"); 
			}
		} catch (Exception e) { 
			e.printStackTrace();   
		} 	} 
	@RequestMapping("/del")
	public void del(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
			adminService.delete(idList.toArray(new Integer[]{}));
			req.setAttribute("nextUrl", "/back/admin/list.action");
			req.setAttribute("global_message", "管理员删除成功！");
			req.getRequestDispatcher("/WEB-INF/pages/back/back_global_message.jsp").forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}	} 
	@RequestMapping("/edit")
	public void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			//修改数据库记录
			String idStr = req.getParameter("adminId");
			Integer id = Integer.valueOf(idStr);
			String adminName = req.getParameter("adminName");  
			String password = req.getParameter("password");  
			String realName = req.getParameter("realName");  
			String gender =  req.getParameterValues("gender")[0];
			String desc = req.getParameter("desc");   
			
			Admin admin = new Admin();
			
			admin.setId(id);
			admin.setAdminName(adminName); 
			admin.setPassword(password); 
			admin.setRealName(realName);
			admin.setSex(gender); 
			admin.setDescs(desc);   
			boolean flag = adminService.edit(admin);
			if(flag){
				msg(req, resp, ADMINLIST, "管理员信息修改成功！");
			}else{ 
				msg(req, resp, BACKPAGE, "管理员信息修改失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	} 
	@RequestMapping("/toEdit")
	public void toEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 
			String adminName = req.getParameter("adminName");
			if(null!=adminName && adminName.length()>0){
				try {
					//根据adminName查找记录 
					Admin admin = adminService.findByName(adminName);
					req.setAttribute("admin", admin);
					//转发器  
					req.getRequestDispatcher("/WEB-INF/pages/back/admin/admin_edit.jsp").forward(req, resp);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}}
	@RequestMapping("/list")
	public void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Map<String,Object> conditions = new HashMap<String,Object>();
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String,String>();		
			//默认第一页和10条记录数
			int pageInt = PageData.PAGE;//1
			int rowsInt = PageData.ROWS;//10
			
			String adminName = req.getParameter("adminName"); 
			String page = req.getParameter("page");
			String rows = req.getParameter("rows");
			
			if(null!=adminName && adminName.length()>0){
				conditions.put("adminName", adminName); 
			}
			if(null!=page && page.length()>0){
				//更新pageInt
				pageInt = Integer.valueOf(page);
			} 
			if(null!=rows && rows.length()>0){
				//更新rowsInt
				rowsInt = Integer.valueOf(rows); 
			}   
			orderBy.put("A_ID", "asc");
			
			PageData pageData =  					 //1      //10
					adminService.listPage(conditions,pageInt,rowsInt,orderBy);
			  
			String reqUri = req.getRequestURI();
			req.setAttribute("reqUri", reqUri);
			req.setAttribute("pageData", pageData);
			req.setAttribute("adminName", adminName);  
			
			req.getRequestDispatcher("/WEB-INF/pages/back/admin/admin_list.jsp").forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		} }
	private void msg(HttpServletRequest req, HttpServletResponse resp, 
	          String nextUrl, String global_message) throws ServletException, IOException {
					//设置nextUrl
					req.setAttribute("nextUrl", nextUrl);
					//设置global_message
					req.setAttribute("global_message", global_message);
					//转发器
					req.getRequestDispatcher("/WEB-INF/pages/back/back_global_message.jsp").forward(req, resp);
		}

	

}
