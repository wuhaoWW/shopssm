<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>

	<head>
		<meta charset="utf-8"> 
		<title>品牌添加</title> 

		<style>
			@import url("/shopssm/resource/css/Brand_add.css");
		</style>

	</head>

	<body>
	
		<div id="title">
			<div class="title">品牌添加</div>
		</div> 
		
		<div class="position"> 当前位置：品牌管理 >> 品牌添加 </div>
		<div id="content">  
			<form id="BrandAddform" enctype="multipart/form-data"
			       name="BrandAddform" method="post" action="/shopssm/back/brandBrandAdd">
				<table align="center" id="add_table">
						<caption id="cap">品牌添加</caption>
						<tbody>
							<tr>
								<td> 中文名称:</td>
								<td>
									<input name="cnName" type="text">
									<span>*</span>
									<div id="cnName_error">${errors.cnName}</div>
								</td>
	
							</tr>
							<tr>
								<td> 英文名称:</td> 
								<td>
									<input name="enName" type="text">
									<span>*</span>
									<div id="enName_error">${errors.enName}</div>
								</td>
	
							</tr> 
							<tr>
								<td> 小图片:</td>
								<td> 
									<input name="smallPhoto" type="file">
									
								<div id="smallPhoto_error">${errors.smallPhoto}</div>
								</td>
							</tr>
							<tr>
								<td> 大图片:</td>
								<td>
									<input name="bigPhoto" type="file">
									<div id="bigPhoto_error">${errors.bigPhoto}</div>
								</td>
								
	
							</tr>
							<tr>
								<td> 描述:</td>
								<td>
									<textarea  class="ckeditor" name="descs" 
															cols="50" rows="10"></textarea>
								</td>
	
							</tr>
	
							<tr>
								<td colspan="2" align="center">
									<input type="submit" value="添加"/> 
									<input type="reset" value="重置"/>
								</td>
							</tr>
						</tbody>
					</table>
			</form>
				
		</div>

	</body>
			<script type="text/javascript"  src="/shopssm/resource/ckeditor/ckeditor.js" ></script>
</html>