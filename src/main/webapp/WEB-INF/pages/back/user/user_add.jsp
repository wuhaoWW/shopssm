<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html> 

	<head>
		<meta charset="utf-8"> 
		<title>管理员添加</title>
		<style>
			@import url("/shopssm//resource/css/Brand_add.css");
		</style>
	</head>

	<body>
		<div id="title">
			<div class="title">添加用户</div> 
		</div>  
		
		<div class="position"> 当前位置：用户管理 >> 添加用户 </div>
		
		<div id="content">
			<form id="UserAddform" name="UserAddform"
					 method="post" action="/shopssm/back/user/add.action">
				<table align="center" id="add_table">
					<caption id="cap">
						用户添加
					</caption>
					<thead>
					</thead>
					<tbody>
						<tr>
							<td width="15%"> 用户名：</td>
							<td>
								<input name="username" type="text"><span>*</span>
								<div id="username_error">${errors['username']}</div>
								</td>
						</tr>
						<tr>
							<td width="15%"> 密码：</td>
							<td>
								<input name="pwd" type="password"><span>*</span>
								<div id="pwd_error">${errors['pwd']}</div></td>
								
						</tr>
						<tr>
							<td width="15%"> 确认密码：</td> 
							<td>
								<input name="pwd2" type="password"><span>*</span>
								<div id="pwd2_error">${errors['pwd2']}</div></td>
								
						</tr>
						<tr>
							<td width="15%"> 手机号：</td>
							<td>
								<input name="mobile" type="text"><span>*</span>
								<div id="mobile_error">${errors['mobile']}</div>
							</td>
						</tr> 
						<tr>  
							<td width="15%"> Email：</td>
							<td>
								<input name="email" type="text"><span>*</span>
								<div id="email_error">${errors['email']}</div>
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