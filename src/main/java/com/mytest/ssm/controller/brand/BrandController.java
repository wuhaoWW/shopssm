package com.mytest.ssm.controller.brand;

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

import com.mytest.ssm.entity.Brand;
import com.mytest.ssm.entity.PageData;
import com.mytest.ssm.service.IBrandService;
import com.mytest.ssm.utils.FileUploadUtils;

@Controller
@RequestMapping("/back")
public class BrandController {
	private static final String savePath = "/resource/upload/brand";
	@Autowired
	private IBrandService brandService;
	
	
	@RequestMapping("/brandBrandAdd")
	public void brandBrandAdd(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException  {

		FileUploadUtils uploadObj = new FileUploadUtils(req); 
		Map<String,String> errors = this.validate(uploadObj);   
		if(errors.size()>0){
			//有错误就转发
			req.setAttribute("errors", errors);
			req.getRequestDispatcher("/WEB-INF/pages/back/brand/Brand_add.jsp").forward(req, resp);
		}else{  
			try {    
				//Dao, 把对象存进数据库，然后页面跳转 
				Brand brand = getBrandFromRequest(uploadObj); 
				brandService.add(brand);//添加到数据库 
				req.setAttribute("global_message", "品牌添加成功！");
				req.setAttribute("nextUrl", "/back/brandList.action");
				req.getRequestDispatcher("/WEB-INF/pages/back/back_global_message.jsp").forward(req, resp);
			} catch (Exception e) { 
				e.printStackTrace();  
				req.setAttribute("global_message", "品牌添加失败！");
				req.setAttribute("nextUrl", "/back/brandToAdd.action");  
				req.getRequestDispatcher("/WEB-INF/pages/back/back_global_message.jsp").forward(req, resp);
			}  
		}  
	}
	@RequestMapping("/brandList")
	public void brandList(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException  {
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
	@RequestMapping("/brandToAdd")
	public void brandToAdd(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException  {
		req.getRequestDispatcher("/WEB-INF/pages/back/brand/Brand_add.jsp").forward(req, resp);
	}
	@RequestMapping("/brandEdit")
	public void brandEdit(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException  {
//		Map<String,String> errors = this.validate(req);
		
//		if(errors.size()>0){
//			//有错误就转发
//			req.getRequestDispatcher("/WEB-INF/pages/back/brand/Brand_add.jsp").forward(req, resp);
//		}else{ 
			try {
				//获取新的brand
				FileUploadUtils uploadUtils = new FileUploadUtils(req);
				
				String idStr = uploadUtils.getParameter("id");
				Integer id = Integer.valueOf(idStr);
				
				String cnName = uploadUtils.getParameter("cnName");
				String enName = uploadUtils.getParameter("enName");
				
				//图片上传为空怎么搞？
				String newSmallPhoto = uploadUtils.processSingleUploadFile("smallPhoto", savePath);
				String newBigPhoto = uploadUtils.processSingleUploadFile("bigPhoto", savePath);
				String desc = uploadUtils.getParameter("desc");
				
				Brand brand = new Brand();
				brand.setId(id);
				brand.setCnName(cnName);
				brand.setEnName(enName);
				brand.setDescs(desc);
				
				if(null!=newSmallPhoto && !newSmallPhoto.isEmpty()){
					//用新上传的图片路径
					brand.setSmallPhoto(newSmallPhoto);
				}else{
					//用旧的图片路径值
					String unChangedSP = uploadUtils.getParameter("unChangedSmallPhoto");
					brand.setSmallPhoto(unChangedSP);
				}
				if(null!=newBigPhoto && !newBigPhoto.isEmpty()){
					brand.setBigPhoto(newBigPhoto);
				}else{
					String unChangedBP = uploadUtils.getParameter("unChangedbigPhoto");
					brand.setBigPhoto(unChangedBP);
				}
				brandService.edit(brand);
				req.setAttribute("global_message", "品牌修改成功！");
				req.setAttribute("nextUrl", "/back/brandList.action");
				req.getRequestDispatcher("/WEB-INF/pages/back/back_global_message.jsp").forward(req, resp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
//	}
	
	@RequestMapping("/brandToEdit")
	public void brandToEdit(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException  {
		String idStr = req.getParameter("id");//1
		Integer id = Integer.valueOf(idStr);  //1
		try {  
			//根据id查找记录 
			Brand brand = brandService.findById(id);
			req.setAttribute("brand", brand);
			//转发器  
			req.getRequestDispatcher("/WEB-INF/pages/back/brand/Brand_edit.jsp").forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}}
	
	@RequestMapping("/brandDel")
	public void brandDel(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException  {
		String[] ids = req.getParameterValues("id");
		List<Integer> idList = new ArrayList<Integer>();
		if(null != ids && ids.length>0){
			for (int i = 0; i < ids.length; i++) {
				Integer id = Integer.valueOf(ids[i]);
				idList.add(id);
			}
		}
		try {
			brandService.delete(idList.toArray(new Integer[]{}));
			req.setAttribute("nextUrl", "/back/brandList.action");
			req.setAttribute("global_message", "品牌删除成功！");
			req.getRequestDispatcher("/WEB-INF/pages/back/back_global_message.jsp").forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("nextUrl", "/back/brandList.action");
			req.setAttribute("global_message", "品牌删除失败！！具体原因我们会尽快查找");
			req.getRequestDispatcher("/WEB-INF/pages/back/back_global_message.jsp").forward(req, resp);
		}
		
	}
	
	
	
	private Map<String, String> validate(FileUploadUtils uploadObj){
		Map<String,String> errors = new HashMap<String, String>();
		//检验代码
		
		String cnName = uploadObj.getParameter("cnName");
		String enName = uploadObj.getParameter("enName");
	
		String smallPhoto = uploadObj.processSingleUploadFile("smallPhoto", savePath);
		String bigPhoto = uploadObj.processSingleUploadFile("bigPhoto", savePath); 
		String desc = uploadObj.getParameter("desc");
		//验证中文名称
		if (cnName == null || cnName.isEmpty()) {
			errors.put("cnName", "中文名称不能为空！！");
		}
		//验证英文名称
		if (enName == null || enName.isEmpty()) {
			errors.put("enName", "英文名称不能为空！！");
		}
		//验证小图片
		if (smallPhoto == null || smallPhoto.isEmpty()) {
			errors.put("smallPhoto", "小图片不能为空！！");
		}
		//验证大图片
		if (bigPhoto == null || bigPhoto.isEmpty()) {
			errors.put("bigPhoto", "大图片不能为空！！");
		}
		return errors; 
	}   
	
	private Brand getBrandFromRequest(FileUploadUtils uploadObj){
		
		String cnName = uploadObj.getParameter("cnName");
		String enName = uploadObj.getParameter("enName");
		
		
		String smallPhoto = uploadObj.processSingleUploadFile("smallPhoto", savePath);
		String bigPhoto = uploadObj.processSingleUploadFile("bigPhoto", savePath); 
		String desc = uploadObj.getParameter("desc");
		
		Brand brand = new Brand();
		brand.setCnName(cnName);
		brand.setEnName(enName); 
		brand.setDescs(desc); 
		
		if(smallPhoto!=null){
			brand.setSmallPhoto(smallPhoto);
		}else{
			brand.setSmallPhoto(" ");
		}
		
		if(bigPhoto!=null){
			brand.setBigPhoto(bigPhoto); 
		}else{
			brand.setBigPhoto(" ");
		}
		
		return brand; 
	}
}
