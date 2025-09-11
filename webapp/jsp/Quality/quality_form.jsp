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
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/src/Header_Side/style.css">
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
					<input type="hidden" name="standardCode" id="standardCode">
					<div class="form-grid">
						<c:if test="${not empty quality}">
							<label>검사번호</label>
							<input type="text" value="${quality.qualityNo}" readonly>
							<input type="hidden" name="qualityNo"
								value="${quality.qualityNo}">
						</c:if>
						
						<label>작업번호</label> <select id="workNo" name="workNo" required>
							<option value="" disabled selected>작업 선택</option>
							<c:forEach var="w" items="${workList}">
								<option value="${w.workNo}" data-stcode="${w.standardCode}"
									data-stname="${w.stName}" data-completed="${w.woCompleted}">
									${w.workNo} — ${w.stName} (완료 ${w.woCompleted})</option>
							</c:forEach>
						</select> <label>제품명</label> <input type="text" id="productName" readonly
							placeholder="작업 선택 시 자동 표시"> <label for="employeeNo">검사자
							사번</label> <input id="employeeNo" name="employeeNo" type="text" required
							value="${empty quality ? '' : quality.employeeNo}"
							placeholder="예) K0001"> <label for="quQuantity">양품
							수량</label> <input id="quQuantity" name="quQuantity" type="number" min="0"
							step="1" value="${empty quality ? 0 : quality.quQuantity}"
							required> <label for="defectQuantity">불량 수량</label> <input
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

						<label for="inspectionDate">검사일시</label> <input
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
	<script>
  const sel = document.getElementById('workNo');
  const nameEl = document.getElementById('productName');
  const codeEl = document.getElementById('standardCode');
  const goodEl = document.getElementById('goodQty');
  const badEl  = document.getElementById('badQty');

  let completed = 0;

  sel?.addEventListener('change', e => {
    const opt = sel.options[sel.selectedIndex];
    const stname = opt.getAttribute('data-stname') || '';
    const stcode = opt.getAttribute('data-stcode') || '';
    completed = parseInt(opt.getAttribute('data-completed') || '0', 10);

    nameEl.value = stname;
    codeEl.value = stcode;

    // 안내(placeholder) 및 간단한 상한 체크
    goodEl.placeholder = `0 ~ ${completed}`;
    badEl.placeholder  = `0 ~ ${completed}`;
    goodEl.max = completed;
    badEl.max  = completed;
  });

  function clamp() {
    const g = parseInt(goodEl.value || '0', 10);
    const b = parseInt(badEl.value  || '0', 10);
    if (g < 0) goodEl.value = 0;
    if (b < 0) badEl.value  = 0;
    if (g > completed) goodEl.value = completed;
    if (b > completed) badEl.value  = completed;
  }
  goodEl.addEventListener('input', clamp);
  badEl.addEventListener('input', clamp);
</script>
</body>
</html>
l>
