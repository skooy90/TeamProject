<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx}/Header_Side/style.css">


<header class="main-header">
	<a class="logo" href="${ctx}/"> <img src="${ctx}/img/logo.png"
		alt="SoAPI Logo">
	</a>
	<c:if test="${not empty sessionScope.loginUser}">
  안녕하세요, ${sessionScope.empName}님
  <a href="${pageContext.request.contextPath}/logout"
			style="margin-left: 8px;">로그아웃</a>
	</c:if>
</header>
