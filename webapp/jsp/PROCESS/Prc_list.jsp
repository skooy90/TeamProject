<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>공정 관리 - MES 시스템</title>
    <c:set var="ctx" value="${pageContext.request.contextPath}" />
    
  <link rel="stylesheet" href="${ctx}/css/process_list.css">
  <link rel="stylesheet" href="${ctx}/Header_Side/style.css">
  
</head>
<body>
    <jsp:include page="../../Header_Side/header.jsp" />
    
    <div class="main-container">
        <jsp:include page="../../Header_Side/sidebar.jsp" />
        
        <div class="content-area">
            <div class="container">
                <h1>공정 관리</h1>

                <!-- 검색 & 등록 -->
                <div class="controls">
                    <form class="search-form"
                        action="${pageContext.request.contextPath}/process/search"
                        method="get">
                        <select name="searchType" class="search-select">
                            <option value="all" ${searchType == 'all' ? 'selected' : ''}>전체 검색</option>
                            <option value="code" ${searchType == 'code' ? 'selected' : ''}>공정번호</option>
                            <option value="name" ${searchType == 'name' ? 'selected' : ''}>제품명</option>
                        </select>
                        <input type="text" class="search-input" name="searchKeyword"
                            placeholder="검색어를 입력하세요" value="${searchKeyword}">
                        <button type="submit" class="btn btn-primary">검색</button>
                    </form>
                    <a href="${pageContext.request.contextPath}/process/form"
                        class="btn btn-success">공정 등록</a>
                </div>

                <!-- 통계 정보 -->
                <div class="stats-section">
                    <div class="stat-card">
                        <h3>전체 공정</h3>
                        <p class="number">${totalCount}</p>
                    </div>
                    <div class="stat-card">
                        <h3>반제품 공정</h3>
                        <p class="number">${semiProcessCount}</p>
                    </div>
                    <div class="stat-card">
                        <h3>완제품 공정</h3>
                        <p class="number">${finishProcessCount}</p>
                    </div>
                </div>

                <!-- 공정 목록 테이블 -->
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>라우팅번호</th>
                                <th>제품코드</th>
                                <th>라우팅명</th>
                                <th>라우팅 타입</th>
                                <th>공정 단계</th>
                                <th>관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${processList != null && !processList.isEmpty()}">
                                    <c:forEach var="process" items="${processList}">
                                        <tr data-type="${process.stType}">
                                            <td>${process.processNo}</td>
                                            <td>${process.standardCode}</td>
                                            <td><a href="${pageContext.request.contextPath}/process/detail?code=${process.standardCode}" class="detail-link">${process.prDescription}</a></td>
                                            <td>
                                                <span class="routing-badge ${process.stType == 'SEMI' ? 'routing-semi' : 'routing-finish'}">
                                                    ${process.stType == 'SEMI' ? '반제품' : '완제품'}
                                                </span>
                                            </td>
                                            <td>
                                                <span class="step-badge ${process.stType == 'SEMI' ? 'step-semi' : 'step-finish'}">
                                                    ${process.prOrder}단계
                                                </span>
                                            </td>
                                            <td class="action-links">
                                                <a href="${pageContext.request.contextPath}/process/form?code=${process.standardCode}" class="edit">수정</a>
                                                <a href="${pageContext.request.contextPath}/process/delete?code=${process.standardCode}" class="delete" onclick="return confirm('정말 삭제하시겠습니까?')">삭제</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="6" style="text-align: center; padding: 20px; color: #6c757d;">
                                            등록된 라우팅이 없습니다.
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

