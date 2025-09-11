<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
// 기대: WorkListServlet이 아래 속성 세팅
// request.setAttribute("workList", List<WorkDTO>);
// request.setAttribute("q", 검색어);
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>작업관리</title>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/src/Header_Side/style.css">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<style>
.container {
	max-width: 1200px;
	margin: 24px auto;
	padding: 28px;
	background: #fff;
	border-radius: 12px;
	box-shadow: 0 6px 16px rgba(0, 0, 0, .08)
}

h1 {
	margin: 0 0 18px;
	text-align: center
}

.controls {
	display: flex;
	align-items: center;
	gap: 12px;
	margin-bottom: 14px
}

.controls .search-form {
	display: flex;
	gap: 8px;
	flex: 1
}

.search-input {
	flex: 1;
	padding: 10px 12px;
	border: 1px solid #d8dde3;
	border-radius: 8px;
	font-size: 14px
}

.btn {
	padding: 10px 14px;
	border: 0;
	border-radius: 8px;
	cursor: pointer;
	font-size: 14px;
	font-weight: 600;
	transition: transform .05s ease, opacity .2s ease
}

.btn:active {
	transform: translateY(1px)
}

.btn-primary {
	background: #1677ff;
	color: #fff
}

.btn-success {
	background: #22c55e;
	color: #fff
}

.table-responsive {
	overflow-x: auto;
	border: 1px solid #eef1f4;
	border-radius: 10px
}

/* 표 */
table {
	width: 100%;
	min-width: 1200px; /* ▶ 가로 스크롤 여유 */
	border-collapse: separate;
	border-spacing: 0;
	table-layout: fixed; /* 열 폭 지정용 */
}

thead th {
	position: sticky;
	top: 0;
	background: #f6f8fb;
	color: #344053;
	font-weight: 700;
	border-bottom: 1px solid #e7ecf1;
	padding: 12px 10px;
	white-space: nowrap
}

tbody td {
	padding: 12px 10px;
	border-bottom: 1px solid #f0f3f6;
	white-space: nowrap
}
/* 긴 텍스트 칸만 .truncate 클래스로 말줄임 */
.truncate {
	overflow: hidden;
	text-overflow: ellipsis
}

tbody tr:hover {
	background: #fafcff
}

/* 열 폭 가이드 */
th:nth-child(1), td:nth-child(1) {
	width: 110px
} /* 작업번호 */
th:nth-child(2), td:nth-child(2) {
	width: 110px
} /* 생산번호 */
th:nth-child(3), td:nth-child(3) {
	width: 110px
} /* 제품코드 */
th:nth-child(4), td:nth-child(4) {
	width: 100px
} /* 담당자 */
th:nth-child(5), td:nth-child(5) {
	width: 120px
} /* 작업 일정 */
th:nth-child(6), td:nth-child(6) {
	width: 90px
} /* 작업량 */
th:nth-child(7), td:nth-child(7) {
	width: 90px
} /* 상태 */
th:nth-child(8), td:nth-child(8) {
	width: 110px
} /* 완료량 */
th:nth-child(9), td:nth-child(9) {
	width: 120px
} /* 시작일 */
th:nth-child(10), td:nth-child(10) {
	width: 120px
} /* 종료일 */
th:nth-child(11), td:nth-child(11) {
	width: 120px
} /* 생성일 */
th:nth-child(12), td:nth-child(12) {
	width: 120px
} /* 수정일 */
th:nth-child(13), td:nth-child(13) {
	width: 140px
} /* 관리(버튼) */

/* 액션 셀: 말줄임 금지 & 버튼 보이게 */
.cell-actions {
	white-space: nowrap;
	overflow: visible;
	text-align: center;
}

.cell-actions a {
	text-decoration: none;
	font-weight: 600;
	margin: 0 6px;
	color: #1677ff;
}

.cell-actions a.delete {
	color: #dc2626
} /* 삭제 빨간색 */
.cell-actions a:hover {
	text-decoration: underline
}

.no-data {
	text-align: center;
	color: #8a95a3;
	padding: 22px;
	background: #fafbfc
}

.charts {
	display: grid;
	grid-template-columns: 1fr 1fr;
	gap: 16px;
	margin-top: 18px
}

.chart-card {
	background: #fff;
	border: 1px solid #eef1f4;
	border-radius: 10px;
	padding: 14px
}

@media ( max-width :1024px) {
	.charts {
		grid-template-columns: 1fr
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

				<h1>작업관리</h1>

				<!-- 검색 + 등록 -->
				<div class="controls">
					<form class="search-form" action="${ctx}/workList" method="get">
						<input class="search-input" type="text" name="q" value="${q}"
							placeholder="작업번호 또는 생산번호 (두 글자 이상)">
						<button class="btn btn-primary" type="submit">검색</button>
					</form>
					<a class="btn btn-success" href="${ctx}/work/form">작업 등록</a>
				</div>

				<!-- 표 -->
				<div class="table-responsive">
					<table>
						<thead>
							<tr>
								<th>작업번호</th>
								<th>생산번호</th>
								<th>제품명</th>
								<th>담당자</th>
								<th>작업 일정</th>
								<th>작업량</th>
								<th>작업 완료량</th>
								<th>작업 시작일</th>
								<th>작업 종료일</th>
								<th>생성일</th>
								<th>수정일</th>
								<th>관리</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${empty workList}">
									<tr>
										<td colspan="13" class="no-data">등록된 작업이 없습니다.</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach var="w" items="${workList}">
										<tr>
											<td>${w.workNo}</td>
											<td>${w.productionNo}</td>
											<td>${w.stName}</td>
											<td>${w.employeeNo}</td>
											<td class="truncate"><fmt:formatDate
													value="${w.woSchedule}" pattern="yyyy-MM-dd" /></td>

											<td>${w.woQuantity}</td>
											<td>${w.woCompleted}</td>
											<td><fmt:formatDate value="${w.woStart}"
													pattern="yyyy-MM-dd" /></td>
											<td><fmt:formatDate value="${w.woEnd}"
													pattern="yyyy-MM-dd" /></td>
											<td><fmt:formatDate value="${w.createDate}"
													pattern="yyyy-MM-dd" /></td>
											<td><fmt:formatDate value="${w.updateDate}"
													pattern="yyyy-MM-dd" /></td>
											<td class="cell-actions"><a
												href="${ctx}/work/form?no=${w.workNo}">수정</a> <a
												class="delete" href="${ctx}/work/delete?no=${w.workNo}"
												onclick="return confirm('정말 삭제하시겠습니까?');">삭제</a></td>
										</tr>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>

				<!-- 차트 -->
				<div class="charts">
					<div class="chart-card">
						<canvas id="amountChart"></canvas>
					</div>
<!-- 					<div class="chart-card"> -->
<%-- 						<canvas id="statusChart"></canvas> --%>
<!-- 					</div> -->
					<!-- 					<div class="chart-card" style="grid-column: 1/-1"> -->
					<%-- 						<canvas id="periodChart"></canvas> --%>
					<!-- 					</div> -->
				</div>

			</div>
		</div>
	</div>

	<!-- 데이터 직렬화 -->
	<script>
    const labels = [
      <c:forEach var="w" items="${workList}" varStatus="s">
        "${fn:escapeXml(w.workNo)}"<c:if test="${!s.last}">,</c:if>
      </c:forEach>
    ];
    const qty = [
      <c:forEach var="w" items="${workList}" varStatus="s">
        ${w.woQuantity}<c:if test="${!s.last}">,</c:if>
      </c:forEach>
    ];
    const done = [
      <c:forEach var="w" items="${workList}" varStatus="s">
        ${w.woCompleted}<c:if test="${!s.last}">,</c:if>
      </c:forEach>
    ];
    const statuses = [
      <c:forEach var="w" items="${workList}" varStatus="s">
        "${fn:escapeXml(w.woStatus)}"<c:if test="${!s.last}">,</c:if>
      </c:forEach>
    ];
    // 상태 카운트
    const countBy = (arr) => arr.reduce((m,v)=> (m[v]=(m[v]||0)+1, m), {});
    const stMap = countBy(statuses);
    const stLabels = Object.keys(stMap);
    const stValues = Object.values(stMap);

    // 기간(일수) = woEnd - woStart (yyyy-MM-dd만 내려왔으니 0h 기준 계산)
    const period = [
      <c:forEach var="w" items="${workList}" varStatus="s">
        (function(){
          const s = "<fmt:formatDate value='${w.woStart}' pattern='yyyy-MM-dd'/>";
          const e = "<fmt:formatDate value='${w.woEnd}'   pattern='yyyy-MM-dd'/>";
          if(!s || !e) return 0;
          const sd = new Date(s), ed = new Date(e);
          return Math.max(0, Math.round((ed - sd)/86400000));
        })()<c:if test="${!s.last}">,</c:if>
      </c:forEach>
    ];

    // 차트 1: 작업량 vs 완료량
    new Chart(document.getElementById('amountChart'), {
      type:'bar',
      data:{ labels, datasets:[
        {label:'작업량', data: qty, backgroundColor:'rgba(54,162,235,.5)', borderColor:'rgba(54,162,235,1)', borderWidth:1},
        {label:'작업 완료량', data: done, backgroundColor:'rgba(75,192,192,.5)', borderColor:'rgba(75,192,192,1)', borderWidth:1}
      ]},
      options:{ responsive:true, scales:{ y:{beginAtZero:true} } }
    });

    // 차트 2: 상태 분포
    new Chart(document.getElementById('statusChart'), {
      type:'pie',
      data:{ labels: stLabels, datasets:[{ data: stValues }]},
      options:{ plugins:{ title:{display:true, text:'작업 상태 분포'} } }
    });

    // 차트 3: 작업 기간
    new Chart(document.getElementById('periodChart'), {
      type:'bar',
      data:{ labels, datasets:[
        {label:'작업 기간(일)', data: period, backgroundColor:'rgba(153,102,255,.5)', borderColor:'rgba(153,102,255,1)', borderWidth:1}
      ]},
      options:{ indexAxis:'y', scales:{ x:{beginAtZero:true} }, plugins:{ title:{display:true, text:'작업 일정 분포'} } }
    });
  </script>
</body>
</html>
