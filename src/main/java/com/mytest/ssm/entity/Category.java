package com.mytest.ssm.entity;

import java.util.Arrays;
import java.util.HashSet;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.ToString;


@TableName(value = "t_category")
@ToString
public class Category {
	public Category(Integer id, String name, String descs, Integer pid, String cls, Integer level, Category parentCategory, HashSet<Category> childsSet) {
		this.id = id;
		this.name = name;
		this.descs = descs;
		this.pid = pid;
		this.cls = cls;
		this.level = level;
		this.parentCategory = parentCategory;
		this.childsSet = childsSet;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescs() {
		return descs;
	}

	public void setDescs(String descs) {
		this.descs = descs;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getCls() {
		return cls;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Category getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}

	public HashSet<Category> getChildsSet() {
		return childsSet;
	}

	public void setChildsSet(HashSet<Category> childsSet) {
		this.childsSet = childsSet;
	}

	/**
	 * 分类id
	 */
	@TableId(value = "C_ID",type = IdType.AUTO)
	private Integer id;
	/**
	 * 分类名称
	 */
	@TableField(value = "C_NAME")
	private String name;
	/**
	 * 描述
	 */ 
	@TableField(value = "C_DESC")
	private String descs;
	/**
	 * 父分类 
	 */
	@TableField(value = "C_PID")
	private Integer pid;
	
	/**
	 * cls
	 * 当前分类上面的所有父分类组成的id序列
	 * 形如|5|,|2|,|1|
	 * 表示当前分类的直接父分类为5,5的父分类为2，2的父分类为1
	 */
	@TableField(value = "C_CLS")
	private String cls;
	/**
	 * 分类级别
	 */
	@TableField(value = "C_LEVEL")
	private Integer level;
	
	@TableField(exist = false)
	private Category parentCategory; 
	
	@TableField(exist = false)
	private HashSet<Category> childsSet;
	
	public Category() {
		super();
	}
	public Category(Integer id) {
		super();
		this.id = id;
	}

}
