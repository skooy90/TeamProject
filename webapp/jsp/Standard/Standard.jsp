<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<title>제품 등록</title>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx}/src/Header_Side/style.css">
<style>
.form-card{max-width:900px;margin:24px auto;background:#fff;padding:28px;border-radius:12px;box-shadow:0 6px 16px rgba(0,0,0,.08)}
.form-card h1{text-align:center;margin:0 0 18px}
.form-group{display:flex;flex-direction:column;gap:8px;margin-bottom:16px}
.form-group input,.form-group select,.form-group textarea{padding:10px 12px;border:1px solid #d8dde3;border-radius:8px;font-size:14px}
.hint{font-size:12px;color:#64748b}
.btns{display:flex;gap:10px;justify-content:center;margin-top:18px}
.btn{padding:10px 16px;border:0;border-radius:8px;font-weight:700;cursor:pointer}
.btn-primary{background:#1677ff;color:#fff}
.btn-secondary{background:#6b7280;color:#fff}
</style>
</head>
<body>
  <jsp:include page="../../Header_Side/header.jsp"/>
  <div class="main-container">
    <jsp:include page="../../Header_Side/sidebar.jsp"/>
    <div class="content-area">
      <div class="form-card">
        <h1>제품 등록/수정</h1>

        <form method="post" action="${ctx}/standard/insert">
          <!-- 제품코드 입력칸은 제거합니다 (서버에서 자동 생성) -->
          <div class="form-group">
            <label for="productType">제품 유형</label>
            <select id="productType" name="productType" required>
              <option value="" disabled selected>선택하세요</option>
              <option value="RAW">RAW (원자재)</option>
              <option value="SEMI">SEMI (반제품)</option>
              <option value="FINISH">FINISH (완제품)</option>
            </select>
            <div class="hint">저장 시 코드가 자동 생성됩니다. 예상 코드: <b id="codePreview">-</b></div>
          </div>

          <div class="form-group">
            <label for="productName">제품 이름</label>
            <input id="productName" name="productName" type="text" required>
          </div>

       <!--    <div class="form-group">
            <label for="quantity">제품 갯수</label>
            <input id="quantity" name="quantity" type="number" min="0" step="1" value="0" required>
          </div> -->

          <div class="form-group">
            <label for="unit">제품 단위</label>
            <select id="unit" name="unit" required>
              <option value="" disabled selected>선택하세요</option>
              <option value="kg">kg</option>
              <option value="개">개</option>
              <option value="L">L</option>
            </select>
          </div>

          <div class="form-group">
            <label for="desc">제품 설명 (선택)</label>
            <textarea id="desc" rows="5" placeholder="필요 시 간단 메모를 남겨주세요."></textarea>
          </div>

          <div class="btns">
            <button class="btn btn-primary" type="submit">저장</button>
            <a class="btn btn-secondary" href="${ctx}/standardList">취소</a>
          </div>
        </form>
      </div>
    </div>
  </div>

<script>
// 시퀀스를 소모하지 않는 안전한 미리보기(접두사만)
const $type = document.getElementById('productType');
const $preview = document.getElementById('codePreview');

function prefixOf(t){
  if(!t) return '';
  t = t.toUpperCase();
  if(t==='RAW') return 'RA';
  if(t==='SEMI') return 'SE';
  if(t==='FINISH') return 'FI';
  return 'RA';
}
$type.addEventListener('change', e=>{
  const p = prefixOf(e.target.value);
  $preview.textContent = p ? `${p}#### (저장 시 실제 번호 부여)` : '-';
});
</script>
</body>
</html>
