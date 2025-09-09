<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>재고 목록 - 자재관리</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/src/Header_Sied/style.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/src/css/material_list.css">
</head>
<body>
    <jsp:include page="/src/Header_Sied/header.jsp" />
    <div class="main-container">
        <jsp:include page="/src/Header_Sied/sidebar.jsp" />
        <div class="content-area">
            <div class="container">
                <h1>재고 목록</h1>
                
                <!-- 통계 정보 -->
                <div class="stats">
                    <div class="stat-item">
                        <div class="stat-number">${totalCount != null ? totalCount : '13'}</div>
                        <div class="stat-label">전체 재고</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-number">${lowStockCount != null ? lowStockCount : '2'}</div>
                        <div class="stat-label">재고 부족</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-number">${rawMaterialCount != null ? rawMaterialCount : '6'}</div>
                        <div class="stat-label">원자재</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-number">${semiProductCount != null ? semiProductCount : '4'}</div>
                        <div class="stat-label">반제품</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-number">${finishedProductCount != null ? finishedProductCount : '3'}</div>
                        <div class="stat-label">완제품</div>
                    </div>
                </div>
                
                <!-- 상단 컨트롤 -->
                <div class="controls">
                    <div class="search-section">
                        <form class="search-form" method="get" action="${pageContext.request.contextPath}/material/search">
                            <input type="text" class="search-input" placeholder="재고코드 또는 제품명 검색" 
                                   name="searchKeyword" value="${searchKeyword != null ? searchKeyword : ''}">
                            <select name="searchType" style="padding: 10px; border: 1px solid #ddd; border-radius: 4px;">
                                <option value="all" ${searchType == 'all' ? 'selected' : ''}>전체</option>
                                <option value="code" ${searchType == 'code' ? 'selected' : ''}>재고코드</option>
                                <option value="name" ${searchType == 'name' ? 'selected' : ''}>제품명</option>
                            </select>
                            <button type="submit" class="btn btn-primary">검색</button>
                        </form>
                        
                        <!-- 제품유형 필터 버튼 -->
                        <div class="filter-buttons">
                            <a href="${pageContext.request.contextPath}/material/filter?type=all" class="filter-btn ${selectedType == 'all' || selectedType == null ? 'active' : ''}">전체</a>
                            <a href="${pageContext.request.contextPath}/material/filter?type=원자재" class="filter-btn ${selectedType == '원자재' ? 'active' : ''}">원자재</a>
                            <a href="${pageContext.request.contextPath}/material/filter?type=반제품" class="filter-btn ${selectedType == '반제품' ? 'active' : ''}">반제품</a>
                            <a href="${pageContext.request.contextPath}/material/filter?type=완제품" class="filter-btn ${selectedType == '완제품' ? 'active' : ''}">완제품</a>
                        </div>
                    </div>
                    
                    <div class="sort-section">
                        <label for="sortSelect">정렬:</label>
                        <select class="sort-select" id="sortSelect" onchange="sortMaterials()">
                            <option value="quantity_desc">재고량 많은 순</option>
                            <option value="quantity_asc">재고량 적은 순</option>
                            <option value="code_asc">재고코드 순</option>
                            <option value="name_asc">제품명 순</option>
                        </select>
                    </div>
                    
                    <a href="${pageContext.request.contextPath}/material/form" class="btn btn-success">재고 등록</a>
                </div>
                
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
                
                <!-- 재고 목록 테이블 -->
                <table id="materialTable">
                    <thead>
                        <tr>
                            <th>재고코드</th>
                            <th>제품코드</th>
                            <th>제품명</th>
                            <th>제품유형</th>
                            <th>재고량</th>
                            <th>단위</th>
                            <th>담당자</th>
                            <th>생성일</th>
                            <th>수정일</th>
                            <th>관리</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty materialList}">
                                <c:forEach var="material" items="${materialList}">
                                    <tr data-type="${material.stType}">
                                        <td><a href="${pageContext.request.contextPath}/material/detail?code=${material.materialCode}" class="detail-link">${material.materialCode}</a></td>
                                        <td>${material.standardCode}</td>
                                        <td><a href="${pageContext.request.contextPath}/material/detail?code=${material.materialCode}" class="detail-link">${material.stName}</a></td>
                                        <td>${material.stType}</td>
                                        <td class="${material.maQuantity <= 50 ? 'status-low' : material.maQuantity >= 1000 ? 'status-high' : 'status-normal'}">
                                            <span class="status-badge ${material.maQuantity <= 50 ? 'badge-low' : material.maQuantity >= 1000 ? 'badge-high' : 'badge-normal'}">
                                                ${material.maQuantity <= 50 ? '부족' : material.maQuantity >= 1000 ? '과다' : '정상'}
                                            </span>
                                            ${material.maQuantity}
                            </td>
                            <td>kg</td>
                                        <td>${material.usName}</td>
                                        <td><fmt:formatDate value="${material.maCreationDate}" pattern="yyyy-MM-dd"/></td>
                                        <td><fmt:formatDate value="${material.maUpdateDate}" pattern="yyyy-MM-dd"/></td>
                            <td class="action-links">
                                            <a href="${pageContext.request.contextPath}/material/detail?code=${material.materialCode}">상세</a>
                                            <a href="${pageContext.request.contextPath}/material/form?code=${material.materialCode}">수정</a>
                                            <a href="#" class="delete" onclick="deleteMaterial('${material.materialCode}')">삭제</a>
                            </td>
                        </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="10" style="text-align: center; padding: 50px; color: #6c757d;">
                                        <div style="font-size: 18px; margin-bottom: 10px;">📦</div>
                                        <div>등록된 재고가 없습니다.</div>
                                        <div style="margin-top: 10px;">
                                            <a href="${pageContext.request.contextPath}/material/form" class="btn btn-success" style="text-decoration: none;">첫 번째 재고 등록하기</a>
                                        </div>
                            </td>
                        </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
                
                <!-- 페이지네이션 -->
                <div class="pagination">
                    <a href="#" class="disabled">&laquo;</a>
                    <span class="current">1</span>
                    <a href="#">2</a>
                    <a href="#">3</a>
                    <a href="#">4</a>
                    <a href="#">5</a>
                    <a href="#">&raquo;</a>
                </div>
            </div>
        </div>
    </div>
    
</body>
</html>

