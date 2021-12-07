package com.mytest.ssm.controller.product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mytest.ssm.entity.Brand;
import com.mytest.ssm.entity.Category;
import com.mytest.ssm.entity.Product;
import com.mytest.ssm.entity.ProductImage;
import com.mytest.ssm.service.IBrandService;
import com.mytest.ssm.service.ICategoryService;
import com.mytest.ssm.service.IProductService;
import com.mytest.ssm.service.impl.BrandServiceImpl;
import com.mytest.ssm.service.impl.CategoryServiceImpl;
import com.mytest.ssm.service.impl.ProductServiceImpl;
import com.mytest.ssm.utils.FileUploadUtils;

public class ProductAddServlet extends HttpServlet{
	private IBrandService brandService = new BrandServiceImpl();
	private ICategoryService categoryService = new CategoryServiceImpl();
	private IProductService productService = new ProductServiceImpl();
	 
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			this.doPost(req, resp);
	}   
 
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException { 
		FileUploadUtils uploadObj = new FileUploadUtils(req);//构造方法初始化数据
		Map<String,String> errors = this.validate(uploadObj);   
		if(errors.size()>0){
			//有错误就转发 
			req.setAttribute("errors", errors);
			req.getRequestDispatcher("/WEB-INF/pages/back/product/product_add.jsp").forward(req, resp);
		}else{
			Product product = getProductFromRequest(uploadObj);
			
			
			try {
				productService.add(product);
				req.setAttribute("global_message", "产品添加成功！");
				req.setAttribute("nextUrl", "/back/product/productList.do");
				req.getRequestDispatcher("/WEB-INF/pages/back/back_global_message.jsp").forward(req, resp);
			} catch (Exception e) {
				e.printStackTrace();
				req.setAttribute("global_message", "产品添加失败！");
				req.setAttribute("nextUrl", "/back/product/productToAdd.do");  
				req.getRequestDispatcher("/WEB-INF/pages/back/back_global_message.jsp").forward(req, resp);
			}  
		}
	}  
	 
	
	private Map<String, String> validate(FileUploadUtils uploadObj){
//		FileUploadUtils uploadObj = new FileUploadUtils(req);//构造方法初始化数据
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
		
		return errors;
	}
	
	private Product getProductFromRequest(FileUploadUtils uploadObj){
//		FileUploadUtils uploadObj = new FileUploadUtils(req);//构造方法初始化数据
		 
		String name = uploadObj.getParameter("name");
		String number = uploadObj.getParameter("number");
		String remark = uploadObj.getParameter("remark");
		String brandStr = uploadObj.getParameter("brandID"); 
		String categoryStr = uploadObj.getParameter("categoryID");
		String marketPriceStr = uploadObj.getParameter("marketPrice");
		System.out.println(name+number+remark+marketPriceStr);
		String discountedPriceStr = uploadObj.getParameter("discountedPrice");
		String color = uploadObj.getParameter("color");
		String recommend = uploadObj.getParameter("recommend");
		String promotion = uploadObj.getParameter("promotion"); 
		String desc = uploadObj.getParameter("desc");  
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
		p.setDesc(desc);
		p.setMainImage(mainImage);
		
		//设置Brand
		p.setBrand(new Brand(brandId));
		//设置Category
		try {
			Category category = categoryService.findById(categoryId);
			p.setCategory(category); 
			p.setCls("|"+category.getId()+"|,"+category.getCls());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
					pi.setIndex(i+1);
					imagesList.add(pi);
				}
		}
		p.setProductImagesList(imagesList);
		return p;
	}
	
}
