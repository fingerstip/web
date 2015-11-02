<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath }"></c:set>
<html>
	<head>
		<title>Welcome</title>
		<script type="text/javascript" src="${ctx }/resources/js/jquery-1.8.3.min.js"></script>
		<style type="text/css">
			table{border:0;margin:0;border-collapse:collapse;border-spacing:0;}
			tdead tr {
				background-color: green;
			}
			tbody tr td {
				text-align:center; 
			    line-height: 25px;  
			}
			.even {background-color: #FFF;}
			.odd {background-color: #E4E4E4;}
			.op {widtd: 20%;line-height: 25px;}
		</style>
	</head>
	<body>
		<h1>
			<c:if test="${empty account }">Add Account</c:if>
			<c:if test="${not empty account }">Update Account</c:if>
		</h1>
		
		<div>
			<c:if test="${result == true }">操作成功</c:if>
			<c:if test="${result == false }">操作失败</c:if>
		</div>
		
		<div>
			<div class="op">
				<a href="javascript:void();" onclick="window.history.back();">返回</a>
			</div>
			
			<form method="post" >
				<input type="hidden" name="id" value="${account.id }">
				<input type="hidden" name="account_number" value="${account.account_number }" />
				<table style="width: 20%;">
					<tr>
						<td>姓氏</td>
						<td><input type="text" name="firstname" value="${account.firstname }" /></td>
					</tr>
					<tr>
						<td>名字</td>
						<td><input type="text" name="lastname" value="${account.lastname }" /></td>
					</tr>
					<tr>
						<td>年龄</td>
						<td><input type="text" name="age" value="${account.age }" /></td>
					</tr>
					<tr>
						<td>地址</td>
						<td><input type="text" name="address" value="${account.address }" /></td>
					</tr>
					<tr>
						<td>城市</td>
						<td><input type="text" name="city" value="${account.city }" /></td>
					</tr>
					<tr>
						<td>州</td>
						<td><input type="text" name="state" value="${account.state }" /></td>
					</tr>
					<tr>
						<td>性别</td>
						<td><input type="text" name="gender" value="${account.gender }" /></td>
					</tr>
					<tr>
						<td>邮箱</td>
						<td><input type="text" name="email" value="${account.email }" /></td>
					</tr>
					<tr>
						<td>余额</td>
						<td><input type="text" name="balance" value="${account.balance }" /></td>
					</tr>
					<tr>
						<td>雇主</td>
						<td><input type="text" name="employer" value="${account.employer }" /></td>
					</tr>
					
					<tr>
						<td colspan="2">
							<button value="提交">提交</button>
						</td>
					</tr>
				</table>
			</form>
			
		</div>
		
		<script type="text/javascript">
			$(function(){
				$("button").click(function(){
					var flag = true;
					$("table tr input").each(function(){
						var _obj = $(this);
						
						if (_obj.val() == ''){
							var text = _obj.parent().prev().text();
							alert("请录入 " + text);
							flag = false;
							return false;//break
						}
					});
					
					if (!flag){
						return false;//防止提交
					}
					
					$("form").attr("action","${ctx }/es/save");
					$("form").submit();
				});
			});
		</script>
	</body>
</html>