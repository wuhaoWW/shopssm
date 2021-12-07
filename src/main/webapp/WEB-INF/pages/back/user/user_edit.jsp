<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

	<head>
		<meta charset="utf-8">
		<title>用户修改</title>
		
		<style>
				@import url("/shopssm/resource/css/Brand_add.css");
		</style>
	</head>  

	<body>
		<div id="title">
				<div class="title">用户修改</div>
		</div> 
		
		<div class="position"> 当前位置：用户管理 >> 用户修改 </div>
		<div id="content"> 
		<form  method="post" action="/shopssm/back/user/edit.do">
		<table align="center" id="add_table" >
			<tbody>
				<input type="hidden" name="userId" value="${user.id }"/>
					<caption id="cap">用户修改</caption>
						<tr>
							<td width="15%"> 用户名：</td>
							<td>
								<input name="username" type="text" value="${user.username }"><span>*</span>
								<div id="username_error">${errors['username']}</div>
								</td>
						</tr>
						<tr>
							<td width="15%"> 手机号：</td>
							<td>
								<input name="mobile" type="text" value="${user.mobile }">
								<div id="mobile_error">${errors['mobile']}</div>
							</td>
						</tr> 
						<tr>  
							<td width="15%"> Email：</td>
							<td>
								<input name="email" type="text" value="${user.email }">
								<div id="email_error">${errors['email']}</div>
							</td>
						</tr>

						<tr>
							<td colspan="2" align="center">
								<input type="submit" value="修改"/>
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>

	</body>
	<script type="text/javascript"  
			src="/shopssm/resource/ckeditor/ckeditor.js" ></script>
</html>