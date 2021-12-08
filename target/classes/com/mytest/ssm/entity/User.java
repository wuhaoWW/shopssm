package com.mytest.ssm.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

@TableName(value="T_user")
@Data
public class User {
		//主键
		@TableId(value = "u_id")
		private Integer id;
		//名字
		@TableId(value = "U_USERNAME")
		private String username; 
		//密码
		@TableId(value = "U_PWD")
		private String password;
		//手机号码
		@TableId(value = "U_MOBILE")
		private String mobile;
		//邮箱地址
		@TableId(value = "U_EMAIL")
		private String email;
	
		@Override
		public String toString() {
			return "User [id=" + id + ", username=" + username + ", password=" + password + ", mobile=" + mobile + ", email="
					+ email + "]";
		}
}
