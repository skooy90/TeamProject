<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BOM 관리 - MES 시스템</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/src/Header_Sied/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/src/css/bom_list.css">
</head>
<body>
    <jsp:include page="../../Header_Sied/header.jsp" />
    
    <div class="main-container">
        <jsp:include page="../../Header_Sied/sidebar.jsp" />
        
        <div class="content-area">
            <div class="container">
                <!-- 헤더 섹션 -->
                <div class="controls">
                    <h1 class="page-title">BOM 관리</h1>
                    <div class="search-section">
                        <form action="${pageContext.request.contextPath}/bom/search" method="get" class="search-form">
                            <input type="text" name="searchKeyword" placeholder="BOM번호, 제품명, BOM설명 검색..." value="${searchKeyword}" style="padding: 10px; border: 1px solid #ddd; border-radius: 4px;">
                            <select name="searchType" style="padding: 10px; border: 1px solid #ddd; border-radius: 4px;">
                                <option value="all" ${searchType == 'all' ? 'selected' : ''}>전체</option>
                                <option value="code" ${searchType == 'code' ? 'selected' : ''}>BOM번호</option>
                                <option value="name" ${searchType == 'name' ? 'selected' : ''}>제품명</option>
                            </select>
                            <button type="submit" class="btn btn-primary">검색</button>
                        </form>
                        <div class="filter-buttons">
                            <a href="${pageContext.request.contextPath}/bom/filter?type=all" class="filter-btn ${filterType == 'all' || filterType == null ? 'active' : ''}">전체</a>
                            <a href="${pageContext.request.contextPath}/bom/filter?type=SEMI" class="filter-btn ${filterType == 'SEMI' ? 'active' : ''}">반제품</a>
                            <a href="${pageContext.request.contextPath}/bom/filter?type=FINISH" class="filter-btn ${filterType == 'FINISH' ? 'active' : ''}">완제품</a>
                        </div>
                        <a href="${pageContext.request.contextPath}/bom/form" class="btn btn-primary">BOM 등록</a>
                    </div>
                </div>

                <!-- 통계 정보 -->
                <div class="stats-section">
                    <div class="stat-card">
                        <h3>전체 BOM</h3>
                        <p class="number">${totalCount}</p>
                    </div>
                    <div class="stat-card">
                        <h3>완제품 BOM</h3>
                        <p class="number">${finishBOMCount}</p>
                    </div>
                    <div class="stat-card">
                        <h3>반제품 BOM</h3>
                        <p class="number">${semiBOMCount}</p>
                    </div>
                    <div class="stat-card">
                        <h3>총 자재 수</h3>
                        <p class="number">${totalMaterialCount}</p>
                    </div>
                </div>

                <!-- BOM 목록 테이블 -->
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>BOM번호</th>
                                <th>제품코드</th>
                                <th>제품명</th>
                                <th>BOM 설명</th>
                                <th>BOM 유형</th>
                                <th>BOM 순서</th>
                                <th>자재 수</th>
                                <th>관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${bomList != null && !bomList.isEmpty()}">
                                    <c:forEach var="bom" items="${bomList}">
                                        <tr data-type="${bom.stType}" data-bom-type="${bom.bomType}">
                                            <td>${bom.bomNo}</td>
                                            <td>${bom.standardCode}</td>
                                            <td>${bom.stName}</td>
                                            <td>${bom.bomDescription}</td>
                                            <td><span class="type-badge type-${bom.stType == 'SEMI' ? 'semi' : 'finish'}">${bom.stType == 'SEMI' ? '반제품' : '완제품'}</span></td>
                                            <td><span class="order-badge">${bom.bomOrder}</span></td>
                                            <td><span class="material-count">-</span></td>
                                            <td class="action-links">
                                                <a href="${pageContext.request.contextPath}/bom/detail?code=${bom.standardCode}" class="detail">상세</a>
                                                <a href="${pageContext.request.contextPath}/bom/form?code=${bom.bomNo}" class="edit">수정</a>
                                                <a href="${pageContext.request.contextPath}/bom/delete?code=${bom.bomNo}" class="delete" onclick="return confirm('정말 삭제하시겠습니까?')">삭제</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="8" style="text-align: center; padding: 20px; color: #6c757d;">
                                            등록된 BOM이 없습니다.
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    </div>

</body>
</html>