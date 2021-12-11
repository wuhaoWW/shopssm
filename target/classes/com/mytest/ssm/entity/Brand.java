package com.mytest.ssm.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName(value = "T_BRAND")
public class Brand {
		@TableId(value = "B_ID",type = IdType.AUTO)
		private Integer id; 
		@TableField(value = "B_CNNAME")
		private String cnName;
		@TableField(value = "B_ENNAME")
		private String enName;
		@TableField(value = "B_BIGPHOTO")
		private String bigPhoto;//小图片
		@TableField(value = "B_SMALLPHOTO")
		private String smallPhoto;//大图片
		@TableField(value = "B_DESC")
		private String descs;
		
		public Brand() {
			super();
		}
		public Brand(Integer id) {
			super();
			this.id = id; 
		}
		public Brand(Integer brandId, String cnName, String enName) {
			this.id = brandId;
			this.cnName = cnName;
			this.enName = enName;
		}
		
		@Override
		public String toString() {
			return "Brand [id=" + id + ", cnName=" + cnName + ", enName=" + enName + ", bigPhoto=" + bigPhoto
					+ ", smallPhoto=" + smallPhoto + ", desc=" + descs + "]";
		}
		
}
