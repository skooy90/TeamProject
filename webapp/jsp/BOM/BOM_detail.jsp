<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BOM 상세 - MES 시스템</title>
        <c:set var="ctx" value="${pageContext.request.contextPath}" />
    
  <link rel="stylesheet" href="${ctx}/Header_Side/style.css">
  <link rel="stylesheet" href="${ctx}/css/bom_detail.css">
</head>
<body>
    <jsp:include page="../../Header_Side/header.jsp" />
    
    <div class="main-container">
        <jsp:include page="../../Header_Side/sidebar.jsp" />
        
        <div class="content-area">
            <div class="container">
                <!-- 페이지 헤더 -->
                <div class="page-header">
                    <div class="header-info">
                        <h1 class="page-title">${bom.bomDescription}</h1>
                        <div class="product-info">
                            <span>제품코드: ${bom.standardCode}</span>
                            <span class="routing-type">${bom.stType == 'SEMI' ? '반제품 라우팅' : '완제품 라우팅'}</span>
                            <span>총 공정 단계: ${bomSteps.size()}단계</span>
                        </div>
                    </div>
                    <div class="action-buttons">
                        <a href="${pageContext.request.contextPath}/bom/form?code=${bom.standardCode}" class="btn btn-warning">라우팅 수정</a>
                        <a href="${pageContext.request.contextPath}/bom/delete?code=${bom.standardCode}" class="btn btn-danger" onclick="return confirm('정말 삭제하시겠습니까?')">삭제</a>
                        <a href="${pageContext.request.contextPath}/bom" class="btn btn-secondary">목록으로</a>
                    </div>
                </div>

                <!-- 기본 정보 카드 -->
                <div class="cards-container">
                    <div class="card">
                        <h3 class="card-title">라우팅 기본 정보</h3>
                        <div class="info-row">
                            <span class="info-label">제품코드:</span>
                            <span class="info-value">${bom.standardCode}</span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">제품명:</span>
                            <span class="info-value">${bom.stName}</span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">라우팅 타입:</span>
                            <span class="info-value">${bom.stType == 'SEMI' ? '반제품' : '완제품'}</span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">총 공정 단계:</span>
                            <span class="info-value">${bomSteps.size()}단계</span>
                        </div>
                    </div>

                    <div class="card">
                        <h3 class="card-title">등록 정보</h3>
                        <div class="info-row">
                            <span class="info-label">등록일:</span>
                            <span class="info-value"><fmt:formatDate value="${bom.createDate}" pattern="yyyy-MM-dd"/></span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">수정일:</span>
                            <span class="info-value"><fmt:formatDate value="${bom.updateDate}" pattern="yyyy-MM-dd"/></span>
                        </div>
                    </div>

                </div>

                <!-- 공정별 자재 사용량 -->
                <div class="bom-section">
                    <h2 class="section-title">공정별 자재 사용량</h2>
                    
                    <c:forEach var="step" items="${bomSteps}">
                        <!-- 자재가 있는 공정만 표시 -->
                        <c:if test="${(step.bomOrder == 1 && bom.stType == 'SEMI') || 
                                      (step.bomOrder == 2 && bom.stType == 'SEMI') ||
                                      (step.bomOrder == 1 && bom.stType == 'FINISH') ||
                                      (step.bomOrder == 2 && bom.stType == 'FINISH') ||
                                      (step.bomOrder == 5 && bom.stType == 'FINISH') ||
                                      (step.bomOrder == 6 && bom.stType == 'FINISH')}">
                            <div class="process-group ${step.bomOrder <= 4 ? 'process-semi' : 'process-finish'}">
                                <div class="process-header">
                                    <h3>${step.bomOrder}단계 - ${step.bomDescription}</h3>
                                </div>
                                <div class="materials-list">
                                        <c:choose>
                                        <c:when test="${step.bomOrder == 1 && bom.stType == 'SEMI'}">
                                            <div class="material-item">
                                                <span class="material-name">신선 오이</span>
                                                <span class="material-quantity">2kg</span>
                                            </div>
                                            <div class="material-item">
                                                <span class="material-name">가성소다</span>
                                                <span class="material-quantity">200g</span>
                                            </div>
                                            <div class="material-item">
                                                <span class="material-name">팜 오일</span>
                                                <span class="material-quantity">500ml</span>
                                            </div>
                                            <div class="material-item">
                                                <span class="material-name">올리브 오일</span>
                                                <span class="material-quantity">300ml</span>
                                            </div>
                                        </c:when>
                                        <c:when test="${step.bomOrder == 2 && bom.stType == 'SEMI'}">
                                            <div class="material-item">
                                                <span class="material-name">살리실산</span>
                                                <span class="material-quantity">50g</span>
                                            </div>
                                            <div class="material-item">
                                                <span class="material-name">과일산</span>
                                                <span class="material-quantity">40g</span>
                                            </div>
                                            <div class="material-item">
                                                <span class="material-name">아연 PCA</span>
                                                <span class="material-quantity">60g</span>
                                            </div>
                                            <div class="material-item">
                                                <span class="material-name">티트리 오일</span>
                                                <span class="material-quantity">30ml</span>
                                            </div>
                                        </c:when>
                                        <c:when test="${step.bomOrder == 1 && bom.stType == 'FINISH'}">
                                            <div class="material-item">
                                                <span class="material-name">신선 오이</span>
                                                <span class="material-quantity">2kg</span>
                                            </div>
                                            <div class="material-item">
                                                <span class="material-name">가성소다</span>
                                                <span class="material-quantity">200g</span>
                                            </div>
                                            <div class="material-item">
                                                <span class="material-name">팜 오일</span>
                                                <span class="material-quantity">500ml</span>
                                            </div>
                                            <div class="material-item">
                                                <span class="material-name">올리브 오일</span>
                                                <span class="material-quantity">300ml</span>
                                            </div>
                                        </c:when>
                                        <c:when test="${step.bomOrder == 2 && bom.stType == 'FINISH'}">
                                            <div class="material-item">
                                                <span class="material-name">살리실산</span>
                                                <span class="material-quantity">50g</span>
                                            </div>
                                            <div class="material-item">
                                                <span class="material-name">과일산</span>
                                                <span class="material-quantity">40g</span>
                                            </div>
                                            <div class="material-item">
                                                <span class="material-name">아연 PCA</span>
                                                <span class="material-quantity">60g</span>
                                            </div>
                                            <div class="material-item">
                                                <span class="material-name">티트리 오일</span>
                                                <span class="material-quantity">30ml</span>
                                            </div>
                                        </c:when>
                                        <c:when test="${step.bomOrder == 5 && bom.stType == 'FINISH'}">
                                            <div class="material-item">
                                                <span class="material-name">비누 베이스</span>
                                                <span class="material-quantity">2kg</span>
                                            </div>
                                            <div class="material-item">
                                                <span class="material-name">오이 추출물</span>
                                                <span class="material-quantity">100ml</span>
                                            </div>
                                            <c:if test="${bom.stName == '오이 각질제거비누'}">
                                                <div class="material-item">
                                                    <span class="material-name">각질제거 성분</span>
                                                    <span class="material-quantity">150g</span>
                                                </div>
                                            </c:if>
                                            <c:if test="${bom.stName == '오이 수축비누'}">
                                                <div class="material-item">
                                                    <span class="material-name">수축 성분</span>
                                                    <span class="material-quantity">125g</span>
                                                </div>
                                            </c:if>
                                            </c:when>
                                        <c:when test="${step.bomOrder == 6 && bom.stType == 'FINISH'}">
                                            <div class="material-item">
                                                <span class="material-name">올리브 오일</span>
                                                <span class="material-quantity">200ml</span>
                    </div>
                                            <div class="material-item">
                                                <span class="material-name">티트리 오일</span>
                                                <span class="material-quantity">20ml</span>
                </div>
                                            <c:if test="${bom.stName == '오이 각질제거비누'}">
                                                <div class="material-item">
                                                    <span class="material-name">살리실산</span>
                                                    <span class="material-quantity">25g</span>
                        </div>
                                            </c:if>
                                            <c:if test="${bom.stName == '오이 수축비누'}">
                                                <div class="material-item">
                                                    <span class="material-name">과일산</span>
                                                    <span class="material-quantity">20g</span>
                    </div>
                                            </c:if>
                                            </c:when>
                                        </c:choose>
                    </div>
                            </div>
                        </c:if>
                                </c:forEach>
                </div>

            </div>
        </div>
    </div>
</body>
</html>
