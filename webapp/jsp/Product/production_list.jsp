<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>생산관리</title>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
   <link rel="stylesheet" href="${pageContext.request.contextPath}/src/Header_Side/style.css">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<style>
/* 페이지 전용 스타일 */
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

/* 표 래퍼(가로 스크롤) */
.table-responsive {
	overflow-x: auto;
	border: 1px solid #eef1f4;
	border-radius: 10px;
}

/* 표 */
table {
	width: 100%;
	min-width: 980px;
	border-collapse: separate;
	border-spacing: 0;
	table-layout: fixed; /* 말줄임 동작 위해 */
}

thead th {
	position: sticky;
	top: 0;
	z-index: 1;
	background: #f6f8fb;
	color: #344053;
	font-weight: 700;
	border-bottom: 1px solid #e7ecf1;
	padding: 12px 10px;
}

tbody td {
	padding: 12px 10px;
	border-bottom: 1px solid #f0f3f6;
	color: #222;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis; /* 긴 값 말줄임 */
}

tbody tr:hover {
	background: #fafcff;
}

/* 가독성을 위한 너비 가이드(필요 시 조절) */
th:nth-child(1), td:nth-child(1) {
	width: 120px;
} /* 생산번호 */
th:nth-child(2), td:nth-child(2) {
	width: 110px;
} /* 제품코드 */
th:nth-child(3), td:nth-child(3), th:nth-child(4), td:nth-child(4) {
	width: 130px;
} /* 날짜들 */
th:nth-child(5), td:nth-child(5), th:nth-child(6), td:nth-child(6) {
	width: 120px;
} /* 수량 */
th:nth-child(7), td:nth-child(7) {
	width: 110px;
} /* 담당자 */
th:nth-child(8), td:nth-child(8), th:nth-child(9), td:nth-child(9) {
	width: 130px;
} /* 생성/수정 */
th:nth-child(10), td:nth-child(10) {
	width: 120px;
} /* 관리 */

/* 액션 링크 */
.action-links a {
	color: #1677ff;
	text-decoration: none;
	margin-right: 8px;
}

.action-links a:last-child {
	margin-right: 0;
}

.action-links a:hover {
	text-decoration: underline;
}

/* 차트 */
.chart-container {
	margin: 18px auto 4px;
	width: 100%;
	max-width: 900px;
	height: 320px; /* 고정 높이로 반응형 차트 안정화 */
}

.no-data {
	text-align: center;
	color: #8a95a3;
	padding: 22px;
	background: #fafbfc;
}
</style>

</head>

<body>
    <jsp:include page="../../Header_Side/header.jsp" />
	<div class="main-container">
        <jsp:include page="../../Header_Side/sidebar.jsp" />
		<div class="content-area">
			<div class="container">

				<h1>생산관리</h1>

				<!-- 검색 & 등록 -->
				<div class="controls">
					<form class="search-form"
						action="${pageContext.request.contextPath}/productionList"
						method="get">
						<input type="text" class="search-input" name="q"
							placeholder="생산번호 또는 제품코드 검색 (두 글자 이상)" value="${q}">
						<button type="submit" class="btn btn-primary">검색</button>
					</form>
					<a href="${pageContext.request.contextPath}/production/form"
						class="btn btn-success">생산 등록</a>
				</div>

				<!-- 테이블 -->
				<div class="table-responsive">
					<table>
						<thead>
							<tr>
								<th>생산번호</th>
								<th>제품코드</th>
								<th>생산 시작일</th>
								<th>생산 종료일</th>
								<th>생산 목표량</th>
								<th>생산 완료량</th>
								<th>담당자</th>
								<th>생성일</th>
								<th>수정일</th>
								<th>관리</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${empty productionList}">
									<tr>
										<td colspan="10" class="no-data">등록된 생산 정보가 없습니다.</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach var="p" items="${productionList}">
										<tr>
											<td>${p.productionNo}</td>
											<td>${p.standardCode}</td>
											<td><fmt:formatDate value="${p.prStart}"
													pattern="yyyy-MM-dd" /></td>
											<td><fmt:formatDate value="${p.prEnd}"
													pattern="yyyy-MM-dd" /></td>
											<td>${p.prTarget}</td>
											<td>${p.prCompleted}</td>
											<!-- 담당자: 현재는 employeeNo를 그대로 표기. 조인으로 이름 필요시 컨트롤러에서 같이 내려주세요 -->
											<td>${p.employeeNo}</td>
											<td><fmt:formatDate value="${p.createDate}"
													pattern="yyyy-MM-dd" /></td>
											<td><fmt:formatDate value="${p.updateDate}"
													pattern="yyyy-MM-dd" /></td>
											<td class="action-links"><a
												href="${pageContext.request.contextPath}/production/form?no=${p.productionNo}">수정</a>
												<a
												href="${pageContext.request.contextPath}/production/delete?no=${p.productionNo}"
												onclick="return confirm('정말 삭제하시겠습니까?');">삭제</a></td>
										</tr>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>

				<!-- 차트 -->
				<div class="chart-container">
					<canvas id="productionChart"></canvas>
				</div>

			</div>
		</div>
	</div>

	<!-- Chart.js: 서버 데이터로 라벨/데이터 구성 -->
	<script>
    // JSP 데이터를 JS 배열로 직렬화
    const labels = [
      <c:forEach var="p" items="${productionList}" varStatus="s">
        "${fn:escapeXml(p.productionNo)}"<c:if test="${!s.last}">,</c:if>
      </c:forEach>
    ];
    const targets = [
      <c:forEach var="p" items="${productionList}" varStatus="s">
        ${p.prTarget}<c:if test="${!s.last}">,</c:if>
      </c:forEach>
    ];
    const completes = [
      <c:forEach var="p" items="${productionList}" varStatus="s">
        ${p.prCompleted}<c:if test="${!s.last}">,</c:if>
      </c:forEach>
    ];

    const ctx = document.getElementById('productionChart').getContext('2d');
    new Chart(ctx, {
      type: 'bar',
      data: {
        labels,
        datasets: [
          {
            label: '생산 목표량',
            data: targets,
            backgroundColor: 'rgba(54, 162, 235, 0.5)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1
          },
          {
            label: '생산 완료량',
            data: completes,
            backgroundColor: 'rgba(75, 192, 192, 0.5)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,     // ✅ 높이 고정
        scales: {
          y: { beginAtZero: true, title: { display: true, text: '수량' } },
          x: { title: { display: true, text: '생산번호' } }
        },
        plugins: {
          legend: { position: 'top' },
          title:  { display: true, text: '생산 목표량 vs 완료량' }
        }
      }
    });

  </script>
	<%-- fn 함수 사용 시 아래 태그라이브러리 추가 --%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
</body>
</html>
