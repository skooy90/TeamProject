<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx}/Header_Side/style.css">

<nav class="sidebar">
  <ul>
    <li><a href="${ctx}/dashboard">대시보드</a></li>
    <li><a href="${ctx}/standardList">기준 관리</a></li>
    <li><a href="${ctx}/productionList">생산 관리</a></li>
    <li><a href="${ctx}/workList">작업 관리</a></li>
    <li><a href="${ctx}/qualityList">품질 관리</a></li>
    <li><a href="${ctx}/process">공정 관리</a></li>
    <li><a href="${ctx}/bom">BOM</a></li>
    <li><a href="${ctx}/material">자재 관리</a></li>
    <li><a href="${ctx}/userList">사용자 관리</a></li>
    <li><a href="${ctx}/boardList">게시판</a></li>
  </ul>
</nav>>