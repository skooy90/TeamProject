<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>제품 등록/수정</title>
<link rel="stylesheet" href="css/style.css">
<style>
    body, html {
        margin: 0;
        padding: 0;
        height: 100%;
        font-family: Arial, sans-serif;
        background-color: #f4f7f9;
        color: #333;
    }

    .page-container {
        display: flex;
        height: 100%;
    }

    .main-content-wrapper {
        flex-grow: 1;
        display: flex;
        flex-direction: column;
    }

    .container {
        max-width: 900px; /* 너비를 더 넓게 확장 */
        width: 100%;
        margin: 20px auto;
        background-color: #ffffff;
        padding: 30px;
        border-radius: 8px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        box-sizing: border-box; /* 패딩이 너비에 포함되도록 설정 */
    }

    h1 {
        text-align: center;
        margin-bottom: 25px;
        font-size: 2em;
        font-weight: bold;
    }

    /* 폼 그룹 레이아웃을 Flexbox로 변경 */
    .form-group {
        display: flex;
        flex-direction: column;
        margin-bottom: 20px;
    }

    label {
        margin-bottom: 8px;
        font-weight: bold;
        color: #555;
    }

    input[type="text"], textarea {
        width: 100%;
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 4px;
        box-sizing: border-box;
        font-size: 1em;
    }

    input[readonly] {
        background-color: #e9ecef;
        cursor: not-allowed;
    }

    .button-group {
        display: flex;
        justify-content: center; /* 버튼 그룹을 중앙 정렬 */
        gap: 15px; /* 버튼 사이 간격 추가 */
        margin-top: 30px;
    }

    .btn {
        padding: 12px 30px; /* 버튼 크기 확장 */
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 1em;
        font-weight: bold;
        transition: background-color 0.3s ease;
    }
    
    .btn-success { background-color: #28a745; color: white; }
    .btn-secondary { background-color: #6c757d; color: white; }
    .btn-danger { background-color: #dc3545; color: white; }
    .btn:hover { opacity: 0.9; }

    /* 사이드바와 헤더 스타일 (이전 답변에서 제공) */
    .sidebar { width: 250px; background-color: #2c3e50; color: white; padding-top: 20px; }
    .header { height: 60px; background-color: #ffffff; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); display: flex; align-items: center; justify-content: space-between; padding: 0 20px; box-sizing: border-box; }
    .header .user-info a { margin-left: 10px; color: #007bff; text-decoration: none; }
</style>
</head>
<body>
	<div class="page-container">
		<jsp:include page="sidebar.jsp" />
		<div class="main-content-wrapper">
			<jsp:include page="header.jsp" />
			<div class="container">
			
				<%
				String productCode = request.getParameter("productCode");
				String title = (productCode != null && !productCode.isEmpty()) ? "제품 수정" : "제품 등록";
				%>
				
				<h1><%=title%></h1>
				<form action="#" method="post">
					<div class="form-group">
						<label for="productCode">제품 코드</label> <input type="text"
							id="productCode" name="productCode"
							value="<%=(productCode != null ? productCode : "")%>"
							<%=(productCode != null ? "readonly" : "")%> required>
					</div>
					<div class="form-group">
						<label for="productName">제품 이름</label> <input type="text"
							id="productName" name="productName" value="" required>
					</div>
					<div class="form-group">
						<label for="productType">제품 유형</label> <input type="text"
							id="productType" name="productType" value="">
					</div>
					<div class="form-group">
						<label for="quantity">제품 갯수</label> <input type="text"
							id="quantity" name="quantity" value="">
					</div>
					<div class="form-group">
						<label for="unit">제품 단위</label> <input type="text" id="unit"
							name="unit" value="">
					</div>
					<div class="form-group">
						<label for="description">제품 설명</label>
						<textarea id="description" name="description" rows="5"></textarea>
					</div>
					<div class="button-group">
						<button type="submit" class="btn btn-success">저장</button>
						<button type="button" class="btn btn-secondary"
							onclick="history.back()">취소</button>
						<%
						if (productCode != null && !productCode.isEmpty()) {
						%>
						<button type="button" class="btn btn-danger"
							onclick="alert('삭제 기능은 나중에 구현될 예정입니다.')">삭제</button>
						<%
						}
						%>
					</div>
				</form>
			</div>
</body>
</html>