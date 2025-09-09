<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>재고 상세 - 자재관리</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/src/Header_Sied/style.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/src/css/material_detail.css">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <jsp:include page="/src/Header_Sied/header.jsp" />
    <div class="main-container">
        <jsp:include page="/src/Header_Sied/sidebar.jsp" />
        <div class="content-area">
            <div class="container">
        <!-- 헤더 섹션 -->
        <div class="detail-header">
            <div>
                <h1 class="detail-title">${material.materialCode} - ${material.stName}</h1>
                <span class="status-badge ${material.maQuantity <= 50 ? 'badge-low' : material.maQuantity >= 1000 ? 'badge-high' : 'badge-normal'}">
                    ${material.maQuantity <= 50 ? '재고 부족' : material.maQuantity >= 1000 ? '재고 과다' : '재고 정상'}
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
                    <span class="info-value">${material.stType}</span>
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

        <!-- 관련 제품 비교 그래프 -->
        <div class="chart-section">
            <h2 class="chart-title">같은 제품의 다른 재고 비교</h2>
            <div class="chart-container">
                <canvas id="comparisonChart"></canvas>
            </div>
        </div>

        <!-- 재고 이력 테이블 -->
        <div class="history-section">
            <h2 class="history-title">재고 이력</h2>
            <table class="history-table">
                <thead>
                    <tr>
                        <th>날짜</th>
                        <th>구분</th>
                        <th>수량</th>
                        <th>담당자</th>
                        <th>비고</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td colspan="5" style="text-align: center; padding: 20px; color: #6c757d;">
                            재고 이력이 없습니다.
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <!-- 통계 정보 카드 -->
        <div class="stats-section">
            <h2 class="stats-title">재고 통계</h2>
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-number">${relatedMaterials != null ? relatedMaterials.size() : 0}</div>
                    <div class="stat-label">같은 제품 재고</div>
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
    </div>
</body>
</html>
