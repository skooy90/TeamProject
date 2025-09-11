<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>품질 관리</title>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

   <link rel="stylesheet" href="${pageContext.request.contextPath}/src/Header_Side/style.css">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<style>
/* 컨테이너 */
.container {
	max-width: 1200px;
	margin: 24px auto;
	padding: 28px;
	background: #fff;
	border-radius: 12px;
	box-shadow: 0 6px 16px rgba(0, 0, 0, .08);
}

h1 {
	text-align: center;
	margin: 0 0 18px;
	letter-spacing: .02em;
}

/* 검색 & 버튼 (부트스트랩 없이 자체 스타일) */
.controls {
	display: flex;
	align-items: center;
	gap: 12px;
	margin-bottom: 14px;
}

.controls .search-form {
	display: flex;
	gap: 8px;
	flex: 1;
}

.search-input {
	flex: 1;
	padding: 10px 12px;
	border: 1px solid #d8dde3;
	border-radius: 8px;
	font-size: 14px;
	background: #fff;
}

.btn {
	padding: 10px 14px;
	border: 0;
	border-radius: 8px;
	cursor: pointer;
	font-size: 14px;
	font-weight: 600;
	transition: transform .05s ease, box-shadow .2s ease, opacity .2s ease;
}

.btn:active {
	transform: translateY(1px);
}

.btn-primary {
	background: #1677ff;
	color: #fff;
	box-shadow: 0 2px 8px rgba(22, 119, 255, .25);
}

.btn-success {
	background: #22c55e;
	color: #fff;
	box-shadow: 0 2px 8px rgba(34, 197, 94, .25);
}

.btn-primary:hover, .btn-success:hover {
	opacity: .95;
}

/* 테이블 */
.table-responsive {
	overflow-x: auto;
	border: 1px solid #eef1f4;
	border-radius: 10px;
}

table.table {
	width: 100%;
	border-collapse: separate;
	border-spacing: 0;
	table-layout: fixed;
}

table.table th, table.table td {
	text-align: center;
	vertical-align: middle;
}

thead.table-light th {
	background: #f6f8fb !important;
	color: #344053 !important;
	font-weight: 700;
	border-bottom: 1px solid #e7ecf1 !important;
	padding: 12px 10px;
}

tbody td {
	padding: 12px 10px;
	border-bottom: 1px solid #f0f3f6;
	color: #222;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

tbody tr:hover {
	background: #fafcff;
}

/* 열 너비 가이드 */
.table thead th:nth-child(1), .table tbody td:nth-child(1) {
	width: 110px;
} /* 검사번호 */
.table thead th:nth-child(2), .table tbody td:nth-child(2) {
	width: 110px;
} /* 작업번호 */
.table thead th:nth-child(3), .table tbody td:nth-child(3) {
	width: 110px;
} /* 제품코드 */
.table thead th:nth-child(4), .table tbody td:nth-child(4) {
	width: 110px;
} /* 검사자 */
.table thead th:nth-child(5), .table tbody td:nth-child(5) {
	width: 100px;
} /* 결과 */
.table thead th:nth-child(6), .table tbody td:nth-child(6) {
	width: 110px;
	text-align: right;
} /* 양품 */
.table thead th:nth-child(7), .table tbody td:nth-child(7) {
	width: 110px;
	text-align: right;
} /* 불량 */
.table thead th:nth-child(8), .table tbody td:nth-child(8) {
	width: 130px;
} /* 제조일 */
.table thead th:nth-child(9), .table tbody td:nth-child(9) {
	width: 160px;
} /* 검사일시 */
.table thead th:nth-child(10), .table tbody td:nth-child(10) {
	width: 130px;
} /* 생성일 */
.table thead th:nth-child(11), .table tbody td:nth-child(11) {
	width: 130px;
} /* 수정일 */
.table thead th:nth-child(12), .table tbody td:nth-child(12) {
	width: 110px;
} /* 관리 */
.num {
	text-align: right;
}

.action a {
	color: #1677ff;
	text-decoration: none;
	margin-right: 8px;
}

.action a:last-child {
	margin-right: 0;
}

.text-success {
	color: #16a34a !important;
}

.text-danger {
	color: #e11d48 !important;
}

/* 차트 크기 */
.charts {
	display: grid;
	grid-template-columns: repeat(2, minmax(0, 1fr));
	gap: 16px;
	margin-top: 16px;
}

.chart-box {
	background: #fff;
	border: 1px solid #eef1f4;
	border-radius: 10px;
	padding: 12px;
	height: 320px;
}

.chart-box canvas {
	width: 100% !important;
	height: 100% !important;
}

@media ( max-width :900px) {
	.charts {
		grid-template-columns: 1fr;
	}
}
</style>
</head>
<body>
    <jsp:include page="../../Header_Side/header.jsp" />
	<div class="main-container">
        <jsp:include page="../../Header_Side/sidebar.jsp" />
		<div class="content-area">
			<div class="container">
				<h1>품질 관리</h1>

				<div class="controls">
					<form class="search-form" action="${ctx}/qualityList" method="get">
						<input type="text" class="search-input" name="q" value="${q}"
							placeholder="검사번호 또는 작업번호 검색 (두 글자 이상)">
						<button type="submit" class="btn btn-primary">검색</button>
					</form>
						<a href="${ctx}/quality/form" class="btn btn-success">검사 등록</a>
				</div>

				<div class="table-responsive">
					<table class="table table-bordered">
						<thead class="table-light">
							<tr>
								<th>검사번호</th>
								<th>작업번호</th>
								<th>제품코드</th>
								<th>검사자</th>
								<th>검사 결과</th>
								<th>양품 수량</th>
								<th>불량 수량</th>
								<th>제조일</th>
								<th>검사일시</th>
								<th>생성일</th>
								<th>수정일</th>
								<th>관리</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${empty qualityList}">
									<tr>
										<td colspan="12" class="text-muted py-3">등록된 검사 데이터가
											없습니다.</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach var="row" items="${qualityList}">
										<tr>
											<td>${row.qualityNo}</td>
											<td>${row.workNo}</td>
											<td>${row.standardCode}</td>
											<td>${row.employeeNo}</td>
											<td
												class="${row.quResult eq '불량' ? 'text-danger' : 'text-success'}">${row.quResult}</td>
											<td class="num"><fmt:formatNumber
													value="${row.quQuantity}" /></td>
											<td class="num"><fmt:formatNumber
													value="${row.defectQuantity}" /></td>
											<td><fmt:formatDate value="${row.quManufactureDate}"
													pattern="yyyy-MM-dd" /></td>
											<td><fmt:formatDate value="${row.inspectionDate}"
													pattern="yyyy-MM-dd HH:mm" /></td>
											<td><fmt:formatDate value="${row.createDate}"
													pattern="yyyy-MM-dd" /></td>
											<td><fmt:formatDate value="${row.updateDate}"
													pattern="yyyy-MM-dd" /></td>
											<td class="action"><a
												href="${ctx}/quality/form?no=${row.qualityNo}">수정</a> <a
												class="text-danger"
												href="${ctx}/quality/delete?no=${row.qualityNo}"
												onclick="return confirm('삭제하시겠습니까?');">삭제</a></td>
										</tr>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>

				<div class="charts">
					<div class="chart-box">
						<canvas id="qualityResultChart"></canvas>
					</div>
					<div class="chart-box">
						<canvas id="defectChart"></canvas>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- JS: 차트 -->
	<script>
    const results = [
      <c:forEach var="x" items="${qualityList}" varStatus="s">
        "${fn:escapeXml(x.quResult)}"<c:if test="${!s.last}">,</c:if>
      </c:forEach>
    ];
    const labels = [
      <c:forEach var="x" items="${qualityList}" varStatus="s">
        "${fn:escapeXml(x.qualityNo)}"<c:if test="${!s.last}">,</c:if>
      </c:forEach>
    ];
    const defects = [
      <c:forEach var="x" items="${qualityList}" varStatus="s">
        ${x.defectQuantity}<c:if test="${!s.last}">,</c:if>
      </c:forEach>
    ];

    const okCount = results.filter(r => r === '양품').length;
    const ngCount = results.filter(r => r === '불량').length;

    new Chart(document.getElementById('qualityResultChart'), {
      type: 'pie',
      data: { labels: ['양품','불량'], datasets: [{ data:[okCount, ngCount], backgroundColor: ['#4CAF50','#F44336'] }] },
      options: { responsive:true, maintainAspectRatio:false, plugins:{ title:{ display:true, text:'검사 결과 분포' } } }
    });

    new Chart(document.getElementById('defectChart'), {
      type: 'bar',
      data: { labels, datasets: [{ label:'불량 수량', data:defects }] },
      options: { responsive:true, maintainAspectRatio:false, plugins:{ title:{ display:true, text:'검사별 불량 수량' } }, scales:{ y:{ beginAtZero:true } } }
    });
  </script>
</body>
</html>

