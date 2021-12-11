package com.mytest.ssm.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
@Data
@TableName(value="T_user")
public class User {
		//主键
		@TableId(value = "u_id",type = IdType.AUTO)
		private Integer id;
		//名字
		@TableField(value = "U_USERNAME")
		private String username; 
		//密码
		@TableField(value = "U_PWD")
		private String password;
		//手机号码
		@TableField(value = "U_MOBILE")
		private String mobile;
		//邮箱地址
		@TableField(value = "U_EMAIL")
		private String email;
	
		@Override
		public String toString() {
			return "User [id=" + id + ", username=" + username + ", password=" + password + ", mobile=" + mobile + ", email="
					+ email + "]";
		}
}
