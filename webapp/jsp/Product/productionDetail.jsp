<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
<h1>���� �� ����</h1>
<table>
  <tr><th>�����ȣ</th><td>${prod.productionNo}</td></tr>
  <tr><th>��ǰ�ڵ�</th><td>${prod.standardCode}</td></tr>
  <tr><th>��ǰ��</th><td>${prod.stName}</td></tr>
  <tr><th>���� ������</th><td>${prod.prStart}</td></tr>
  <tr><th>���� ������</th><td>${prod.prEnd}</td></tr>
  <tr><th>���� ��ǥ��</th><td>${prod.prTarget}</td></tr>
  <tr><th>���� �Ϸᷮ</th><td>${prod.prCompleted}</td></tr>
  <tr><th>�����</th><td>${prod.employeeNo}</td></tr>
  <tr><th>������</th><td>${prod.createDate}</td></tr>
  <tr><th>������</th><td>${prod.updateDate}</td></tr>
</table>
<a href="${ctx}/Product/production_list.jsp" class="btn btn-secondary">�������</a>

</body>
</html>