<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>대시보드</title>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<link rel="stylesheet" href="${ctx}/src/Header_Side/style.css">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<style>
.container {
	max-width: 1200px; /* 가운데 라인 고정 */
	margin: 24px auto; /* 수평 중앙 정렬 */
	padding: 30px;
}

.page-title {
	text-align: center;
	margin: 8px 0 56px;
}

.grid {
	display: grid;
	grid-template-columns: repeat(3, 1fr);
	gap: 18px
}

.card {
	background: #fff;
	border: 1px solid #eef1f4;
	border-radius: 12px;
	box-shadow: 0 6px 16px rgba(0, 0, 0, .06);
	padding: 34px; /* 내부 여백 키우기 */
	min-height: 220px;
}

.card h3 {
	margin: 2px 0 10px;
	font-size: 18px
}

.row2 {
	display: grid;
	grid-template-columns: 1fr 1fr 2fr;
	gap: 18px;
	margin-top: 18px
}

.notice {
	list-style: none;
	margin: 0;
	padding: 0
}

.notice li {
	padding: 8px 2px;
	border-bottom: 1px dashed #e5e7eb
}

.big-number {
	font-size: 40px;
	font-weight: 800;
	text-align: center;
	margin-top: 30px
}

canvas {
	width: 100% !important;
	height: 160px !important
}

@media ( min-width : 1024px) {
	.card.notice {
		grid-column: span 2;
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

				<h1 class="page-title">대시보드</h1>

				<div class="dashboard-grid">

					<div class="grid">
						<div class="card">
							<h3>생산 목표량 vs 완료량</h3>
							<canvas id="prodTvc"></canvas>
						</div>
						<div class="card">
							<h3>금일 생산량</h3>
							<div class="big-number">
								<c:out value="${todayProduction}" />
								건
							</div>
						</div>
						<div class="card"
							style="padding: 16px; display: flex; gap: 16px; align-items: center;">
							<div style="width: 220px; height: 220px;">
								<canvas id="workProgressChart"></canvas>
							</div>

							<div style="display: flex; flex-direction: column; gap: 6px;">
								<div style="font-weight: 700; font-size: 18px;">작업 진행률</div>
								<div style="font-size: 32px; font-weight: 800;">
									<fmt:formatNumber value="${workRate}" type="number"
										maxFractionDigits="1" />
									%
								</div>
								<div style="color: #64748b;">
									총 작업량: <b>${workTotal}</b>
								</div>
								<div style="color: #64748b;">
									완료량: <b>${workDone}</b>
								</div>
								<div style="color: #64748b;">
									잔여량: <b>${workRemain}</b>
								</div>
							</div>
						</div>
					</div>

					<div class="row2">
						<div class="card">
							<h3>불량 수량 현황</h3>
							<canvas id="defectTop"></canvas>
						</div>
						<div class="card">
							<h3>불량률 현황</h3>
							<canvas id="defectRate"></canvas>
						</div>
						<div class="card">
							<h3>공지 사항</h3>
							<ul class="notice">
								<c:forEach var="n" items="${notices}">
									<li>${n}</li>
								</c:forEach>
							</ul>
						</div>
					</div>

				</div>
			</div>
		</div>
	</div>


	<script>
  // ----- 생산: Target vs Completed -----
  const pLabels = [
    <c:forEach var="m" items="${prodTvc}" varStatus="s">
      "${fn:escapeXml(m.no)}"<c:if test="${!s.last}">,</c:if>
    </c:forEach>
  ];
  const pTarget = [
    <c:forEach var="m" items="${prodTvc}" varStatus="s">
      ${m.t}<c:if test="${!s.last}">,</c:if>
    </c:forEach>
  ];
  const pComplete = [
    <c:forEach var="m" items="${prodTvc}" varStatus="s">
      ${m.c}<c:if test="${!s.last}">,</c:if>
    </c:forEach>
  ];
  new Chart(document.getElementById('prodTvc'),{
    type:'bar',
    data:{labels:pLabels,
          datasets:[{label:'목표',data:pTarget},
                    {label:'완료',data:pComplete}]},
    options:{responsive:true,maintainAspectRatio:false}
  });

  // ----- 작업: Qty vs Completed -----
  const wLabels = [
    <c:forEach var="m" items="${workQvc}" varStatus="s">
      "${fn:escapeXml(m.no)}"<c:if test="${!s.last}">,</c:if>
    </c:forEach>
  ];
  const wQty = [
    <c:forEach var="m" items="${workQvc}" varStatus="s">
      ${m.q}<c:if test="${!s.last}">,</c:if>
    </c:forEach>
  ];
  const wComp = [
    <c:forEach var="m" items="${workQvc}" varStatus="s">
      ${m.c}<c:if test="${!s.last}">,</c:if>
    </c:forEach>
  ];
  new Chart(document.getElementById('workQvc'),{
    type:'bar',
    data:{labels:wLabels,
          datasets:[{label:'작업량',data:wQty},
                    {label:'완료',data:wComp}]},
    options:{responsive:true,maintainAspectRatio:false}
  });

  // ----- 불량 수량 Top5 -----
  const dLabels = [
    <c:forEach var="m" items="${defectTop}" varStatus="s">
      "${fn:escapeXml(m.name)}"<c:if test="${!s.last}">,</c:if>
    </c:forEach>
  ];
  const dVals = [
    <c:forEach var="m" items="${defectTop}" varStatus="s">
      ${m.cnt}<c:if test="${!s.last}">,</c:if>
    </c:forEach>
  ];
  new Chart(document.getElementById('defectTop'),{
    type:'bar',
    data:{labels:dLabels,datasets:[{label:'불량 수량',data:dVals}]},
    options:{responsive:true,maintainAspectRatio:false,scales:{y:{beginAtZero:true}}}
  });

  // ----- 불량률 Top5 -----
  const rLabels = [
    <c:forEach var="m" items="${defectRateTop}" varStatus="s">
      "${fn:escapeXml(m.name)}"<c:if test="${!s.last}">,</c:if>
    </c:forEach>
  ];
  const rVals = [
    <c:forEach var="m" items="${defectRateTop}" varStatus="s">
      ${m.rate}<c:if test="${!s.last}">,</c:if>
    </c:forEach>
  ];
  new Chart(document.getElementById('defectRate'),{
    type:'line',
    data:{labels:rLabels,datasets:[{label:'불량률(%)',data:rVals}]},
    options:{responsive:true,maintainAspectRatio:false,scales:{y:{beginAtZero:true}}}
  });
  
  (function () {
	    const total  = ${workTotal};
	    const done   = ${workDone};
	    const remain = ${workRemain};
	    const rate   = ${workRate};

	    const centerText = {
	      id: 'centerText',
	      afterDraw(chart) {
	        const {ctx} = chart;
	        const p = chart.getDatasetMeta(0).data[0];
	        if (!p) return;
	        ctx.save();
	        ctx.font = '700 28px sans-serif';
	        ctx.fillStyle = '#111';
	        ctx.textAlign = 'center';
	        ctx.textBaseline = 'middle';
	        ctx.fillText((rate || 0).toFixed(1) + '%', p.x, p.y);
	        ctx.restore();
	      }
	    };

	    const canvas = document.getElementById('workProgressChart');
	    const c2d = canvas.getContext('2d');

	    new Chart(c2d, {
	      type: 'doughnut',
	      data: {
	        labels: ['완료', '잔여'],
	        datasets: [{
	          data: [done, Math.max(0, total - done)],
	          backgroundColor: ['rgba(75, 192, 192, 0.9)', 'rgba(229, 231, 235, 0.9)'],
	          borderColor: ['rgba(75, 192, 192, 1)', 'rgba(229, 231, 235, 1)'],
	          borderWidth: 1,
	          cutout: '68%'
	        }]
	      },
	      options: {
	        responsive: true,
	        maintainAspectRatio: false,
	        plugins: {
	          legend: { position: 'bottom' },
	          tooltip: {
	            callbacks: {
	              // ⬇⬇ 여기만 변경 ⬇⬇
	              label: function (c) {
	                var pct = total ? Math.round(c.parsed * 100 / total) : 0;
	                return c.label + ': ' + c.parsed + ' (' + pct + '%)';
	              }
	            }
	          }
	        }
	      },
	      plugins: [centerText]
	    });
	  })();
</script>
</body>
</html>
