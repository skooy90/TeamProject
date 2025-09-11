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
   <link rel="stylesheet" href="${pageContext.request.contextPath}/src/Header_Side/style.css">
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

						<!-- 작업번호: 신규는 시퀀스 미리보기, 수정은 실제 번호 -->
						<label>작업번호</label> <input type="text"
							value="${empty work ? (empty nextNo ? '자동 생성' : nextNo) : work.workNo}"
							readonly />

						<!-- 수정일 때만 실제 전송 -->
						<c:if test="${not empty work}">
							<input type="hidden" name="workNo" value="${work.workNo}" />
						</c:if>

						<!-- 이하 기존 필드들 그대로 -->
						<label for="productionNo">생산번호</label> <input id="productionNo"
							name="productionNo" type="text"
							value="${empty work ? '' : work.productionNo}" required /> <label
							for="standardCode">제품코드</label> <input id="standardCode"
							name="standardCode" type="text"
							value="${empty work ? '' : work.standardCode}" required /> <label
							for="employeeNo">담당자 사번</label> <input id="employeeNo"
							name="employeeNo" type="text"
							value="${empty work ? '' : work.employeeNo}" required /> <label
							for="woSchedule">작업 일정</label> <input id="woSchedule"
							name="woSchedule" type="date"
							value="<fmt:formatDate value='${work.woSchedule}' pattern='yyyy-MM-dd'/>" />

						<label for="woQuantity">작업량</label> <input id="woQuantity"
							name="woQuantity" type="number" min="0" step="1"
							value="${empty work ? 0 : work.woQuantity}" required /> <label
							for="woStatus">작업 상태</label> <select id="woStatus"
							name="woStatus">
							<option ${empty work || work.woStatus=='대기' ? 'selected' : ''}>대기</option>
							<option ${work.woStatus=='진행중' ? 'selected' : ''}>진행중</option>
							<option ${work.woStatus=='완료' ? 'selected' : ''}>완료</option>
						</select> <label for="woCompleted">작업 완료량</label> <input id="woCompleted"
							name="woCompleted" type="number" min="0" step="1"
							value="${empty work ? 0 : work.woCompleted}" /> <label
							for="woStart">작업 시작일</label> <input id="woStart" name="woStart"
							type="datetime-local"
							value="${not empty work.woStart ? fn:replace(fn:substring(work.woStart,0,16),' ','T') : ''}" />

						<label for="woEnd">작업 종료일</label> <input id="woEnd" name="woEnd"
							type="datetime-local"
							value="${not empty work.woEnd ? fn:replace(fn:substring(work.woEnd,0,16),' ','T') : ''}" />
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
</body>
</html>
