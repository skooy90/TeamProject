<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>작업 ${empty work ? '등록' : '수정'}</title>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/src/Header_Side/style.css">
<style>
.form-card {
	max-width: 820px;
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

.help {
	color: #64748b;
	font-size: 13px
}
</style>
</head>
<body>
	<jsp:include page="../../Header_Side/header.jsp" />
	<div class="main-container">
		<jsp:include page="../../Header_Side/sidebar.jsp" />
		<div class="content-area">
			<div class="form-card">

				<!-- 제목 -->
				<h1>작업 ${empty work ? "등록" : "수정"}</h1>

				<form method="post"
					action="${pageContext.request.contextPath}${empty work ? '/work/insert' : '/work/update'}">
					<div class="form-grid">



						<!-- 수정일 때만 실제 전송 -->
						<c:if test="${not empty work}">
							<input type="hidden" name="workNo" value="${work.workNo}" />
						</c:if>

						<!-- 이하 기존 필드들 그대로 -->

						<label for="productionNo">생산번호</label> <select id="productionNo"
							name="productionNo" required class="form-control">
							<option value="" disabled selected>선택하세요</option>
							<c:forEach var="p" items="${productions}">
								<option value="${p.productionNo}" data-std="${p.standardCode}"
									data-name="${p.stName}">${p.productionNo}—${p.stName}
									(${p.standardCode})</option>
							</c:forEach>
						</select> <label for="productName">제품명</label> <input id="productName"
							class="form-control" type="text" readonly
							placeholder="생산번호 선택 시 자동표시"> <input type="hidden"
							id="standardCode" name="standardCode"> <label
							for="employeeNo">담당자 사번</label> <input id="employeeNo"
							name="employeeNo" type="text"
							value="${empty work ? '' : work.employeeNo}" required /> <label
							for="woSchedule">작업 일정</label> <input id="woSchedule"
							name="woSchedule" type="date"
							value="<fmt:formatDate value='${work.woSchedule}' pattern='yyyy-MM-dd'/>" />

						<label for="woQuantity">작업량</label> <input id="woQuantity"
							name="woQuantity" value="${targetFromServer}" readonly> <input
							type="hidden" id="standardCode" name="standardCode"> <label
							for="woCompleted">작업 완료량</label> <input id="woCompleted"
							name="woCompleted">
					</div>

					<div class="actions">
						<button type="submit" class="btn btn-primary">저장</button>
						<a class="btn btn-secondary"
							href="${pageContext.request.contextPath}/workList">취소</a>
					</div>
				</form>

			</div>
		</div>
	</div>
	<script>
	  const sel  = document.getElementById('productionNo');
	  const $qty = document.getElementById('woQuantity');
	  const $std = document.getElementById('standardCode');

	  function applyFromProduction() {
	    const opt = sel.options[sel.selectedIndex];
	    $qty.value = opt?.dataset.target ?? 0;   // 작업량 ← 생산목표량
	    $std.value = opt?.dataset.standard ?? '';
	  }
	  sel.addEventListener('change', applyFromProduction);
	  applyFromProduction();
</script>
</body>
</html>