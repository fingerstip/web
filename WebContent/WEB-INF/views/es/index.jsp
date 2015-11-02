<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath }" />
<html>
	<head>
		<title>Welcome</title>
		<script type="text/javascript" src="${ctx }/resources/js/jquery-1.8.3.min.js"></script>
		<style type="text/css">
			table{border:0;margin:0;border-collapse:collapse;border-spacing:0;}
			thead tr {
				background-color: green;
			}
			tbody tr td {
				text-align:center; 
			    line-height: 25px;
			    cursor: pointer;  
			}
			.even {background-color: #FFF;}
			.odd {background-color: #E4E4E4;}
			.op {text-align: right;width: 98%;line-height: 25px;}
		</style>
	</head>
	<body>
		<h1>Account List</h1>
		<div>
			<form action="${ctx}/es/search" id="searchForm" method="post">
				地址 : <input type="text" name="address" id="address" />
				姓名 : <input type="text" name="firstname" id="firstname" />
				年龄 : <input type="text" name="gteAge" id="gteAge" /> ~ <input  name="lteAge"  id="lteAge" />
				<input type="submit" id="searchBtn" value="搜索" />
			</form>
		</div>		
		<div class="op">
			<a href="${ctx }/es/add">新增</a>
			<a id="update" href="javascript:void(0);">修改</a>
			<a id="delete" href="javascript:void(0);">删除</a>
		</div>
		<form id="deleteForm" method="post">
			<table style="width: 98%">
				<thead>
					<tr>
						<th><input type="checkbox" id="checkAll" /></th>
						<th>员工号</th>
						<th>姓名</th>
						<th>年龄</th>
						<th>地址</th>
						<th>性别</th>
						<th>邮箱</th>
						<th>余额</th>
						<th>雇主</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="account" items="${page.result }">
							<tr>
								<th><input type="checkbox" id="single" name="ids[]" value="${account.id }" ></th>
								<td>${account.account_number }</td>
								<td>${account.firstname } ${account.lastname }</td>
								<td>${account.age }</td>
								<td>${account.address } ${account.city } ${account.state }</td>
								<td>${account.gender }</td>
								<td>${account.email }</td>
								<td>${account.balance }</td>
								<td>${account.employer }</td>
							</tr>
							
					</c:forEach>
				</tbody>
			</table>
			<hr/>
			<div id="page">
				<a href="${ctx }/es?pageNo=1&pageSize=${page.pageSize}">首页</a>&nbsp;
				<c:if test="${page.hasPrevPage }">
					<a href="${ctx }/es?pageNo=${page.prevPage }&pageSize=${page.pageSize}">上一页</a>&nbsp;
				</c:if>
				<c:if test="${page.hasNextPage }">
					<a href="${ctx }/es?pageNo=${page.nextPage}&pageSize=${page.pageSize}">下一页</a>&nbsp;
				</c:if>
				<a href="${ctx }/es?pageNo=${page.totalPage}&pageSize=${page.pageSize}">尾页</a>&nbsp;
				<input type="text" id="pageNo" name="pageNo" size="2" value="${page.currentPage }"  />
				<input type="button" id="go" value="跳页"  />
				当前页 ： ${page.currentPage }，共  ${page.totalCount } 条数据，共 ${page.totalPage } 页，
				
				每页  
				<select id="pageSize" name="pageSize">
					<option <c:if test="${page.pageSize eq 20 }">selected='selected'</c:if> value="20">20</option>
					<option <c:if test="${page.pageSize eq 50 }">selected='selected'</c:if> value="50">50</option>
					<option <c:if test="${page.pageSize eq 100 }">selected='selected'</c:if> value="100">100</option>
				</select> 条数据
			</div>
		</form>
		
		<script type="text/javascript">
			
			/* $(document).ready(function(){
				if (getQueryString("result") == 'true') {
					alert("操作成功");
				}
				function getQueryString(name) {
					var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
					var r = window.location.search.substr(1).match(reg);
					if (r != null) return unescape(r[2]); return null;
				} 
			}); */
		
			$(function(){
				//Table 斑马线效果
				$("tbody tr:even").addClass("even");//偶数行
				$("tbody tr:odd").addClass("odd");//基数行
				
				$("tbody tr td").click(function(){
					var _parent = $(this).parent();
					
					var chx = _parent.find("input[id='single']");
					var length = _parent.find("input[id='single']:checked").length;
					//1 表示已经选择
					if (length == 1) {
						chx.attr("checked",false);
					}else{
						chx.attr("checked",true);
					}
					
				});
				
				$("#checkAll").click(function(){
					var checked = $(this).attr("checked");
					
					if (checked == undefined) {
						$("[name='ids[]']").attr("checked",false);
					}else{
						$("[name='ids[]']").attr("checked",true);
					}
					
				});
				
				$("#update").click(function(){
					var checked = $("input[id='single']:checked");
					if (checked.length != 1 || checked.val() == undefined) {
						alert("请最多选取一行进行修改！");
						return false;
					}
					$(this).attr("href","${ctx}/es/update/"+checked.val());
				});
				
				$("#delete").click(function(){
					var checked = $("input[id='single']:checked");
					if (checked.length <= 0) {
						alert("请选取一行进行修改！");
						return false;
					}
					
					$("#deleteForm").attr("action" , "${ctx}/es/delete");
					$("#deleteForm").submit();
				});
				
				$("#go").click(function(){
					var pageNo = $.trim($("#pageNo").val());
					if (!pageNo) {
						alert("请输入跳转页码");
						return;
					}
					window.location.href = "${ctx}/es?pageNo=" + pageNo + "&pageSize=" + $("#pageSize").val();
				});
				
				$("#pageSize").change(function(){
					var _this = $(this);
					window.location.href = "${ctx}/es?pageSize=" + _this.val();
				});
				
			});
		</script>
	</body>

</html>