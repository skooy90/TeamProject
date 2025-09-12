<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>재고 상세 - 자재관리</title>
    <c:set var="ctx" value="${pageContext.request.contextPath}" />
    
  <link rel="stylesheet" href="${ctx}/css/material_detail.css">
  <link rel="stylesheet" href="${ctx}/Header_Side/style.css">
  
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <jsp:include page="../../Header_Side/header.jsp" />
    <div class="main-container">
        <jsp:include page="../../Header_Side/sidebar.jsp" />
        <div class="content-area">
            <div class="container">
        <!-- 헤더 섹션 -->
        <div class="detail-header">
            <div>
                <h1 class="detail-title">${material.materialCode} - ${material.stName}</h1>
                <span class="status-badge ${material.maQuantity <= 50 ? 'badge-low' : 'badge-normal'}">
                    ${material.maQuantity <= 50 ? '재고 부족' : '재고 정상'}
                </span>
            </div>
            <div class="action-buttons">
                <a href="${pageContext.request.contextPath}/material/form?code=${material.materialCode}" class="btn btn-primary">수정</a>
                <a href="#" class="btn btn-danger" onclick="deleteMaterial('${material.materialCode}')">삭제</a>
                <a href="${pageContext.request.contextPath}/material" class="btn btn-secondary">목록으로</a>
            </div>
        </div>

        <!-- 기본 정보 카드 -->
        <div class="info-card">
            <h2 style="margin-top: 0; color: #2c3e50;">기본 정보</h2>
            <div class="info-grid">
                <div class="info-item">
                    <span class="info-label">재고 코드</span>
                    <span class="info-value">${material.materialCode}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">제품 코드</span>
                    <span class="info-value">${material.standardCode}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">제품명</span>
                    <span class="info-value">${material.stName}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">제품 유형</span>
                    <span class="info-value">
                        <c:choose>
                            <c:when test="${material.stType == 'RAW'}">원자재</c:when>
                            <c:when test="${material.stType == 'SEMI'}">반제품</c:when>
                            <c:when test="${material.stType == 'FINISH'}">완제품</c:when>
                            <c:otherwise>${material.stType}</c:otherwise>
                        </c:choose>
                    </span>
                </div>
                <div class="info-item">
                    <span class="info-label">재고량</span>
                    <span class="info-value">${material.maQuantity} kg</span>
                </div>
                <div class="info-item">
                    <span class="info-label">단위</span>
                    <span class="info-value">kg</span>
                </div>
                <div class="info-item">
                    <span class="info-label">담당자</span>
                    <span class="info-value">${material.usName}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">등록일</span>
                    <span class="info-value"><fmt:formatDate value="${material.maCreationDate}" pattern="yyyy-MM-dd"/></span>
                </div>
                <div class="info-item">
                    <span class="info-label">수정일</span>
                    <span class="info-value"><fmt:formatDate value="${material.maUpdateDate}" pattern="yyyy-MM-dd"/></span>
                </div>
            </div>
        </div>

        <!-- 통계 정보 카드 -->
        <div class="stats-section">
            <h2 class="stats-title">재고 통계</h2>
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-number">${relatedMaterials != null ? relatedMaterials.size() : 0}</div>
                    <div class="stat-label">전체 제품 재고</div>
					<!-- 기준코드가 같은 제품의 총 합 -->
                </div>
                <div class="stat-card">
                    <div class="stat-number">${material.maQuantity}</div>
                    <div class="stat-label">현재 재고량</div>
                </div>
                <div class="stat-card">
                    <div class="stat-number">${material.stType}</div>
                    <div class="stat-label">제품 유형</div>
                </div>
            </div>
        </div>
    </div>

    <script>
        // JSP에서 JavaScript로 전달할 변수들
        window.contextPath = '${pageContext.request.contextPath}';
    </script>
    <script src="${pageContext.request.contextPath}/src/js/material_detail.js"></script>
            </div>
        </div>
 
</body>
</html>
