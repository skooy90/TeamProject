<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

		<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>비밀번호 변경</title>
  <style>
    body {
      margin: 0;
      font-family: "Noto Sans KR", sans-serif;
      background-color: #f4f6f9;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
    }

    .container {
      background: #fff;
      padding: 30px;
      border-radius: 8px;
      box-shadow: 0 2px 6px rgba(0,0,0,0.1);
      width: 350px;
      text-align: center;
    }

    h2 {
      margin-bottom: 20px;
    }

    label {
      display: block;
      text-align: left;
      margin: 10px 0 5px;
      font-weight: bold;
    }

    input {
      width: 100%;
      padding: 10px;
      border: 1px solid #ccc;
      border-radius: 4px;
      margin-bottom: 15px;
    }

    button {
      width: 100%;
      padding: 12px;
      background: #3498db;
      color: #fff;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 16px;
    }

    button:hover {
      background: #2980b9;
    }

    .back-link {
      display: block;
      margin-top: 10px;
      font-size: 14px;
      color: #3498db;
      text-decoration: none;
    }

    .back-link:hover {
      text-decoration: underline;
    }
  </style>
</head>
<body>
  <div class="container">
    <h2>비밀번호 변경</h2>
    <form id="pwForm">
      <label>현재 비밀번호</label>
      <input type="password" id="currentPw" placeholder="현재 비밀번호 입력">

      <label>새 비밀번호</label>
      <input type="password" id="newPw" placeholder="새 비밀번호 입력">

      <label>새 비밀번호 확인</label>
      <input type="password" id="confirmPw" placeholder="새 비밀번호 확인">

      <button type="submit">변경하기</button>
    </form>

    <!-- 이동 링크 2개 -->
    <a href="23_Mypage.html" class="back-link">← 마이페이지로 돌아가기</a>
    <a href="25_login.html" class="back-link">→ 로그인 페이지로 가기</a>
    <a href="22_manager.html" class="back-link">→ 관리자 페이지로 가기</a>
  </div>

  <script>
  document.getElementById("pwForm").addEventListener("submit", function(e) {
    e.preventDefault();

    const currentPw = document.getElementById("currentPw").value;
    const newPw = document.getElementById("newPw").value;
    const confirmPw = document.getElementById("confirmPw").value;

    if (newPw.length === 0 || confirmPw.length === 0) {
      alert("새 비밀번호를 입력하세요.");
      return;
    }

    if (newPw !== confirmPw) {
      alert("새 비밀번호가 일치하지 않습니다.");
      return;
    }

    if (newPw.length > 20) {
      alert("비밀번호는 최대 20자리까지만 가능합니다.");
      return;
    }

    // 👉 localStorage에 새 비밀번호 저장 (연습용)
    localStorage.setItem("userPw", newPw);

    alert("비밀번호가 성공적으로 변경되었습니다!");
    window.location.href = "25_login.html"; // 로그인 페이지로 이동
  });
</script>

</body>
</html>
		
		
</body>
</html>