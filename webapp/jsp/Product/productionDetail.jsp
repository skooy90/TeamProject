<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
<h1>생산 상세 정보</h1>
<table>
  <tr><th>생산번호</th><td>${prod.productionNo}</td></tr>
  <tr><th>제품코드</th><td>${prod.standardCode}</td></tr>
  <tr><th>제품명</th><td>${prod.stName}</td></tr>
  <tr><th>생산 시작일</th><td>${prod.prStart}</td></tr>
  <tr><th>생산 종료일</th><td>${prod.prEnd}</td></tr>
  <tr><th>생산 목표량</th><td>${prod.prTarget}</td></tr>
  <tr><th>생산 완료량</th><td>${prod.prCompleted}</td></tr>
  <tr><th>담당자</th><td>${prod.employeeNo}</td></tr>
  <tr><th>생성일</th><td>${prod.createDate}</td></tr>
  <tr><th>수정일</th><td>${prod.updateDate}</td></tr>
</table>
<a href="${ctx}/Product/production_list.jsp" class="btn btn-secondary">목록으로</a>

</body>
</html>