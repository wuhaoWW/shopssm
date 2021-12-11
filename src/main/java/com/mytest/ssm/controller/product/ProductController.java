package com.mytest.ssm.controller.product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import com.mytest.ssm.entity.Category;
import com.mytest.ssm.entity.PageData;
import com.mytest.ssm.entity.Product;
import com.mytest.ssm.entity.ProductImage;
import com.mytest.ssm.service.IBrandService;
import com.mytest.ssm.service.ICategoryService;
import com.mytest.ssm.service.IProductService;
import com.mytest.ssm.utils.FileUploadUtils;

@Controller
@RequestMapping("/back/product")
public class ProductController {
	private static final Integer categoryLevel = 1;
	 private static final String savePath = "/resource/upload/product";
	@Autowired
	private IProductService productService;
	@Autowired
	private IBrandService brandService;
	@Autowired
	private ICategoryService categoryService;
	
	@RequestMapping("/toAdd")
	public void toAdd(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		try { 
			List<Brand> brandList = brandService.listAllBrand();
			List<Category> categoryList = categoryService.listAllCategory(categoryLevel);
			req.setAttribute("brandList", brandList); 
			req.setAttribute("categoryList", categoryList);
			
			//请求转发 
			req.getRequestDispatcher("/WEB-INF/pages/back/product/product_add.jsp").forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
	}
	@RequestMapping("/add")
	public void add(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		FileUploadUtils uploadObj = new FileUploadUtils(req);//构造方法初始化数据
		Map<String,String> errors = new HashMap<String, String>();
		//检验代码 
		String name = uploadObj.getParameter("name");
		String number = uploadObj.getParameter("number");
		String remark = uploadObj.getParameter("remark");
		String brandStr = uploadObj.getParameter("brandID"); 
		String categoryStr = uploadObj.getParameter("categoryID");
		String marketPriceStr = uploadObj.getParameter("marketPrice");
		String discountedPriceStr = uploadObj.getParameter("discountedPrice");
		String color = uploadObj.getParameter("color");
		String recommend = uploadObj.getParameter("recommend");
		String promotion = uploadObj.getParameter("promotion"); 
		String desc = uploadObj.getParameter("desc");  
		System.out.println(name+number+remark+marketPriceStr);
		if (name==null || name.isEmpty()) {
			errors.put("name", "产品名称不得为空");
		}
		if (number==null || number.isEmpty()) {
			errors.put("number", "编号不得为空");
		}
		if (remark==null || remark.isEmpty()) {
			errors.put("remark", "购买说明不得为空");
		}
		if (brandStr==null || brandStr.isEmpty()) {
			errors.put("brandID", "所属品牌不得为空");
		}
		if (categoryStr==null || categoryStr.isEmpty()) {
			errors.put("categoryID", "所属分类不得为空");
		}
		if (marketPriceStr==null || marketPriceStr.isEmpty()) {
			errors.put("marketPrice", "市场价不得为空");
		}
		if (discountedPriceStr==null || discountedPriceStr.isEmpty()) {
			errors.put("discountedPrice", "宏购价不得为空");
		}  
		if(errors.size()>0){
			//有错误就转发 
			req.setAttribute("errors", errors);
			req.getRequestDispatcher("/WEB-INF/pages/back/product/product_add.jsp").forward(req, resp);
		}else{
			Double marketPriceDou = null;
			try { 
				marketPriceDou = Double.parseDouble(marketPriceStr);
			} catch (Exception e) {
				System.out.println("marketPriceStrs"+marketPriceStr);
				// TODO: handle exception
			}
			Double discountedPriceDou = Double.parseDouble(discountedPriceStr);
			  
			Integer brandId = Integer.valueOf(brandStr);
			Integer categoryId = Integer.valueOf(categoryStr);
			
			//processSingleUploadFile("name值","路径");
			String mainImage = uploadObj.processSingleUploadFile("mainImage", "/resource/upload/product");
			String[] otherImages = uploadObj.processUploadFiles("otherImages", "/resource/upload/product");
			
			Product p = new Product();
			p.setName(name);
			p.setNumber(number); 
			p.setRemark(remark); 
			p.setMarketPrice(marketPriceDou);
			p.setDiscountedPrice(discountedPriceDou);
			p.setAddDate(new Date());
			p.setColor(color);
			p.setDescs(desc);
			p.setMainImage(mainImage);
			p.setBrandId(brandId);
			p.setCategoryId(categoryId);
			if(null!=recommend && recommend.equals("select")){
				p.setRecommend(true);
			}else{
				p.setRecommend(false);
			}
			if(null!=promotion && promotion.equals("select")){
				p.setPromotion(true); 
			}else{
				p.setPromotion(false);   
			} 
			
			//处理otherImages副图片
			List<ProductImage> imagesList = new ArrayList<ProductImage>();
			if(null!=otherImages && otherImages.length>0){
					for (int i = 0; i < otherImages.length; i++) {
						String str = otherImages[i];
						ProductImage pi = new ProductImage();
						pi.setName(str.substring(str.lastIndexOf('/')+1, str.length()));
						pi.setUrl(str);
						pi.setIndexs(i+1);
						imagesList.add(pi);
					}
			}
			p.setProductImagesList(imagesList);
			
			
			try {
				productService.add(p);
				req.setAttribute("global_message", "产品添加成功！");
				req.setAttribute("nextUrl", "/back/product/list.action");
				req.getRequestDispatcher("/WEB-INF/pages/back/back_global_message.jsp").forward(req, resp);
			} catch (Exception e) {
				e.printStackTrace();
				req.setAttribute("global_message", "产品添加失败！");
				req.setAttribute("nextUrl", "/back/product/toAdd.action");  
				req.getRequestDispatcher("/WEB-INF/pages/back/back_global_message.jsp").forward(req, resp);
			}  
		}
		
	}
	
	@RequestMapping("/toEdit")
	public void toEdit(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String idStr = req.getParameter("id");//1
		Integer id = Integer.valueOf(idStr);  //1
		try {  
			//根据id查找记录
			Product product = productService.findById(id);
			List<Brand> brandList = brandService.listAllBrand();
			List<Category> categoryList = categoryService.listAllCategory(categoryLevel);
			
			req.setAttribute("product", product);
			req.setAttribute("brandList", brandList);
			req.setAttribute("categoryList", categoryList);
			//转发器  
			req.getRequestDispatcher("/WEB-INF/pages/back/product/product_edit.jsp").forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@RequestMapping("/edit")
	public void edit(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		try {
			FileUploadUtils uploadObj = new FileUploadUtils(req);//构造方法初始化数据
			 
			String idStr = uploadObj.getParameter("id");
			Integer productId = Integer.valueOf(idStr);
			String name = uploadObj.getParameter("name");
			String number = uploadObj.getParameter("number");
			String remark = uploadObj.getParameter("remark");
			String brandStr = uploadObj.getParameter("brandID"); 
			String categoryStr = uploadObj.getParameter("categoryID");
			String marketPriceStr = uploadObj.getParameter("marketPrice");
			String discountedPriceStr = uploadObj.getParameter("discountedPrice");
			String color = uploadObj.getParameter("color");
			String recommend = uploadObj.getParameter("recommend");
			String promotion = uploadObj.getParameter("promotion"); 
			String desc = uploadObj.getParameter("desc");  
			Double marketPriceDou = Double.valueOf(marketPriceStr);
			Double discountedPriceDou = Double.valueOf(discountedPriceStr);
			  
			Integer brandId = Integer.valueOf(brandStr);
			Integer categoryId = Integer.valueOf(categoryStr);
			
			//processSingleUploadFile("name值","路径");
			String mainImage = uploadObj.processSingleUploadFile("mainImage", savePath);
			String otherImages1 = uploadObj.processSingleUploadFile("otherImages1", savePath);
			String otherImages2 = uploadObj.processSingleUploadFile("otherImages2", savePath);
			
			Product p = new Product();
			p.setId(productId);
			p.setName(name);
			p.setNumber(number); 
			p.setRemark(remark); 
			p.setMarketPrice(marketPriceDou);
			p.setDiscountedPrice(discountedPriceDou);
			p.setAddDate(new Date());
			p.setColor(color);
			p.setDescs(desc);
			p.setBrandId(brandId);
			p.setCategoryId(categoryId);
			
			//设置recommend
			if(null!=recommend && recommend.equals("select")){
				p.setRecommend(true);
			}else{
				p.setRecommend(false);
			}
			
			//设置promotion
			if(null!=promotion && promotion.equals("select")){  
				p.setPromotion(true); 
			}else{
				p.setPromotion(false);   
			} 
			
			//设置mainImage图片
			if(null!=mainImage && !mainImage.isEmpty()){
				//用新上传的图片路径
				p.setMainImage(mainImage);
			}else{
				//否则用旧的图片路径值
				String unChangedMainImage = uploadObj.getParameter("unChangedMainImage");
				p.setMainImage(unChangedMainImage);
			}
			
			//设置otherImages副图片 
			ProductImage pi1 = new ProductImage();
			pi1.setProductId(productId);
			pi1.setIndexs(1);
			if(null!=otherImages1 && !otherImages1.isEmpty()){
				pi1.setUrl(otherImages1);
				pi1.setName(otherImages1.substring(otherImages1.lastIndexOf('/')+1, otherImages1.length()));
			}else{
				String unChangedOtherImages1 = uploadObj.getParameter("unChangedOtherImages1");
				if (unChangedOtherImages1!=null) {
					pi1.setUrl(unChangedOtherImages1);
					pi1.setName(unChangedOtherImages1.substring(unChangedOtherImages1.lastIndexOf('/')+1, unChangedOtherImages1.length()));
			
				}
			}
			
			ProductImage pi2 = new ProductImage();
			pi2.setProductId(productId);
			pi2.setIndexs(2);
			if(null!=otherImages2 && !otherImages2.isEmpty()){
				pi2.setUrl(otherImages2);
				pi2.setName(otherImages2.substring(otherImages2.lastIndexOf('/')+1, otherImages2.length()));
			}else{
				
				String unChangedOtherImages2 = uploadObj.getParameter("unChangedOtherImages2");
if (unChangedOtherImages2!=null ) {
	pi2.setUrl(unChangedOtherImages2);
	pi2.setName(unChangedOtherImages2.substring(unChangedOtherImages2.lastIndexOf('/')+1, unChangedOtherImages2.length()));

				}
			}
			
			List<ProductImage> imagesList = new ArrayList<ProductImage>();
			//先添加pi1再添加pi2,有顺序的
			imagesList.add(pi1);
			imagesList.add(pi2);
			p.setProductImagesList(imagesList);
			productService.edit(p);
			req.setAttribute("global_message", "产品修改成功！");
			req.setAttribute("nextUrl", "/back/product/list.action");
			req.getRequestDispatcher("/WEB-INF/pages/back/back_global_message.jsp").forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}  
		
	}
	
	
	@RequestMapping("/del")
	public void del(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		//获得用户从页面传来的数据
		String[] ids = req.getParameterValues("id");
		List<Integer> idList = new ArrayList<Integer>();
		if(null != ids && ids.length>0){
			for (int i = 0; i < ids.length; i++) {
				Integer id = Integer.valueOf(ids[i]);
				idList.add(id);
			}
		}
		try {
			productService.delete(idList.toArray(new Integer[]{}));
			req.setAttribute("nextUrl", "/back/product/list.action");
			req.setAttribute("global_message", "产品删除成功！");
			req.getRequestDispatcher("/WEB-INF/pages/back/back_global_message.jsp").forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("nextUrl", "/back/product/list.action");
			req.setAttribute("global_message", "产品删除失败！！具体原因我们会尽快查找");
			req.getRequestDispatcher("/WEB-INF/pages/back/back_global_message.jsp").forward(req, resp);
		}
	}
	@RequestMapping("/detail")
	public void detail(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String p_idStr = req.getParameter("id");
		Integer p_idInt = Integer.valueOf(p_idStr);
		
		try {
			Product	product = productService.findById(p_idInt);
			req.setAttribute("product", product);
			req.getRequestDispatcher("/WEB-INF/pages/front/product/product_detail.jsp").forward(req, resp);
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
			
			String productName = req.getParameter("productName");
			String brandName = req.getParameter("brandName");
			String categoryName = req.getParameter("categoryName");
			
			String page = req.getParameter("page");
			String rows = req.getParameter("rows");
			
			//默认第一页和30条记录数
			int pageInt = PageData.PAGE;//1
			int rowsInt = PageData.ROWS;//30
			if(null!=productName && productName.length()>0){
				conditions.put("productName", productName);
			}
			if(null!=brandName && brandName.length()>0){
				conditions.put("brandName", brandName);
			}
			if(null!=categoryName && categoryName.length()>0){
				conditions.put("categoryName", categoryName);
			}
			if(null!=page && page.length()>0){
				pageInt = Integer.valueOf(page);
			} 
			if(null!=rows && rows.length()>0){
				rowsInt = Integer.valueOf(rows); 
			}
			orderBy.put("P_ID", "asc");    
			
			PageData pageData =  					 //1      //10
					productService.listPage(conditions,pageInt,rowsInt,orderBy);
			  
			String reqUri = req.getRequestURI();
			req.setAttribute("reqUri", reqUri);
			req.setAttribute("pageData", pageData);
			req.setAttribute("productName", productName);
			req.setAttribute("brandName", brandName); 
			req.setAttribute("categoryName", categoryName);
			
			req.getRequestDispatcher("/WEB-INF/pages/back/product/product_list.jsp").forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
