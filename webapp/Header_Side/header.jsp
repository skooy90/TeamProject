<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx}/Header_Side/style.css">


    <header class="main-header">
        <a class="logo" href="${ctx}/">
            <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS95olptlO1wIvD1sFAfAR8QrsMzAEswISagw&s" alt="SoAPI Logo">
            <span class="logo-text">SoAPI</span>
        </a>
        <c:if test="${not empty sessionScope.loginUser}">
            <div class="user-info">
                <span><i class="fas fa-user-circle"></i> 안녕하세요, ${sessionScope.empName}님</span>
                <a href="${pageContext.request.contextPath}/mypage">
                    <i class="fas fa-user-cog"></i> 마이페이지
                </a>
                
                <a href="${pageContext.request.contextPath}/logout">
                    <i class="fas fa-sign-out-alt"></i> 로그아웃
                </a>
            </div>
        </c:if>
    </header>
