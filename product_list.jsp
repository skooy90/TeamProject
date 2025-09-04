<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>제품 목록 - 기준관리</title>
<link rel="stylesheet" href="css/style.css">
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
</style>
</head>
<body>
	 <jsp:include page="header.jsp" />
    <div class="main-container">
        <jsp:include page="sidebar.jsp" />
        <div class="content-area">
			<div class="container">
				<h1>제품 목록</h1>
				<div class="controls">
					<div class="search-form">
						<input type="text" class="search-input" placeholder="제품 코드 또는 이름 검색">
						<button type="button" class="btn btn-primary">검색</button>
					</div>
					<a href="product_form.jsp" class="btn btn-success">제품 등록</a>
				</div>
				<table>
					<thead>
						<tr>
							<th>제품 코드</th>
							<th>제품 이름</th>
							<th>제품 유형</th>
							<th>제품 갯수</th>
							<th>제품 단위</th>
							<th>수정일</th>
							<th>관리</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>P-001</td>
							<td>MES 시스템 모듈</td>
							<td>완제품</td>
							<td>50</td>
							<td>개</td>
							<td>2023-10-26</td>
							<td class="action-links"><a href="#">수정</a><a href="#">삭제</a></td>
						</tr>
						<tr>
							<td>M-101</td>
							<td>컨베이어 벨트 모터</td>
							<td>부품</td>
							<td>120</td>
							<td>개</td>
							<td>2023-10-25</td>
							<td class="action-links"><a href="#">수정</a><a href="#">삭제</a></td>
						</tr>
						<tr>
							<td>R-505</td>
							<td>알루미늄 판재</td>
							<td>원자재</td>
							<td>1000</td>
							<td>kg</td>
							<td>2023-10-24</td>
							<td class="action-links"><a href="#">수정</a><a href="#">삭제</a></td>
						</tr>
					</tbody>
				</table>
				</div>
			</div>
		</div>
	</body>
</html>