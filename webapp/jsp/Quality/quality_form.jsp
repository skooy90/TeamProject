<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>검사 ${empty quality ? '등록' : '수정'}</title>
   <link rel="stylesheet" href="${pageContext.request.contextPath}/src/Header_Side/style.css">
<style>
.form-card {
	max-width: 780px;
	margin: 24px auto;
	background: #fff;
	padding: 28px;
	border-radius: 12px;
	box-shadow: 0 6px 16px rgba(0, 0, 0, .08)
}

.form-card h1 {
	text-align: center;
	margin: 0 0 18px
}

.form-grid {
	display: grid;
	grid-template-columns: 140px 1fr;
	gap: 12px 14px;
	align-items: center
}

.form-grid label {
	font-weight: 700;
	color: #333
}

.form-grid input, .form-grid select {
	padding: 10px 12px;
	border: 1px solid #d8dde3;
	border-radius: 8px;
	font-size: 14px
}

.actions {
	display: flex;
	gap: 10px;
	justify-content: center;
	margin-top: 18px
}

.btn {
	padding: 10px 16px;
	border: 0;
	border-radius: 8px;
	font-weight: 700;
	cursor: pointer
}

.btn-primary {
	background: #1677ff;
	color: #fff
}

.btn-secondary {
	background: #6b7280;
	color: #fff
}
</style>
</head>
<body>
    <jsp:include page="../../Header_Side/header.jsp" />
	<div class="main-container">
        <jsp:include page="../../Header_Side/sidebar.jsp" />
		<div class="content-area">
			<div class="form-card">
				<h1>검사 ${empty quality ? '등록' : '수정'}</h1>


				<form method="post"
					action="${pageContext.request.contextPath}${empty quality ? '/quality/insert' : '/quality/update'}">
					<div class="form-grid">
						<c:if test="${not empty quality}">
							<label>검사번호</label>
							<input type="text" value="${quality.qualityNo}" readonly>
							<input type="hidden" name="qualityNo"
								value="${quality.qualityNo}">
						</c:if>

						<label for="workNo">작업번호</label> <input id="workNo" name="workNo"
							type="text" required
							value="${empty quality ? '' : quality.workNo}"
							placeholder="예) W0008"> <label for="standardCode">제품코드</label>
						<input id="standardCode" name="standardCode" type="text" required
							value="${empty quality ? '' : quality.standardCode}"
							placeholder="예) FI0014"> <label for="employeeNo">검사자
							사번</label> <input id="employeeNo" name="employeeNo" type="text" required
							value="${empty quality ? '' : quality.employeeNo}"
							placeholder="예) K0001"> <label for="quResult">검사
							결과</label> <select id="quResult" name="quResult">
							<option
								${empty quality || quality.quResult=='양품' ? 'selected' : ''}>양품</option>
							<option ${quality.quResult=='불량' ? 'selected' : ''}>불량</option>
						</select> <label for="quQuantity">양품 수량</label> <input id="quQuantity"
							name="quQuantity" type="number" min="0" step="1"
							value="${empty quality ? 0 : quality.quQuantity}" required>

						<label for="defectQuantity">불량 수량</label> <input
							id="defectQuantity" name="defectQuantity" type="number" min="0"
							step="1" value="${empty quality ? 0 : quality.defectQuantity}"
							required>

						<c:set var="mfDate" value="" />
						<c:set var="insDT" value="" />
						<c:if test="${not empty quality}">
							<!-- 제조일 yyyy-MM-dd -->
							<fmt:formatDate value="${quality.quManufactureDate}"
								pattern="yyyy-MM-dd" var="mfDate" />
							<!-- 검사일시 yyyy-MM-dd'T'HH:mm  (datetime-local용) -->
							<fmt:formatDate value="${quality.inspectionDate}"
								pattern="yyyy-MM-dd'T'HH:mm" var="insDT" />
						</c:if>

						<label for="quManufactureDate">제조일</label> <input type="date"
							id="quManufactureDate" name="quManufactureDate" value="${mfDate}"
							required /> <label for="inspectionDate">검사일시</label> <input
							type="datetime-local" id="inspectionDate" name="inspectionDate"
							step="60" value="${insDT}" />

					</div>

					<div class="actions">
						<button type="submit" class="btn btn-primary">저장</button>
						<a class="btn btn-secondary"
							href="${pageContext.request.contextPath}/qualityList">취소</a>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
l>
