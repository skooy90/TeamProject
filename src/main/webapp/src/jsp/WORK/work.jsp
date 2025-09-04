<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>작업관리</title>
<link rel="stylesheet" href="css/style.css">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<style>
.table-container {
	overflow-x: auto;
	margin-bottom: 20px;
	max-height: 400px; /* 필요 시 세로 스크롤 */
}

.container {
	max-width: 1000px;
	margin: 20px auto;
	padding: 30px;
	background-color: #ffffff;
	border-radius: 8px;
	box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
	overflow-x: auto; /* 테이블 잘림 방지 */
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
	font-size: 0.9em; /* 글자 크기 살짝 줄이기 */
}

th, td {
	padding: 8px;
	text-align: center;
	white-space: nowrap;
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

.charts-wrapper {
	display: flex;
	justify-content: space-between;
	flex-wrap: wrap;
	gap: 20px;
	margin-top: 20px;
}

.chart-container {
	flex: 1;
	max-width: 45%; /* 차트 크기 줄이기 */
	background: #fff;
	padding: 15px;
	border-radius: 8px;
	box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}
</style>
</head>
<body>
	<jsp:include page="header.jsp" />
	<div class="main-container">
		<jsp:include page="sidebar.jsp" />
		<div class="content-area">
			<div class="container">
				<h1>작업관리</h1>
				<div class="controls">
					<div class="search-form">
						<input type="text" class="search-input"
							placeholder="작업번호 또는 생산번호 검색">
						<button type="button" class="btn btn-primary">검색</button>
					</div>
					<a href="work_form.jsp" class="btn btn-success">작업 등록</a>
				</div>
				<div class="table-container">
					<table>
						<thead>
							<tr>
								<th>작업번호</th>
								<th>생산번호</th>
								<th>제품코드</th>
								<th>담당자</th>
								<th>작업 일정</th>
								<th>작업량</th>
								<th>작업 상태</th>
								<th>작업 완료량</th>
								<th>작업 시작일</th>
								<th>작업 종료일</th>
								<th>생성일</th>
								<th>수정일</th>
								<th>관리</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>WK-001</td>
								<td>PRD-001</td>
								<td>P-001</td>
								<td>김철수</td>
								<td>2025-09-01 ~ 2025-09-03</td>
								<td>50</td>
								<td>진행중</td>
								<td>30</td>
								<td>2025-09-01</td>
								<td>2025-09-03</td>
								<td>2025-08-30</td>
								<td>2025-09-02</td>
								<td class="action-links"><a href="#">수정</a><a href="#">삭제</a></td>
							</tr>
							<tr>
								<td>WK-002</td>
								<td>PRD-002</td>
								<td>M-101</td>
								<td>이영희</td>
								<td>2025-09-02 ~ 2025-09-05</td>
								<td>120</td>
								<td>대기</td>
								<td>0</td>
								<td>2025-09-02</td>
								<td>2025-09-05</td>
								<td>2025-08-31</td>
								<td>2025-09-03</td>
								<td class="action-links"><a href="#">수정</a><a href="#">삭제</a></td>
							</tr>
							<tr>
								<td>WK-003</td>
								<td>PRD-003</td>
								<td>R-505</td>
								<td>박민수</td>
								<td>2025-09-03 ~ 2025-09-06</td>
								<td>200</td>
								<td>진행중</td>
								<td>100</td>
								<td>2025-09-03</td>
								<td>2025-09-06</td>
								<td>2025-09-01</td>
								<td>2025-09-03</td>
								<td class="action-links"><a href="#">수정</a><a href="#">삭제</a></td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="charts-wrapper">
					<div class="chart-container">
						<canvas id="workAmountChart"></canvas>
					</div>
					<div class="chart-container">
						<canvas id="statusChart"></canvas>
					</div>
					<div class="chart-container">
						<canvas id="timelineChart"></canvas>
					</div>
				</div>


			</div>
		</div>
	</div>
	<script>
		const ctx = document.getElementById('workAmountChart').getContext('2d');
		const workChart = new Chart(ctx, {
			type : 'bar',
			data : {
				labels : [ 'WK-001', 'WK-002', 'WK-003' ],
				datasets : [ {
					label : '작업량',
					data : [ 50, 120, 200 ],
					backgroundColor : 'rgba(54, 162, 235, 0.5)',
					borderColor : 'rgba(54, 162, 235, 1)',
					borderWidth : 1
				}, {
					label : '작업 완료량',
					data : [ 30, 0, 100 ],
					backgroundColor : 'rgba(75, 192, 192, 0.5)',
					borderColor : 'rgba(75, 192, 192, 1)',
					borderWidth : 1
				} ]
			},
			options : {
				scales : {
					y : {
						beginAtZero : true,
						title : {
							display : true,
							text : '수량'
						}
					},
					x : {
						title : {
							display : true,
							text : '작업번호'
						}
					}
				},
				plugins : {
					legend : {
						position : 'top'
					},
					title : {
						display : true,
						text : '작업량 vs 완료량'
					}
				}
			}
		});

		const ctxStatus = document.getElementById('statusChart').getContext(
				'2d');
		const statusChart = new Chart(ctxStatus, {
			type : 'pie',
			data : {
				labels : [ '대기', '진행중', '완료' ],
				datasets : [ {
					label : '작업 상태 분포',
					data : [ 1, 2, 0 ], // 더미데이터 기준
					backgroundColor : [ 'rgba(255, 206, 86, 0.5)', // 대기
					'rgba(54, 162, 235, 0.5)', // 진행중
					'rgba(75, 192, 192, 0.5)' // 완료
					],
					borderColor : [ 'rgba(255, 206, 86, 1)',
							'rgba(54, 162, 235, 1)', 'rgba(75, 192, 192, 1)' ],
					borderWidth : 1
				} ]
			},
			options : {
				plugins : {
					title : {
						display : true,
						text : '작업 상태 분포'
					}
				}
			}
		});
		const ctxTimeline = document.getElementById('timelineChart')
				.getContext('2d');
		const timelineChart = new Chart(ctxTimeline, {
			type : 'bar',
			data : {
				labels : [ 'WK-001', 'WK-002', 'WK-003' ],
				datasets : [ {
					label : '작업 기간(일)',
					data : [ 3, 4, 4 ], // (종료일 - 시작일)
					backgroundColor : 'rgba(153, 102, 255, 0.5)',
					borderColor : 'rgba(153, 102, 255, 1)',
					borderWidth : 1
				} ]
			},
			options : {
				indexAxis : 'y', // 가로 막대
				scales : {
					x : {
						beginAtZero : true,
						title : {
							display : true,
							text : '일수'
						}
					},
					y : {
						title : {
							display : true,
							text : '작업번호'
						}
					}
				},
				plugins : {
					title : {
						display : true,
						text : '작업 일정 분포'
					}
				}
			}
		});
	</script>
</body>
</html>
