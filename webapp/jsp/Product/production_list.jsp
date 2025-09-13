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
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/src/Header_Side/style.css">
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
	width: 110px;
} /* 생산번호 */
th:nth-child(2), td:nth-child(2) {
	width: 100px;
} /* 제품코드 */
th:nth-child(3), td:nth-child(3) {
	width: 140px;
} /* 제품명 */
th:nth-child(4), td:nth-child(4), th:nth-child(5), td:nth-child(5) {
	width: 120px;
} /* 시작/종료일 */
th:nth-child(6), td:nth-child(6), th:nth-child(7), td:nth-child(7) {
	width: 110px;
} /* 목표/완료량 */
th:nth-child(8), td:nth-child(8) {
	width: 90px;
} /* 담당자 */
th:nth-child(9), td:nth-child(9), th:nth-child(10), td:nth-child(10) {
	width: 110px;
} /* 생성/수정일 */
th:nth-child(11), td:nth-child(11) {
	width: 140px;
} /* 관리 */

/* 액션 링크 */
.action-links {
	white-space: nowrap;
}

.action-links a {
	color: #1677ff;
	text-decoration: none;
	margin-right: 8px;
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
					<form class="search-form" action="${ctx}/productionList"
						method="get">
						<input type="text" class="search-input" name="q"
							placeholder="생산번호, 제품코드, 제품명 검색">
						<button type="submit" class="btn btn-primary">검색</button>
					</form>
					<a href="${ctx}/production/form" class="btn btn-success">생산 등록</a>
				</div>

				<!-- 테이블 -->
				<div class="table-responsive">
					<table>
						<thead>
							<tr>
								<th>생산번호</th>
								<th>제품코드</th>
								<th>제품명</th>
								<th>생산 시작일</th>
								<th>생산 목표량</th>
								<th>생산 완료량</th>
								<th>담당자</th>
								<th>관리</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${productionList}">
								<tr>
									<td>${item.productionNo}</td>
									<td>${item.standardCode}</td>
									<td><a href="#" class="js-prod-detail"
										data-no="${item.productionNo}"> ${item.stName} </a></td>
									<td>${item.prStart}</td>
									<td>${item.prTarget}</td>
									<td>${item.prCompleted}</td>
									<td>${item.employeeNo}</td>
									<td class="action-links"><a
										href="${ctx}/production/update?no=${item.productionNo}">수정</a>
										<a href="${ctx}/production/delete?no=${item.productionNo}">삭제</a>
									</td>
								</tr>
							</c:forEach>
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

    (function(){
    	  const dlg   = document.getElementById('prodDlg');
    	  const frame = document.getElementById('prodFrame');
    	  const close = document.getElementById('dlgClose');
    	  const ctx   = '${pageContext.request.contextPath}'; // JSP에서 컨텍스트 경로

    	  document.addEventListener('click', (e)=>{
    	    const a = e.target.closest('.js-prod-detail');
    	    if(!a) return;
    	    e.preventDefault();
    	    frame.src = ctx + '/production/detail?no=' + encodeURIComponent(a.dataset.no);
    	    dlg.showModal();
    	  });

    	  close.addEventListener('click', ()=> dlg.close());
    	})();
    
  </script>
	<%-- fn 함수 사용 시 아래 태그라이브러리 추가 --%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
	<style>
  dialog.prod-dlg { border:0; padding:0; border-radius:12px; width:min(900px, 92vw); }
  .dlg-head { display:flex; justify-content:space-between; align-items:center; padding:12px 14px; border-bottom:1px solid #eee; font-weight:700;}
  .dlg-body { width:min(900px, 92vw); height:min(70vh, 620px); }
  #prodFrame { width:100%; height:100%; border:0; }
  .dlg-close { background:#fff; border:1px solid #ddd; border-radius:8px; padding:6px 10px; cursor:pointer; }
  dialog::backdrop { background:rgba(0,0,0,.35); }
</style>

<dialog id="prodDlg" class="prod-dlg">
  <div class="dlg-head">
    <span>생산 상세</span>
    <button type="button" id="dlgClose" class="dlg-close">닫기</button>
  </div>
  <div class="dlg-body">
    <iframe id="prodFrame" src=""></iframe>
  </div>
</dialog>
	
</body>
</html>
