<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>공정 상세 - ${process.stName} - MES 시스템</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/src/Header_Sied/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/src/css/process_detail.css">
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
                    <div class="header-info">
                        <h1>${process.stName}</h1>
                        <span class="product-type">${process.stType == 'RAW' ? '원자재' : process.stType == 'SEMI' ? '반제품' : '완제품'}</span>
                    </div>
                    <div class="header-actions">
                        <a href="${pageContext.request.contextPath}/process/form?code=${process.processNo}" class="btn btn-primary">공정 수정</a>
                        <a href="${pageContext.request.contextPath}/process" class="btn btn-secondary">목록으로</a>
                    </div>
                </div>

                <!-- 그리드 레이아웃 -->
                <div class="grid-container">
                    <!-- 기본 정보 카드 -->
                    <div class="info-card">
                        <h3>공정 기본 정보</h3>
                        <div class="info-item">
                            <span class="info-label">공정번호</span>
                            <span class="info-value">${process.processNo}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">제품코드</span>
                            <span class="info-value">${process.standardCode}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">공정 설명</span>
                            <span class="info-value">${process.prDescription}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">공정 유형</span>
                            <span class="info-value">${process.prType}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">공정 순서</span>
                            <span class="info-value">${process.prOrder}단계</span>
                        </div>
                    </div>

                    <!-- 공정 순서도 -->
                    <div class="process-flow">
                        <h3>공정 순서도</h3>
                        <div class="flow-chart">
                            <div class="flow-step ${process.stType == 'SEMI' ? 'semi' : 'finish'}">
                                <div class="step-number">${process.prOrder}</div>
                                <div class="step-title">${process.prDescription}</div>
                                <div class="step-type">${process.prType}</div>
                            </div>
                        </div>
                        <p style="margin: 0; color: #666; font-size: 0.9em;">
                            <strong>${process.stType == 'SEMI' ? '반제품' : '완제품'} 과정:</strong> 
                            ${process.stType == 'SEMI' ? '원자재를 혼합하여 오이 비누 베이스를 제조합니다.' : '베이스에 특별한 성분을 추가하여 최종 제품을 완성합니다.'}
                        </p>
                    </div>
                </div>

                <!-- 작업 지침서 섹션 -->
                <div class="work-guide-section">
                    <h3>작업 지침서</h3>
                    
                        <div class="process-guide">
                            <div class="guide-header">
                            <div class="guide-title">${process.prDescription}</div>
                            <div class="guide-order">${process.prOrder}단계</div>
                            </div>
                            <div class="guide-content">
                                <div class="guide-method">
                                    <h4>작업 방법</h4>
                                <p>공정 상세 작업 방법은 관리자에게 문의하세요.</p>
                                </div>
                                <div class="guide-materials">
                                    <h4>필요 자재</h4>
                                <p>필요한 자재 목록은 관리자에게 문의하세요.</p>
                                </div>
                            </div>
                        </div>
                </div>

                <!-- 통계 정보 -->
                <div class="stats-section">
                    <div class="stat-card">
                        <h4>공정 유형</h4>
                        <p class="number">${process.prType}</p>
                    </div>
                    <div class="stat-card">
                        <h4>공정 순서</h4>
                        <p class="number">${process.prOrder}</p>
                    </div>
                    <div class="stat-card">
                        <h4>제품 유형</h4>
                        <p class="number">${process.stType == 'RAW' ? '원자재' : process.stType == 'SEMI' ? '반제품' : '완제품'}</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>

