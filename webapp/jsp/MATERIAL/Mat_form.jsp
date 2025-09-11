<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>재고 등록/수정 - 자재관리</title>
    <c:set var="ctx" value="${pageContext.request.contextPath}" />
    
  <link rel="stylesheet" href="${ctx}/css/material_form.css">
  <link rel="stylesheet" href="${ctx}/src/Header_Side/style.css">
</head>
<body>
    <jsp:include page="../../Header_Side/header.jsp" />
    <div class="main-container">
        <jsp:include page="../../Header_Side/sidebar.jsp" />
        <div class="content-area">
            <div class="container">
                <h1>${mode == 'update' ? '재고 수정' : '재고 등록'}</h1>
                
               <!-- 정보 표시 섹션 -->
                <div class="info-section">
                    <div class="info-title">
                        ${mode == 'update' ? '수정 모드' : '등록 모드'}
                    </div>
                    <div class="info-content">
                        ${mode == 'update' ? 
                            '기존 재고 정보를 수정합니다. 재고코드는 변경할 수 없습니다.' : 
                            '새로운 재고를 등록합니다. 재고코드는 자동으로 생성됩니다.'}
                    </div>
                </div>
                
                <div class="form-container">
                    <form id="materialForm" method="post" action="${pageContext.request.contextPath}/material/${mode == 'update' ? 'update' : 'insert'}">
                        <!-- 재고코드 -->
                        <div class="form-group">
                            <label for="materialCode">재고코드</label>
                            <input type="text" 
                                   id="materialCode" 
                                   name="materialCode" 
                                   value="${mode == 'update' ? material.materialCode : material.materialCode}"
                                   readonly
                                   required>
                            <div class="error-message" id="materialCodeError"></div>
                        </div>
                        
                        <!-- 제품코드 -->
                        <div class="form-group">
                            <label for="standardCode">제품코드 *</label>
                            <c:choose>
                                <c:when test="${mode == 'update'}">
                                    <!-- 수정 모드: readonly로 표시 -->
                                    <input type="text" 
                                           id="standardCode" 
                                           name="standardCode" 
                                           value="${material.standardCode}"
                                           readonly>
                                </c:when>
                                <c:otherwise>
                                    <!-- 등록 모드: 드롭다운 선택 -->
                                    <select id="standardCode" name="standardCode" required>
                                        <option value="">제품을 선택하세요</option>
                                        <c:forEach var="standard" items="${standardList}">
                                            <option value="${standard.standardCode}">${standard.standardCode} - ${standard.stName}</option>
                                        </c:forEach>
                                    </select>
                                </c:otherwise>
                            </c:choose>
                            <div class="error-message" id="standardCodeError"></div>
                        </div>
                        
                        <!-- 재고량 -->
                        <div class="form-group">
                            <label for="quantity">재고량 *</label>
                            <input type="number" 
                                   id="quantity" 
                                   name="quantity" 
                                   min="0" 
                                   max="999999"
                                   value="${mode == 'update' ? material.maQuantity : '0'}"
                                   required>
                            <div class="error-message" id="quantityError"></div>
                        </div>
                        
                        <!-- 담당자 -->
                        <div class="form-group">
                            <label for="employeeNo">담당자 *</label>
                            <c:choose>
                                <c:when test="${mode == 'update'}">
                                    <!-- 수정 모드: readonly로 표시 -->
                                    <input type="text" 
                                           id="employeeNo" 
                                           name="employeeNo" 
                                           value="${material.employeeNo}"
                                           readonly>
                                </c:when>
                                <c:otherwise>
                                    <!-- 등록 모드: 드롭다운 선택 -->
                                    <select id="employeeNo" name="employeeNo" required>
                                        <option value="">담당자를 선택하세요</option>
                                        <c:forEach var="user" items="${userList}">
                                            <option value="${user.employeeNo}">${user.employeeNo} - ${user.usName}</option>
                                        </c:forEach>
                                    </select>
                                </c:otherwise>
                            </c:choose>
                            <div class="error-message" id="employeeNoError"></div>
                        </div>
                        
                        <!-- 버튼 그룹 -->
                        <div class="button-group">
                            <button type="submit" class="btn btn-success">저장</button>
                            <button type="button" class="btn btn-secondary" onclick="goBack()">취소</button>
                            <c:if test="${mode == 'update'}">
                            <button type="button" class="btn btn-danger" onclick="deleteMaterial()">삭제</button>
                            </c:if>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    
    <script>
        // JSP에서 JavaScript로 전달할 변수들
        window.mode = '${mode}';
        window.isEditMode = '${mode}' === 'update';
        window.contextPath = '${pageContext.request.contextPath}';
        window.materialCode = '${material.materialCode}';
    </script>
    <script src="${pageContext.request.contextPath}/src/js/material_form.js"></script>
</body>
</html>
