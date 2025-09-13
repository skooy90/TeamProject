<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- Add JSTL core tag library --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>기준관리</title>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx}/Header_Side/style.css">
<link rel="stylesheet" href="${ctx}/css/style.css">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<style>
/* 페이지별 고유 스타일은 여기에 추가 */
.container {
	max-width: 1000px;
	margin: 20px auto;
	padding: 30px;
	background-color: #ffffff;
	border-radius: 8px;
	box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

h1 {
	text-align: center;
}

.controls {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 20px;
}

.search-form {
	display: flex;
	gap: 10px;
}

.search-input {
	padding: 10px;
	border: 1px solid #ccc;
	border-radius: 4px;
	font-size: 1em;
}

.btn {
	padding: 10px 15px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-size: 1em;
}

.btn-primary {
	background-color: #007bff;
	color: white;
}

.btn-success {
	background-color: #28a745;
	color: white;
}

table {
	width: 100%;
	border-collapse: collapse;
}

th, td {
	border: 1px solid #e9ecef;
	padding: 12px;
	text-align: left;
}

th {
	background-color: #e9ecef;
	font-weight: bold;
	color: #495057;
}

tr:nth-child(even) {
	background-color: #f8f9fa;
}

.action-links a {
	text-decoration: none;
	color: #007bff;
	margin-right: 10px;
}

.action-links a:hover {
	text-decoration: underline;
}

.no-data {
	text-align: center;
	color: #6c757d;
	padding: 20px;
}

.pg {
	padding: 6px 10px;
	border: 1px solid #d0d7de;
	border-radius: 6px;
	text-decoration: none;
	color: #111;
	background: #fff;
	font-size: 14px;
}

.pg.active {
	background: #1677ff;
	color: #fff;
	border-color: #1677ff;
}

.pg.disabled {
	pointer-events: none;
	opacity: .5;
}

.pg:hover {
	background: #f6f8fa;
}
</style>
</head>
<body>
	<jsp:include page="../../Header_Side/header.jsp" />
	   
	<div class="main-container">
		       
		<jsp:include page="../../Header_Side/sidebar.jsp" />
		       
		<div class="content-area">
			<div class="container">
				<h1>기준 관리</h1>
				<div class="controls">
					<form class="search-form"
						action="${pageContext.request.contextPath}/standardList"
						method="get">
						<input type="text" class="search-input" name="q"
							placeholder="제품 코드 또는 이름 검색 (두 글자 이상)" value="${q}" />
						<button type="submit" class="btn btn-primary">검색</button>
					</form>
					<a href="${ctx}/standard/form" class="btn btn-success">제품 등록</a>
				</div>
				<table>
					<thead>
						<tr>
							<th>제품 코드</th>
							<th>제품 이름</th>
							<th>제품 유형</th>
							<th>제품 단위</th>
							<th>수정일</th>
							<th>관리</th>
						</tr>
					</thead>
					<tbody>
						<%-- Check if the list from the servlet is empty --%>
						<c:choose>
							<c:when test="${empty standardList}">
								<tr>
									<td colspan="7" class="no-data">등록된 제품이 없습니다.</td>
								</tr>
							</c:when>
							<c:otherwise>
								<%-- Loop through each item in the standardList --%>
								<c:forEach var="item" items="${standardList}">
									<tr>
										<td>${item.standardCode}</td>
										<td>${item.stName}</td>
										<td>${item.stType}</td>
										<td>${item.stUnit}</td>
										<td>${item.updateDate}</td>
										<td class="action-links">
											<%-- Pass the unique code as a parameter for modify/delete actions --%>
											<a href="modifyStandard?code=${item.standardCode}">수정</a><a
											href="${pageContext.request.contextPath}/standard/delete?code=${item.standardCode}">삭제</a>
										</td>
									</tr>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
				    <div class="container">
      <!-- 표/내용 -->
      ...
      <!-- ✅ 페이지네이션을 컨텐트 안쪽으로 이동 -->
      <div class="pager" style="display:flex; justify-content:center; gap:6px; margin:16px 0;">
        <c:set var="ctx" value="${pageContext.request.contextPath}" />
        <c:set var="qparam">
          <c:if test="${not empty q}">&amp;q=${fn:escapeXml(q)}</c:if>
        </c:set>

        <a href="${ctx}/standardList?page=1${qparam}" class="pg ${page==1?'disabled':''}">« 처음</a>
        <a href="${ctx}/standardList?page=${page-1}${qparam}" class="pg ${page==1?'disabled':''}">‹ 이전</a>

        <c:forEach var="p" begin="${startPage}" end="${endPage}">
          <a href="${ctx}/standardList?page=${p}${qparam}" class="pg ${p==page?'active':''}">${p}</a>
        </c:forEach>

        <a href="${ctx}/standardList?page=${page+1}${qparam}" class="pg ${page==totalPages?'disabled':''}">다음 ›</a>
        <a href="${ctx}/standardList?page=${totalPages}${qparam}" class="pg ${page==totalPages?'disabled':''}">마지막 »</a>
      </div>
			</div>
		</div>
	</div>
</div>
</body>

<script>
	document.querySelector('.search-form').addEventListener('submit',
			function(e) {
				const v = this.q.value.trim();
				if (v.length > 0 && v.length < 2) {
					alert('두 글자 이상 입력해 주세요.');
					e.preventDefault();
				}
			});
</script>
</html>