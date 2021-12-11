package com.mytest.ssm.entity;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName(value = "t_product")
public class Product {
	
	/**
	 * id
	 */
	@TableId(value = "P_ID",type = IdType.AUTO)
	private Integer id;
	/**
	 * 产品名称 
	 */
	@TableField(value = "P_NAME")
	private String name;
	/**
	 * 产品编号
	 */
	@TableField(value = "P_NUMBER")
	private String number;
	/**
	 * 市场价
	 */
	@TableField(value = "P_MARKET_PRICE")
	private Double marketPrice;
	/**
	 * 折扣价
	 */
	@TableField(value = "P_DISCOUTED_PRICE")
	private Double discountedPrice ;
	/**
	 * 录入日期
	 */
	@TableField(value = "P_ADD_DATE")
	private Date addDate;
	/**
	 * 产品介绍 
	 */
	
	@TableField(value = "P_BRAND_ID")
	private Integer brandId;
	@TableField(value = "P_CATEGORY_ID")
	private Integer categoryId;
	
	@TableField(value = "P_DESC")
	private String descs;
	/**
	 * 产品颜色
	 */
	@TableField(value = "P_COLOR")
	private String color;
	/**
	 * 产品购买说明
	 */
	@TableField(value = "P_REMARK")
	private String remark;
	
	/**
	 * 产品主图片
	 */
	@TableField(value = "P_MAINIMAGE")
	private String mainImage;
	/**
	 * 是否推荐
	 */
	@TableField(value = "P_RECOMMEND")
	private boolean recommend; 
	/**
	 * 是否促销
	 */
	@TableField(value = "P_PROMOTION")
	private boolean promotion;
	/**
	 * 关注次数
	 */
	@TableField(value = "P_ATTENTION")
	private int attention;
	/**
	 * 购买次数
	 */
	@TableField(value = "P_BUY_COUNT")
	private long buyCount;
	/**
	 * 当前产品的分类和上面的所有父分类组成的id序列
	 * 形如|5|,|2|,|1|
	 * 表示当前产品的直接分类为5,5的父分类为2，2的父分类为1
	 */
	@TableField(value = "P_CLS")
	private String cls;
	/**10
	 * 产品所属品牌
	 */ 
	@TableField(exist = false)
	private Brand brand;
	/**
	 * 产品所属类别
	 */
	@TableField(exist = false)
	private Category category;
	/**
	 * 产品颜色 
	 */ 
	@TableField(exist = false)
	private List<ProductImage> productImagesList;
	
	
	
}
  