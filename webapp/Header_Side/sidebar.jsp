<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx}/Header_Side/style.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
 <div class="main-container">
        <nav class="sidebar">
            <ul>
                <li><a href="${ctx}/dashboard" class="active"><i class="fas fa-chart-line"></i> 대시보드</a></li>
                <li><a href="${ctx}/standardList"><i class="fas fa-ruler-combined"></i> 기준 관리</a></li>
                <li><a href="${ctx}/productionList"><i class="fas fa-cogs"></i> 생산 관리</a></li>
                <li><a href="${ctx}/workList"><i class="fas fa-tasks"></i> 작업 관리</a></li>
                <li><a href="${ctx}/qualityList"><i class="fas fa-check-circle"></i> 품질 관리</a></li>
                <li><a href="${ctx}/process"><i class="fas fa-project-diagram"></i> 공정 관리</a></li>
                <li><a href="${ctx}/bom"><i class="fas fa-clipboard-list"></i> BOM</a></li>
                <li><a href="${ctx}/material"><i class="fas fa-box"></i> 자재 관리</a></li>
                <li><a href="${ctx}/boardList"><i class="fas fa-chalkboard"></i> 게시판</a></li>
                <li><a href="${ctx}/mypage"><i class="fas fa-user-circle"></i> 마이페이지</a></li>
                <li class="admin-section">
      				<div class="admin-title">관리자</div>
      				<ul class="admin-menu">
                        <li><a href="${ctx}/admin/users"><i class="fas fa-users"></i> 사용자 관리</a></li>
                        <li><a href="${ctx}/admin/boards"><i class="fas fa-file-alt"></i> 게시판 관리</a></li>
      				</ul>
    					</li>
           	</ul>
   		</nav>
</div>