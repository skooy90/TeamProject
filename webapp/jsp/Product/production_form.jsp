<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="ko">

<head>
<meta charset="UTF-8">
<title>생산 ${empty prod ? '등록' : '수정'}</title>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/src/Header_Side/style.css">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>


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
	margin: 0 0 18px;
	text-align: center
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

				<h1>생산 ${empty prod ? '등록' : '수정'}</h1>

				<c:if test="${not empty error}">
					<div
						style="margin-bottom: 12px; padding: 12px; border-radius: 8px; background: #fff1f0; color: #cf1322">
						${error}</div>
				</c:if>

				<form method="post"
					action="${pageContext.request.contextPath}${empty prod ? '/production/insert' : '/production/update'}">
					<div class="form-grid">

						<!-- 수정 모드에서만 노출/전송 -->
						<!-- 수정 모드에서만 노출/전송 -->
						<c:if test="${not empty prod}">
							<label>생산번호</label>
							<input type="text" value="${prod.productionNo}" readonly>
							<input type="hidden" name="productionNo"
								value="${prod.productionNo}">
						</c:if>

						<c:choose>
							<%-- 등록 모드 --%>
							<c:when test="${empty prod}">
								<label for="standardCode">제품코드</label>
								<select id="standardCode" name="standardCode" required>
									<option value="" disabled selected>선택하세요</option>

									<optgroup label="SEMI">
										<c:forEach var="s" items="${standards}">
											<c:if test="${s.stType eq 'SEMI'}">
												<option value="${s.standardCode}">
													${s.standardCode} — ${s.stName}</option>
											</c:if>
										</c:forEach>
									</optgroup>

									<optgroup label="FINISH">
										<c:forEach var="s" items="${standards}">
											<c:if test="${s.stType eq 'FINISH'}">
												<option value="${s.standardCode}">
													${s.standardCode} — ${s.stName}</option>
											</c:if>
										</c:forEach>
									</optgroup>
								</select>
							</c:when>

							<%-- 수정 모드: 화면에는 안 보이게, 값만 유지 --%>
							<c:otherwise>
								<input type="hidden" name="standardCode"
									value="${prod.standardCode}">
							</c:otherwise>
						</c:choose>

						<label for="employeeNo">담당자 사번</label> <input id="employeeNo"
							name="employeeNo" type="text"
							value="${empty prod ? '' : prod.employeeNo}" required
							placeholder="예) K0001"> <label for="prStart">생산
							시작일</label> <input id="prStart" name="prStart" type="date"
							value="<fmt:formatDate value='${prod.prStart}' pattern='yyyy-MM-dd'/>"
							required> <label for="prEnd">생산 종료일</label> <input
							id="prEnd" name="prEnd" type="date"
							value="<fmt:formatDate value='${prod.prEnd}' pattern='yyyy-MM-dd'/>"
							required> <label for="prTarget">생산 목표량</label> <input
							id="prTarget" name="prTarget" type="number" min="0" step="1"
							value="${empty prod ? 0 : prod.prTarget}" required>

					</div>

					<div class="actions">
						<button type="submit" class="btn btn-primary">저장</button>
						<a class="btn btn-secondary"
							href="${pageContext.request.contextPath}/productionList">취소</a>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
