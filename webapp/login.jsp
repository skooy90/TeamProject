<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>

<style>
/* 전체 페이지 기본 스타일 */
body {
	font-family: 'Malgun Gothic', sans-serif;
	display: flex;
	justify-content: center; /* 가로 중앙 정렬 */
	align-items: center; /* 세로 중앙 정렬 */
	height: 100vh;
	margin: 0;
	background-color: #f0f2f5;
}

/* 로그인 폼을 감싸는 컨테이너 */
.login-container {
	width: 360px;
	padding: 40px;
	background-color: #ffffff;
	border-radius: 10px;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
	text-align: center;
}

/* '로그인' 제목 스타일 */
h1 {
	margin-top: 0;
	margin-bottom: 30px;
	font-size: 24px;
	color: #333;
}

/* 입력 필드 (ID, PW) 공통 스타일 */
.input-field {
	width: 100%;
	padding: 12px;
	margin-bottom: 20px;
	border: 1px solid #ddd;
	border-radius: 5px;
	font-size: 16px;
	box-sizing: border-box; /* 패딩과 테두리를 너비에 포함 */
}

/* 로그인 버튼 스타일 */
.login-button {
	width: 100%;
	padding: 12px;
	border: none;
	border-radius: 5px;
	background-color: #007bff;
	color: white;
	font-size: 18px;
	font-weight: bold;
	cursor: pointer;
	transition: background-color 0.3s;
}

/* 버튼에 마우스를 올렸을 때 효과 */
.login-button:hover {
	background-color: #0056b3;
}
</style>
</head>
<body>

	<div class="login-container">
		<h1>로그인</h1>

		<%-- 
            로그인 정보를 서버로 보내기 위한 form 태그입니다.
            action 속성에는 로그인 처리를 담당할 URL을 지정합니다. (예: loginProcess.jsp)
            method="post"는 로그인 정보를 HTTP Body에 담아 안전하게 보내는 방식입니다.
        --%>
		<form action="${pageContext.request.contextPath}/login" method="post">
			<div>
				<input type="text" name="userID" class="input-field"
					placeholder="사원번호 입력 [ID]" required>
			</div>
			<div>
				<input type="password" name="userPW" class="input-field"
					placeholder="비밀번호 입력 [PW]" required>
			</div>
			<div>
				<button type="submit" class="login-button">로그인</button>
			</div>

			<c:if test="${not empty error}">
				<div style="margin-top: 12px; color: #e11d48; font-weight: 600;">${error}</div>
			</c:if>
		</form>
	</div>

</body>
</html>