<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BOM 관리 - MES 시스템</title>
        <c:set var="ctx" value="${pageContext.request.contextPath}" />
    
  <link rel="stylesheet" href="${ctx}/css/bom_list.css">
  <link rel="stylesheet" href="${ctx}/Header_Side/style.css">
</head>
<body>
    <jsp:include page="../../Header_Side/header.jsp" />
    
    <div class="main-container">
        <jsp:include page="../../Header_Side/sidebar.jsp" />
        
        <div class="content-area">
            <div class="container">
                <h1>BOM 관리</h1>

                <!-- 검색 & 등록 -->
                <div class="controls">
                    <form class="search-form"
                        action="${pageContext.request.contextPath}/bom/search"
                        method="get">
                        <select name="searchType" class="search-select">
                            <option value="all" ${searchType == 'all' ? 'selected' : ''}>전체 검색</option>
                            <option value="code" ${searchType == 'code' ? 'selected' : ''}>BOM번호</option>
                            <option value="name" ${searchType == 'name' ? 'selected' : ''}>BOM명</option>
                        </select>
                        <input type="text" class="search-input" name="searchKeyword"
                            placeholder="검색어를 입력하세요" value="${searchKeyword}">
                        <button type="submit" class="btn btn-primary">검색</button>
                    </form>
                    <a href="${pageContext.request.contextPath}/bom/form"
                        class="btn btn-success">BOM 등록</a>
                </div>

                <!-- 통계 정보 -->
                <div class="stats-section">
                    <div class="stat-card">
                        <h3>전체 BOM</h3>
                        <p class="number">${totalCount}</p>
                    </div>
                    <div class="stat-card">
                        <h3>반제품 BOM</h3>
                        <p class="number">${semiBOMCount}</p>
                    </div>
                    <div class="stat-card">
                        <h3>완제품 BOM</h3>
                        <p class="number">${finishBOMCount}</p>
                    </div>
                </div>

                <!-- 검색 결과 정보 -->
                <c:if test="${not empty searchKeyword}">
                    <div class="search-result-info" style="background: #e3f2fd; padding: 10px; border-radius: 4px; margin-bottom: 20px; border-left: 4px solid #2196f3;">
                        <strong>검색 결과:</strong> 
                        "<span style="color: #1976d2;">${searchKeyword}</span>" 
                        (${searchType == 'all' ? '전체 검색' : searchType == 'code' ? 'BOM번호' : 'BOM명'}) 
                        - <strong>${bomList != null ? bomList.size() : 0}개</strong> BOM 발견
                    </div>
                </c:if>

                <!-- BOM 목록 테이블 -->
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>BOM번호</th>
                                <th>제품코드</th>
                                <th>BOM명</th>
                                <th>BOM 유형</th>
                                <th>BOM 순서</th>
                                <th>관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${bomList != null && !bomList.isEmpty()}">
                                    <c:forEach var="bom" items="${bomList}">
                                        <tr>
                                            <td>${bom.bomNo}</td>
                                            <td>${bom.standardCode}</td>
                                            <td><a href="${pageContext.request.contextPath}/bom/detail?code=${bom.standardCode}" class="detail-link">${bom.bomDescription}</a></td>
                                            <td>
                                                <span class="routing-badge ${bom.bomType == '원자재' ? 'routing-raw' : bom.bomType == '반제품' ? 'routing-semi' : 'routing-finish'}">
                                                    ${bom.bomType}
                                                </span>
                                            </td>
                                            <td>${bom.bomOrder}</td>
                                            <td class="action-links">
                                                <a href="${pageContext.request.contextPath}/bom/form?code=${bom.standardCode}" class="edit">수정</a>
                                                <a href="${pageContext.request.contextPath}/bom/delete?code=${bom.standardCode}" class="delete" onclick="return confirm('정말 삭제하시겠습니까?')">삭제</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="6" style="text-align: center; padding: 20px; color: #6c757d;">
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