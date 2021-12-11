package com.mytest.ssm.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName(value = "t_admin")
public class Admin {
		//主键
		@TableId(value = "A_ID",type = IdType.AUTO)
		private Integer id;
		//名字
		@TableField(value = "A_ADMINNAME")
		private String adminName; 
		//密码
		@TableField(value = "A_PASSWORD")
		private String password;
		//性别
		@TableField(value = "A_GENDER")
		private String sex;
		//描述
		@TableField(value = "A_DESC")
		private String descs;
		//真实姓名
		@TableField(value = "A_REALNAME")
		private String realName;
		
		
		@Override
		public String toString() {
			return "Admin [id=" + id + ", adminName=" + adminName + ", password=" + password + ", sex=" + sex + ", desc=" + descs + ", realName=" + realName + "]";
		}
		
}
 