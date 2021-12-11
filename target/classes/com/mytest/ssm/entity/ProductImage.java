package com.mytest.ssm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName(value = "t_product_image")
public class ProductImage {
	/**
	 * 图片id
	 * int的默认值为0,Integer的默认值为null
	 */
	@TableId(value = "PI_ID",type = IdType.AUTO)
	private Integer id;
	/**
	 * 图片名称
	 */
	@TableField(value = "PI_NAME")
	private String name;
	/**
	 * 图片集合
	 */
	@TableField(value = "PI_URL")
	private String url;
	/**
	 * 图片排序
	 */
	@TableField(value = "PI_INDEX")
	private int indexs;
	/**
	 * 图片所属产品Id,重要
	 */
	@TableField(value = "PI_PRODUCT_ID")
	private int	productId;
	
	

	
	
}
