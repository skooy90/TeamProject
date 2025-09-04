<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>제품 등록/수정</title>
<link rel="stylesheet" href="css/style.css"><!-- 전역 레이아웃 사용 -->
<style>
  /* 페이지 전용 스타일 (전역과 충돌되지 않게 별도 클래스 사용) */
  .form-card {
    max-width: 900px;
    width: 100%;
    margin: 20px auto;
    background-color: #fff;
    padding: 30px;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0,0,0,0.1);
    box-sizing: border-box;
  }
  .form-card h1 {
    text-align: center;
    margin-bottom: 25px;
    font-size: 2rem;
    font-weight: 700;
  }
  .form-group {
    display: flex;
    flex-direction: column;
    margin-bottom: 18px;
  }
  .form-group label {
    margin-bottom: 8px;
    font-weight: 600;
    color: #555;
  }
  .form-group input[type="text"],
  .form-group textarea {
    width: 100%;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 6px;
    font-size: 1rem;
    box-sizing: border-box;
    background: #fff;
  }
  .form-group input[readonly] { background:#e9ecef; cursor:not-allowed; }

  .button-group {
    display: flex;
    justify-content: center;
    gap: 12px;
    margin-top: 26px;
  }
  .btn {
    padding: 12px 28px;
    border: none;
    border-radius: 8px;
    font-size: 1rem;
    font-weight: 700;
    cursor: pointer;
    transition: transform .06s ease, opacity .2s ease;
  }
  .btn:active { transform: translateY(1px); }
  .btn-success { background:#2ecc71; color:#fff; }
  .btn-success:hover { opacity:.9; }
  .btn-secondary { background:#6c757d; color:#fff; }
  .btn-secondary:hover { opacity:.9; }
  .btn-danger { background:#dc3545; color:#fff; }
  .btn-danger:hover { opacity:.9; }
</style>
</head>
<body>
  <!-- 전역 고정 헤더 -->
  <jsp:include page="header.jsp" />

  <!-- 전역 레이아웃 래퍼: sidebar(고정) + content-area(스크롤) -->
  <div class="main-container">
    <jsp:include page="sidebar.jsp" />

    <div class="content-area">
      <div class="form-card">
        <%
          String productCode = request.getParameter("productCode");
          String title = (productCode != null && !productCode.isEmpty()) ? "제품 수정" : "제품 등록";
        %>
        <h1><%= title %></h1>

        <form action="#" method="post">
          <div class="form-group">
            <label for="productCode">제품 코드</label>
            <input type="text" id="productCode" name="productCode"
                   value="<%=(productCode != null ? productCode : "")%>"
                   <%= (productCode != null ? "readonly" : "") %> required />
          </div>

          <div class="form-group">
            <label for="productName">제품 이름</label>
            <input type="text" id="productName" name="productName" required />
          </div>

          <div class="form-group">
            <label for="productType">제품 유형</label>
            <input type="text" id="productType" name="productType" />
          </div>

          <div class="form-group">
            <label for="quantity">제품 갯수</label>
            <input type="text" id="quantity" name="quantity" />
          </div>

          <div class="form-group">
            <label for="unit">제품 단위</label>
            <input type="text" id="unit" name="unit" />
          </div>

          <div class="form-group">
            <label for="description">제품 설명</label>
            <textarea id="description" name="description" rows="5"></textarea>
          </div>

          <div class="button-group">
            <button type="submit" class="btn btn-success">저장</button>
            <button type="button" class="btn btn-secondary" onclick="history.back()">취소</button>
            <%
              if (productCode != null && !productCode.isEmpty()) {
            %>
            <button type="button" class="btn btn-danger"
                    onclick="alert('삭제 기능은 나중에 구현될 예정입니다.')">삭제</button>
            <% } %>
          </div>
        </form>
      </div>
    </div>
  </div>
</body>
</html>
