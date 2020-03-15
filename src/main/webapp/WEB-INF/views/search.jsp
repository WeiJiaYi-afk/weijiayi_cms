<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${channel!=null?channel.name:'首页' }</title>
<link href="/public/css/bootstrap.min.css" rel="stylesheet">
<link href="/public/css/index.css" rel="stylesheet">
</head>
<body>
	<!-- 头部导航 -->
	<jsp:include page="./common/user/head-top.jsp"></jsp:include>

	<div class="container-fluid" style="margin-top: 6px;">
		<div class="row offset-1">
			<!-- 频道 -->
			<div class="col-1" style="padding-right: 5px;padding-left: 5px;">
				

			</div>
			
			<div class="col-6">
				<!--  搜索框 -->
				<div class="card"
					style="width: 18rem; margin-bottom: 5px; border: 0px;margin-top: 20px;margin-bottom: 20px;">
					<div class="form-inline">
						<input type="text" placeholder="请输入要搜索的内容" id="keyword" value="${keyword }" class="form-control"	style="width: 14rem; margin-right: 5px">
						<button class="btn btn-info" type="button" onclick="search();">搜索</button>
					</div>
		
				</div>
				<!-- 文章列表 -->
				<div style="margin-top: 18px;">
					<c:forEach items="${pageInfo.list }" var="item" varStatus="status">
					  	<div class="media">
					  		<a href="/article/detail/${item.id}.html" target="_blank">
						  		<img src="${item.picture }" class="mr-3" style="height: 108px; width: 175px;">
						  	</a>
						  <div class="media-body">
						    <h4 class="mt-1">
						    	<a href="/article/detail/${item.id}.html" target="_blank">${item.title }</a>
						    </h4>
						    <fmt:formatDate value="${item.created }" pattern="yyyy-MM-dd HH:mm:ss"/></p>
						  </div>
						</div>
					</c:forEach>
				</div>
				<%-- ${pageInfo } --%>
			</div>
			<!-- 首页右侧 -->
			<div class="col-3">
				
			</div>

		</div>
		<!-- 分页 -->
				<nav class="offset-2">
				<ul class="pagination">
					<c:if test="${pageInfo.hasPreviousPage }">
						<li class="page-item"><a class="page-link" href="javascript:gotoPage('${pageInfo.pageNum-1 }');">上一页</a></li>
					</c:if>
					
					<c:forEach items="${pageInfo.navigatepageNums }" var="item">
						<c:if test="${item>pageInfo.pageNum-5 &&  item<pageInfo.pageNum+5}">
							<c:if test="${pageInfo.pageNum==item }">
								<li class="page-item active"><a class="page-link" href="javascript:gotoPage('${item}');">${item }</a></li>
							</c:if>
							<c:if test="${pageInfo.pageNum!=item }">
								<li class="page-item"><a class="page-link" href="javascript:gotoPage('${item}');">${item }</a></li>
							</c:if>
						</c:if>
					</c:forEach>
					
					<c:if test="${pageInfo.hasNextPage }">
						<li class="page-item"><a class="page-link" href="javascript:gotoPage('${pageInfo.pageNum+1 }');">下一页</a></li>
					</c:if>
				</ul>
			</nav>
		<div class="col-10 offset-1 breadcrumb" style="margin-bottom: 200px;">
			友情链接：
			<c:forEach items="${linkList }" var="item">
				<div style="margin-left: 20px;">
					<a href="${item.url }" target="_blank">${item.text }</a>
				</div>
			</c:forEach>
		</div>
	</div>
	<script src="/public/js/jquery.min.1.12.4.js"></script>
	<script src="/public/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		var channelId = '${channelId}';
		var cateId = '${cateId}';
		function gotoPage(pageNum){
			window.location.href="/search?keyword="+$("#keyword").val()+"&pageNum="+pageNum;			
		}
		function search() {
			window.location.href = "/search?keyword="+$("#keyword").val();
		}
	</script>
</body>
</html>