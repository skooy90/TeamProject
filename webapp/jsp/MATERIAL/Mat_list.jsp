<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>재고 목록 - 자재관리</title>
    <c:set var="ctx" value="${pageContext.request.contextPath}" />
    
  <link rel="stylesheet" href="${ctx}/Header_Side/style.css">
  <link rel="stylesheet" href="${ctx}/css/material_list.css">
</head>
<body>
    <jsp:include page="../../Header_Side/header.jsp" />
    <div class="main-container">
        <jsp:include page="../../Header_Side/sidebar.jsp" />
        <div class="content-area">
            <div class="container">
                <h1>재고 목록</h1>
                
                <!-- 통계 정보 -->
                <div class="stats">
                    <div class="stat-item">
                        <div class="stat-number">${totalCount != null ? totalCount : '0'}</div>
                        <div class="stat-label">전체 재고</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-number">${lowStockCount != null ? lowStockCount : '0'}</div>
                        <div class="stat-label">재고 부족</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-number">${rawMaterialCount != null ? rawMaterialCount : '0'}</div>
                        <div class="stat-label">원자재</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-number">${semiProductCount != null ? semiProductCount : '0'}</div>
                        <div class="stat-label">반제품</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-number">${finishedProductCount != null ? finishedProductCount : '0'}</div>
                        <div class="stat-label">완제품</div>
                    </div>
                </div>
                
                <!-- 탭 버튼 -->
                <div class="tab-buttons">
                    <a href="${pageContext.request.contextPath}/material?tab=lot" 
                       class="tab-btn ${activeTab == 'lot' ? 'active' : ''}">LOT별 재고</a>
                    <a href="${pageContext.request.contextPath}/material?tab=total" 
                       class="tab-btn ${activeTab == 'total' ? 'active' : ''}">총수량</a>
                </div>
                
                
                <!-- 검색 섹션 -->
                <div class="search-section">
                    <form action="${pageContext.request.contextPath}/material/search" method="get" class="search-form">
                        <div class="search-controls">
                            <input type="hidden" name="tab" value="${activeTab}">
                            <select name="searchType" class="search-select">
                                <option value="all" ${searchType == 'all' ? 'selected' : ''}>전체 검색</option>
                                <option value="code" ${searchType == 'code' ? 'selected' : ''}>재고코드</option>
                                <option value="name" ${searchType == 'name' ? 'selected' : ''}>제품명</option>
                            </select>
                            <input type="text" name="searchKeyword" value="${searchKeyword}" placeholder="검색어를 입력하세요" class="search-input">
                            <button type="submit" class="search-btn">검색</button>
                            <a href="${pageContext.request.contextPath}/material" class="reset-btn">초기화</a>
                        </div>
                    </form>
                </div>
                
                <!-- 필터 버튼 -->
                <div class="filter-section">
                    <div class="filter-buttons">
                        <a href="${pageContext.request.contextPath}/material/filter?type=all&tab=${activeTab}" 
                           class="filter-btn ${filterType == 'all' || filterType == null ? 'active' : ''}">전체</a>
                        <a href="${pageContext.request.contextPath}/material/filter?type=RAW&tab=${activeTab}" 
                           class="filter-btn ${filterType == 'RAW' ? 'active' : ''}">원자재</a>
                        <a href="${pageContext.request.contextPath}/material/filter?type=SEMI&tab=${activeTab}" 
                           class="filter-btn ${filterType == 'SEMI' ? 'active' : ''}">반제품</a>
                        <a href="${pageContext.request.contextPath}/material/filter?type=FINISH&tab=${activeTab}" 
                           class="filter-btn ${filterType == 'FINISH' ? 'active' : ''}">완제품</a>
                    </div>
                </div>
                
                <!-- 상단 컨트롤 -->
                <div class="controls">
                    <div class="control-left">
                        <div class="view-info">
                            <span class="view-label">현재 보기:</span>
                            <span class="view-value">
                                <c:choose>
                                    <c:when test="${activeTab == 'lot'}">LOT별 재고</c:when>
                                    <c:when test="${activeTab == 'total'}">총수량</c:when>
                                    <c:otherwise>총수량</c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                   
                    </div>
                    
                    <div class="control-right">
                        <a href="${pageContext.request.contextPath}/material/form" class="btn btn-success">재고 등록</a>
                    </div>
                </div>
                
                <!-- 검색 결과 정보 -->
                <c:if test="${not empty searchKeyword}">
                    <div class="search-result-info" style="background: #e3f2fd; padding: 10px; border-radius: 4px; margin-bottom: 20px; border-left: 4px solid #2196f3;">
                        <strong>검색 결과:</strong> 
                        "<span style="color: #1976d2;">${searchKeyword}</span>" 
                        (${searchType == 'all' ? '전체 검색' : searchType == 'code' ? '재고코드' : '제품명'}) 
                        - <strong>${activeTab == 'lot' ? (lotList != null ? lotList.size() : 0) : (totalList != null ? totalList.size() : 0)}개</strong> 
                        ${activeTab == 'lot' ? 'LOT' : '제품'} 발견
                    </div>
                </c:if>
                
                <!-- 필터 결과 정보 -->
                <c:if test="${not empty filterType && filterType != 'all'}">
                    <div class="filter-result-info" style="background: #fff3cd; padding: 10px; border-radius: 4px; margin-bottom: 20px; border-left: 4px solid #ffc107;">
                        <strong>필터 결과:</strong> 
                        <span style="color: #856404;">
                            <c:choose>
                                <c:when test="${filterType == 'RAW'}">원자재</c:when>
                                <c:when test="${filterType == 'SEMI'}">반제품</c:when>
                                <c:when test="${filterType == 'FINISH'}">완제품</c:when>
                                <c:otherwise>${filterType}</c:otherwise>
                            </c:choose>
                        </span>
                        
                    </div>
                </c:if>
                
                <!-- 성공/에러 메시지 -->
                <c:if test="${not empty success}">
                    <div style="background-color: #d4edda; color: #155724; padding: 10px; border-radius: 4px; margin-bottom: 20px;">
                        ${success}
                    </div>
                </c:if>
                <c:if test="${not empty error}">
                    <div style="background-color: #f8d7da; color: #721c24; padding: 10px; border-radius: 4px; margin-bottom: 20px;">
                        ${error}
                    </div>
                </c:if>
                
                <!-- LOT별 재고 탭 -->
                <c:if test="${activeTab == 'lot'}">
                    <div class="table-container">
                        <table class="material-table">
                            <thead>
                                <tr>
                                    <th>재고코드</th>
                                    <th>제품명</th>
                                    <th>제품유형</th>
                                    <th>수량</th>
                                    <th>단위</th>
                                    <th>담당자</th>
                                    <th>사번</th>
                                    <th>등록일</th>
                                    <th>관리</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty lotList}">
                                        <c:forEach var="material" items="${lotList}">
                                            <tr data-type="${material.stType}">
                                                <td class="code-cell">
                                                        ${material.materialCode}
                                                </td>
                                                <td class="name-cell">
                                                        ${material.stName}
                                                </td>
                                                <td class="type-cell">
                                                    <span class="type-badge type-${material.stType}">
                                                        <c:choose>
                                                            <c:when test="${material.stType == 'RAW'}">원자재</c:when>
                                                            <c:when test="${material.stType == 'SEMI'}">반제품</c:when>
                                                            <c:when test="${material.stType == 'FINISH'}">완제품</c:when>
                                                            <c:otherwise>${material.stType}</c:otherwise>
                                                        </c:choose>
                                                    </span>
                                                </td>
                                                <td class="quantity-cell">
                                                    <div class="quantity-info">
                                                        <span class="status-badge ${material.maQuantity <= 50 ? 'badge-low' : 'badge-normal'}">
                                                            ${material.maQuantity <= 50 ? '부족' : '정상'}
                                                        </span>
                                                        <span class="quantity-value">${material.maQuantity}</span>
                                                    </div>
                                                </td>
                                                <td class="unit-cell">${material.stUnit}</td>
                                                <td class="employee-cell">${material.usName}</td>
                                                <td class="empno-cell">${material.employeeNo}</td>
                                                <td class="date-cell">
                                                    <fmt:formatDate value="${material.maCreationDate}" pattern="yyyy-MM-dd"/>
                                                </td>
                                                <td class="action-cell">
                                                    <div class="action-buttons">
                                                        <a href="${pageContext.request.contextPath}/material/form?code=${material.materialCode}" class="btn-edit">수정</a>
                                                        <a href="#" class="btn-delete" onclick="deleteMaterial('${material.materialCode}')">삭제</a>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="9" class="empty-state">
                                                <div class="empty-content">
                                                    <div class="empty-icon">📦</div>
                                                    <div class="empty-message">등록된 LOT가 없습니다.</div>
                                                    <div class="empty-action">
                                                        <a href="${pageContext.request.contextPath}/material/form" class="btn btn-success">첫 번째 재고 등록하기</a>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </c:if>
                
                <!-- 총수량 탭 -->
                <c:if test="${activeTab == 'total'}">
                    <div class="table-container">
                        <table class="material-table">
                            <thead>
                                <tr>
                                    <th>제품코드</th>
                                    <th>제품명</th>
                                    <th>제품유형</th>
                                    <th>총수량</th>
                                    <th>단위</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty totalList}">
                                        <c:forEach var="material" items="${totalList}">
                                            <tr data-type="${material.stType}">
                                                <td class="code-cell">${material.standardCode}</td>
                                                <td class="name-cell">${material.stName}</td>
                                                <td class="type-cell">
                                                    <span class="type-badge type-${material.stType}">
                                                        <c:choose>
                                                            <c:when test="${material.stType == 'RAW'}">원자재</c:when>
                                                            <c:when test="${material.stType == 'SEMI'}">반제품</c:when>
                                                            <c:when test="${material.stType == 'FINISH'}">완제품</c:when>
                                                            <c:otherwise>${material.stType}</c:otherwise>
                                                        </c:choose>
                                                    </span>
                                                </td>
                                                <td class="quantity-cell">
                                                    <div class="quantity-info">
                                                        <span class="status-badge ${material.maQuantity <= 50 ? 'badge-low' : 'badge-normal'}">
                                                            ${material.maQuantity <= 50 ? '부족' : '정상'}
                                                        </span>
                                                        <span class="quantity-value">${material.maQuantity}</span>
                                                    </div>
                                                </td>
                                                <td class="unit-cell">${material.stUnit}</td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="5" class="empty-state">
                                                <div class="empty-content">
                                                    <div class="empty-icon">📊</div>
                                                    <div class="empty-message">총수량 데이터가 없습니다.</div>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </c:if>
                
            </div>
        </div>
    </div>
    
    <script>
        // 재고 삭제 함수
        function deleteMaterial(materialCode) {
            if (confirm('정말로 이 재고를 삭제하시겠습니까?')) {
                // 삭제 요청
                window.location.href = '${pageContext.request.contextPath}/material/delete?code=' + materialCode;
            }
        }
        
        // 클라이언트 사이드 필터링
        function filterByType(type) {
            const rows = document.querySelectorAll('tbody tr[data-type]');
            let visibleCount = 0;
            
            rows.forEach(row => {
                if (type === 'all' || row.getAttribute('data-type') === type) {
                    row.style.display = '';
                    visibleCount++;
                } else {
                    row.style.display = 'none';
                }
            });
            
            // 필터 버튼 활성화 상태 업데이트
            document.querySelectorAll('.filter-btn').forEach(btn => {
                btn.classList.remove('active');
            });
            event.target.classList.add('active');
            
            // 필터링된 결과 개수 업데이트
            const filteredCountElement = document.getElementById('filteredCount');
            if (filteredCountElement) {
                filteredCountElement.textContent = visibleCount;
            }
        }
        
        // 페이지 로드 시 필터링 적용
        document.addEventListener('DOMContentLoaded', function() {
            const filterType = '${filterType}';
            if (filterType && filterType !== 'all') {
                filterByType(filterType);
            }
        });
    </script>
</body>
</html>