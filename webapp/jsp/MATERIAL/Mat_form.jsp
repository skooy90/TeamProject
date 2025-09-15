<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>재고 등록/수정 - 자재관리</title>
    <c:set var="ctx" value="${pageContext.request.contextPath}" />
    
  <link rel="stylesheet" href="${ctx}/css/material_form.css">
  <link rel="stylesheet" href="${ctx}/Header_Side/style.css">
  
  <style>
    /* 자동 입력 필드 스타일 */
    .auto-input {
        background-color: #f8f9fa !important;
        border: none !important;
        color: #6c757d !important;
        cursor: not-allowed !important;
        text-align: center !important;
        font-weight: bold !important;
        position: relative !important;
    }
    
    .auto-input::before {
        content: "📋 ";
        margin-right: 8px;
    }
    
    .auto-input::after {
        content: "자동 입력됨";
        position: absolute;
        top: -8px;
        right: 10px;
        background: #007bff;
        color: white;
        font-size: 10px;
        padding: 2px 6px;
        border-radius: 10px;
        font-weight: normal;
    }
  </style>
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
                            <p class="esse"> * 필수 선택 사항입니다. </p>
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
                        
                        <!-- 검사번호 선택 (등록 모드만) -->
                        <c:if test="${mode == 'insert'}">
                            <div class="form-group">
                                <label for="qualityNo">검사번호 *</label>
                                <select id="qualityNo" name="qualityNo" required onchange="loadQualityInfo()">
                                    <option value="">검사번호를 선택하세요</option>
                                    <c:forEach var="quality" items="${qualityList}">
                                        <option value="${quality.qualityNo}" 
                                                data-standard-code="${quality.standardCode}"
                                                data-quantity="${quality.quQuantity}"
                                                data-st-name="${quality.stName}">
                                            ${quality.qualityNo} - ${quality.stName} (${quality.quQuantity}개)
                                        </option>
                                    </c:forEach>
                                </select>
                                <div class="error-message" id="qualityNoError"></div>
                            </div>
                        </c:if>
                        
                        <!-- 제품코드 -->
                        <div class="form-group">
                            <label for="standardCode">제품코드</label>
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
                                    <!-- 등록 모드: 자동 입력용 input -->
                                    <input type="text" 
                                           id="standardCode" 
                                           name="standardCode" 
                                           value=""
                                           class="auto-input"
                                           readonly>
                                </c:otherwise>
                            </c:choose>
                            <div class="error-message" id="standardCodeError"></div>
                        </div>
                        
                        <!-- 재고량 -->
                        <div class="form-group">
                            <c:choose>
                                <c:when test="${mode == 'update'}">
                                    <!-- 수정 모드: 수정 가능 -->
                            <label for="quantity">재고량 *</label>
                                    <input type="number" 
                                           id="quantity" 
                                           name="quantity" 
                                           min="0" 
                                           max="999999"
                                           value="${material.maQuantity}"
                                           required>
                                </c:when>
                                <c:otherwise>
                                    <!-- 등록 모드: 자동 입력용 input -->
                            <label for="quantity">재고량</label>
                                    <input type="number" 
                                           id="quantity" 
                                           name="quantity" 
                                           value="" 
                                           class="auto-input"
                                           readonly>
                                </c:otherwise>
                            </c:choose>
                            <div class="error-message" id="quantityError"></div>
                        </div>
                        
                        <!-- 담당자 -->
                        <div class="form-group">
                            <label for="employeeNo">담당자</label>
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
                                        <option value="">담당자를 선택하세요 *</option>
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
        
        // 검사번호 선택 시 자동 입력 함수
        function loadQualityInfo() {
            const select = document.getElementById('qualityNo');
            const option = select.options[select.selectedIndex];
            
            if (option.value) {
                // 제품코드 자동 입력
                document.getElementById('standardCode').value = option.dataset.standardCode;
                
                // 재고량 자동 입력
                document.getElementById('quantity').value = option.dataset.quantity;
                
                // 정보 표시 (선택사항)
                console.log('선택된 검사번호:', option.value);
                console.log('제품코드:', option.dataset.standardCode);
                console.log('재고량:', option.dataset.quantity);
            } else {
                // 선택 해제 시 초기화
                document.getElementById('standardCode').value = '';
                document.getElementById('quantity').value = '';
            }
        }
        
        // 폼 제출 전 유효성 검사
        function validateForm() {
            if (window.mode === 'insert') {
                const qualityNo = document.getElementById('qualityNo').value;
                const employeeNo = document.getElementById('employeeNo').value;
                
                if (!qualityNo) {
                    alert('검사번호를 선택해주세요.');
                    return false;
                }
                
                if (!employeeNo) {
                    alert('담당자를 선택해주세요.');
                    return false;
                }
            }
            
            return true;
        }
        
        // 폼 제출 이벤트 리스너
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('materialForm');
            if (form) {
                form.addEventListener('submit', function(e) {
                    if (!validateForm()) {
                        e.preventDefault();
                    }
                });
            }
        });
    </script>
    <script src="${pageContext.request.contextPath}/src/js/material_form.js"></script>
</body>
</html>