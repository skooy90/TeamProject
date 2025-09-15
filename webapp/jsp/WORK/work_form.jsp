<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="isEdit" value="${not empty work}" />
<title>작업 ${isEdit ? '수정' : '등록'}</title>

<link rel="stylesheet" href="${ctx}/src/Header_Side/style.css" />
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
</style>
</head>
<body>
	<jsp:include page="../../Header_Side/header.jsp" />
	<div class="main-container">
		<jsp:include page="../../Header_Side/sidebar.jsp" />
		<div class="content-area">
			<div class="form-card">

				<h1>작업 ${isEdit ? '수정' : '등록'}</h1>

				<form method="post"
					action="${ctx}${isEdit ? '/work/update' : '/work/insert'}">
					<div class="form-grid">

						<c:if test="${isEdit}">
							<label>작업번호</label>
							<input type="text" value="${work.workNo}" readonly />
							<input type="hidden" name="workNo" value="${work.workNo}" />
						</c:if>

						<label>생산번호</label>
						<c:choose>
							<c:when test="${isEdit}">
								<input type="text" value="${work.productionNo}" readonly />
								<input type="hidden" id="productionNo" name="productionNo"
									value="${work.productionNo}" />
							</c:when>

							<c:otherwise>
								<c:choose>
									<c:when test="${empty productions}">
										<div
											style="grid-column: 1/-1; color: #64748b; font-size: 13px;">
											등록 가능한 생산이 없습니다. 먼저 생산을 등록해 주세요.</div>
									</c:when>

									<c:otherwise>
										<select id="productionNo" name="productionNo" required>
											<option value="" disabled selected>선택하세요</option>
											<c:forEach var="p" items="${productions}">
												<option value="${p.productionNo}"
													data-standard="${p.standardCode}" data-name="${p.stName}"
													data-target="${p.prTarget}">${p.productionNo} —
													${p.stName} (${p.standardCode})</option>
											</c:forEach>
										</select>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>

						<label for="productName">제품명</label> <input id="productName"
							type="text" readonly
							value="${isEdit ? (empty work.stName ? '' : work.stName) : ''}"
							placeholder="생산번호 선택 시 자동표시" /> <input type="hidden"
							id="standardCode" name="standardCode"
							value="${isEdit ? work.standardCode : ''}" /> <label>담당자
							사번</label>
						<c:choose>
							<c:when test="${isEdit}">
								<input type="text" value="${work.employeeNo}" readonly />
								<input type="hidden" name="employeeNo"
									value="${work.employeeNo}" />
							</c:when>
							<c:otherwise>
								<input id="employeeNo" name="employeeNo" type="text"
									placeholder="예) K0001" required />
							</c:otherwise>
						</c:choose>

						<label for="woSchedule">작업 일정</label> <input id="woSchedule"
							name="woSchedule" type="date"
							value="<fmt:formatDate value='${work.woSchedule}' pattern='yyyy-MM-dd'/>" />

						<label for="woQuantity">작업량</label> <input id="woQuantity"
							name="woQuantity" type="number" min="0" step="1"
							value="${
			  isEdit
			    ? (empty targetFromServer ? 0 : targetFromServer)
			    : (empty targetFromServer ? 0 : targetFromServer)
			}"
							readonly /> <label for="woCompleted">작업 완료량</label> <input
							id="woCompleted" name="woCompleted"
							value="${isEdit ? work.woCompleted : 0}" />
					</div>

					<div class="actions">
						<button type="submit" class="btn btn-primary">저장</button>
						<a class="btn btn-secondary" href="${ctx}/workList">취소</a>
					</div>
				</form>

			</div>
		</div>
	</div>

	<script>
    (function () {
      const isEdit = ${isEdit ? 'true' : 'false'};

      // 등록 모드에서만 생산번호 선택 시 작업량/제품명 자동 채움
      if (!isEdit) {
        const sel  = document.getElementById('productionNo');
        if (!sel) return;                        // 생산목록이 없는 경우(안전가드)
        const qty  = document.getElementById('woQuantity');
        const std  = document.getElementById('standardCode');
        const name = document.getElementById('productName');

        function apply() {
          const opt = sel.options[sel.selectedIndex];
          if (!opt) return;
          qty.value  = opt.dataset.target   ?? 0;
          std.value  = opt.dataset.standard ?? '';
          name.value = opt.dataset.name     ?? '';
        }
        sel.addEventListener('change', apply);
      }
    })();
  </script>
</body>
</html>