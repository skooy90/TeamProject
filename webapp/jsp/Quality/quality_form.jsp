<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <c:set var="ctx"    value="${pageContext.request.contextPath}" />
  <c:set var="isEdit" value="${not empty quality}" />
  <title>검사 ${isEdit ? '수정' : '등록'}</title>

  <link rel="stylesheet" href="${ctx}/src/Header_Side/style.css" />
  <style>
    .form-card{max-width:820px;margin:24px auto;background:#fff;padding:28px;border-radius:12px;box-shadow:0 6px 16px rgba(0,0,0,.08)}
    .form-card h1{text-align:center;margin:0 0 18px}
    .form-grid{display:grid;grid-template-columns:140px 1fr;gap:12px 14px;align-items:center}
    .form-grid label{font-weight:700;color:#333}
    .form-grid input,.form-grid select{padding:10px 12px;border:1px solid #d8dde3;border-radius:8px;font-size:14px}
    .actions{display:flex;gap:10px;justify-content:center;margin-top:18px}
    .btn{padding:10px 16px;border:0;border-radius:8px;font-weight:700;cursor:pointer}
    .btn-primary{background:#1677ff;color:#fff}
    .btn-secondary{background:#6b7280;color:#fff}
  </style>
</head>
<body>
  <jsp:include page="../../Header_Side/header.jsp" />
  <div class="main-container">
    <jsp:include page="../../Header_Side/sidebar.jsp" />
    <div class="content-area">
      <div class="form-card">

        <h1>검사 ${isEdit ? '수정' : '등록'}</h1>

        <form method="post" action="${ctx}${isEdit ? '/quality/update' : '/quality/insert'}">
          <div class="form-grid">

            <!-- 수정 모드: 검사번호 고정 표시 + hidden 전송 -->
            <c:if test="${isEdit}">
              <label>검사번호</label>
              <input type="text" value="${quality.qualityNo}" readonly />
              <input type="hidden" name="qualityNo" value="${quality.qualityNo}" />
            </c:if>

            <!-- 작업번호: 등록 시 선택, 수정 시 고정 -->
            <label>작업번호</label>
            <c:choose>
              <c:when test="${!isEdit}">
                <select id="workNo" name="workNo" required>
                  <option value="" disabled selected>작업 선택</option>
                  <c:forEach var="w" items="${works}">
                    <option value="${w.workNo}" data-name="${w.stName}">
                      ${w.workNo} — ${w.stName}
                    </option>
                  </c:forEach>
                </select>
              </c:when>
              <c:otherwise>
                <input type="text" value="${quality.workNo}" readonly />
                <input type="hidden" name="workNo" value="${quality.workNo}" />
              </c:otherwise>
            </c:choose>

            <!-- 제품명 표시(읽기전용, 서버전송 안함) -->
            <label for="productName">제품명</label>
            <input id="productName" type="text" readonly
                   value="${isEdit ? quality.stName : ''}"
                   placeholder="작업 선택 시 자동 표시" />

            <!-- 검사자 사번: 등록 시 입력, 수정 시 고정 -->
            <label>검사자 사번</label>
            <c:choose>
              <c:when test="${!isEdit}">
                <input id="employeeNo" name="employeeNo" type="text" placeholder="예) K0001" required />
              </c:when>
              <c:otherwise>
                <input type="text" value="${quality.employeeNo}" readonly />
                <input type="hidden" name="employeeNo" value="${quality.employeeNo}" />
              </c:otherwise>
            </c:choose>

            <!-- 양품/불량 수량: DTO 이름에 맞게 quQuantity / defectQuantity -->
            <label for="quQuantity">양품 수량</label>
            <input id="quQuantity" name="quQuantity" type="number" min="0" step="1"
                   value="${isEdit ? quality.quQuantity : 0}" />

            <label for="defectQuantity">불량 수량</label>
            <input id="defectQuantity" name="defectQuantity" type="number" min="0" step="1"
                   value="${isEdit ? quality.defectQuantity : 0}" />

            <!-- 검사일시(Timestamp -> datetime-local) -->
            <c:if test="${not empty quality.inspectionDate}">
              <fmt:formatDate value="${quality.inspectionDate}" pattern="yyyy-MM-dd'T'HH:mm" var="inspectionStr" />
            </c:if>
            <label for="inspectionDate">검사일시</label>
            <input id="inspectionDate" name="inspectionDate" type="datetime-local"
                   value="${empty inspectionStr ? '' : inspectionStr}" />

          </div>

          <div class="actions">
            <button type="submit" class="btn btn-primary">저장</button>
            <a href="${ctx}/qualityList" class="btn btn-secondary">취소</a>
          </div>
        </form>

      </div>
    </div>
  </div>

  <script>
    (function () {
      var isEdit = ${isEdit ? 'true' : 'false'};
      if (isEdit) return; // 등록 화면에서만 제품명 자동 채움

      var sel = document.getElementById('workNo');
      var nm  = document.getElementById('productName');
      if (!sel || !nm) return;

      sel.addEventListener('change', function () {
        var opt = sel.options[sel.selectedIndex];
        nm.value = opt ? (opt.dataset.name || '') : '';
      });
    })();
  </script>
</body>
</html>

