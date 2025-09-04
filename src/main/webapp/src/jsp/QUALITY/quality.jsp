<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>품질 관리</title>
<link rel="stylesheet" href="css/style.css">
<!-- Bootstrap -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<!-- Chart.js -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<style>
/* 테이블 균일화 */
.table th, .table td {
	vertical-align: middle;
	white-space: nowrap;
	font-size: 0.9rem;
}

/* 각 열 폭 지정 */
.table th:nth-child(1), .table td:nth-child(1) {
	min-width: 80px;
} /* 검사번호 */
.table th:nth-child(2), .table td:nth-child(2) {
	min-width: 90px;
} /* 작업번호 */
.table th:nth-child(3), .table td:nth-child(3) {
	min-width: 90px;
} /* 제품코드 */
.table th:nth-child(4), .table td:nth-child(4) {
	min-width: 100px;
} /* 검사자 */
.table th:nth-child(5), .table td:nth-child(5) {
	min-width: 100px;
} /* 검사결과 */
.table th:nth-child(6), .table td:nth-child(6), .table th:nth-child(7),
	.table td:nth-child(7) {
	min-width: 100px;
	text-align: right; /* 양품/불량 수량 숫자는 오른쪽 정렬 */
}

.table th:nth-child(8), .table td:nth-child(8), .table th:nth-child(9),
	.table td:nth-child(9), .table th:nth-child(10), .table td:nth-child(10),
	.table th:nth-child(11), .table td:nth-child(11) {
	min-width: 120px;
}

.table th:nth-child(12), .table td:nth-child(12) {
	min-width: 100px;
	text-align: center;
}

.btn-quality-search, .btn-quality-register {
	display: inline-flex;
	align-items: center;
	justify-content: center;
	padding: 8px 20px;
	border-radius: 6px;
	font-size: 0.95rem;
	font-weight: 500;
	white-space: nowrap; /* 글씨 줄바꿈 방지 */
	border: none;
	transition: background-color 0.2s ease-in-out;
}

/* 검색 버튼 */
.btn-quality-search {
	background-color: #3498db;
	color: #fff;
}

.btn-quality-search:hover {
	background-color: #2980b9;
}

/* 등록 버튼 */
.btn-quality-register {
	background-color: #2ecc71;
	color: #fff;
}

.btn-quality-register:hover {
	background-color: #27ae60;
}
</style>

</head>
<body>
	<%-- header --%>
	<jsp:include page="header.jsp" />

	<div class="main-container d-flex">
		<%-- sidebar --%>
		<jsp:include page="sidebar.jsp" />

		<%-- 본문 영역 --%>
		<div class="content-area flex-grow-1 p-4">
			<div class="container">
				<h2 class="mb-4">품질 관리</h2>

				<!-- 검색 영역 -->
				<div class="d-flex mb-3">
					<input type="text" class="form-control me-2"
						placeholder="검사번호 또는 작업번호 검색">
					<button class="btn-quality-search">검색</button>
					<a href="quality_form.jsp" class="btn-quality-register ms-2">검사
						등록</a>
				</div>

				<!-- 검사 내역 테이블 -->
				<div class="table-responsive">
					<table class="table table-bordered text-center align-middle">
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
							<tr>
								<td>QC-001</td>
								<td>WK-001</td>
								<td>P-001</td>
								<td>김철수</td>
								<td class="text-success">양품</td>
								<td>480</td>
								<td>20</td>
								<td>2025-08-30</td>
								<td>2025-09-01 10:30</td>
								<td>2025-09-01</td>
								<td>2025-09-02</td>
								<td><a href="#" class="text-primary">수정</a> | <a href="#"
									class="text-danger">삭제</a></td>
							</tr>
							<tr>
								<td>QC-002</td>
								<td>WK-002</td>
								<td>M-101</td>
								<td>이영희</td>
								<td class="text-danger">불량</td>
								<td>110</td>
								<td>10</td>
								<td>2025-08-31</td>
								<td>2025-09-02 15:00</td>
								<td>2025-09-02</td>
								<td>2025-09-03</td>
								<td><a href="#" class="text-primary">수정</a> | <a href="#"
									class="text-danger">삭제</a></td>
							</tr>
							<tr>
								<td>QC-003</td>
								<td>WK-003</td>
								<td>R-505</td>
								<td>박민수</td>
								<td class="text-success">양품</td>
								<td>195</td>
								<td>5</td>
								<td>2025-09-01</td>
								<td>2025-09-03 09:20</td>
								<td>2025-09-03</td>
								<td>2025-09-03</td>
								<td><a href="#" class="text-primary">수정</a> | <a href="#"
									class="text-danger">삭제</a></td>
							</tr>
						</tbody>
					</table>
				</div>

				<!-- 차트 영역 -->
				<div class="row mt-5">
					<div class="col-md-6">
						<canvas id="qualityResultChart"></canvas>
					</div>
					<div class="col-md-6">
						<canvas id="defectChart"></canvas>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Chart.js -->
	<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
	<script>
		// 품질 검사 결과 분포 (양품 vs 불량)
		new Chart(document.getElementById('qualityResultChart'), {
			type : 'pie',
			data : {
				labels : [ '양품', '불량' ],
				datasets : [ {
					data : [ 785, 35 ],
					backgroundColor : [ '#4CAF50', '#F44336' ]
				} ]
			},
			options : {
				plugins : {
					title : {
						display : true,
						text : '검사 결과 분포'
					}
				}
			}
		});

		// 검사별 불량 수량
		new Chart(document.getElementById('defectChart'), {
			type : 'bar',
			data : {
				labels : [ 'QC-001', 'QC-002', 'QC-003' ],
				datasets : [ {
					label : '불량 수량',
					data : [ 20, 10, 5 ],
					backgroundColor : '#FF9800'
				} ]
			},
			options : {
				plugins : {
					title : {
						display : true,
						text : '검사별 불량 수량'
					}
				},
				scales : {
					y : {
						beginAtZero : true
					}
				}
			}
		});
	</script>
</body>
</html>
