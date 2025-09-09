<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${mode == 'update' ? '공정 수정' : '공정 등록'} - MES 시스템</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/src/Header_Sied/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/src/css/process_form.css">
</head>
<body>
    <jsp:include page="/src/Header_Sied/header.jsp" />
    
    <div class="main-container">
        <jsp:include page="/src/Header_Sied/sidebar.jsp" />
        
        <div class="content-area">
            <div class="container">
                <!-- 헤더 섹션 -->
                <div class="form-header">
                    <h1>${mode == 'update' ? '공정 수정' : '공정 등록'}</h1>
                    <a href="${pageContext.request.contextPath}/process" class="btn btn-secondary">목록으로</a>
                </div>

                <!-- 폼 컨테이너 -->
                <div class="form-container">
                    <form id="processForm" action="${pageContext.request.contextPath}/process/${mode == 'update' ? 'update' : 'insert'}" method="post">
                        <!-- 공정번호 -->
                        <div class="form-group">
                            <label for="processNo">공정번호 <span class="required">*</span></label>
                            <input type="text" 
                                   id="processNo" 
                                   name="processNo" 
                                   value="${mode == 'update' ? process.processNo : ''}"
                                   ${mode == 'update' ? 'readonly' : 'readonly'}>
                            <div class="help-text">
                                ${mode == 'update' ? '공정번호는 수정할 수 없습니다.' : '공정번호는 시스템에서 자동으로 생성됩니다. (PC0001, PC0002...)'}
                            </div>
                            <div class="error-message" id="processNoError"></div>
                        </div>

                        <!-- 제품코드 -->
                        <div class="form-group">
                            <label for="standardCode">제품코드 <span class="required">*</span></label>
                            <c:choose>
                                <c:when test="${mode == 'update'}">
                                    <!-- 수정 모드: readonly로 표시 -->
                                    <input type="text" 
                                           id="standardCode" 
                                           name="standardCode" 
                                           value="${process.standardCode}"
                                           readonly>
                                </c:when>
                                <c:otherwise>
                                    <!-- 등록 모드: 드롭다운 선택 -->
                                    <select id="standardCode" name="standardCode" required>
                                        <option value="">제품을 선택하세요</option>
                                        <c:forEach var="standard" items="${standardList}">
                                            <option value="${standard.standardCode}" ${mode == 'update' && process.standardCode == standard.standardCode ? 'selected' : ''}>${standard.standardCode} - ${standard.stName}</option>
                                        </c:forEach>
                                    </select>
                                </c:otherwise>
                            </c:choose>
                            <div class="error-message" id="standardCodeError"></div>
                        </div>

                        <!-- 공정 설명 -->
                        <div class="form-group">
                            <label for="prDescription">공정 설명 <span class="required">*</span></label>
                            <textarea id="prDescription" 
                                      name="prDescription" 
                                      placeholder="공정에 대한 상세한 설명을 입력하세요"
                                      required>${mode == 'update' ? process.prDescription : ''}</textarea>
                            <div class="error-message" id="prDescriptionError"></div>
                        </div>

                        <!-- 공정 유형 -->
                        <div class="form-group">
                            <label for="prType">공정 유형 <span class="required">*</span></label>
                            <select id="prType" name="prType" required>
                                <option value="">공정 유형을 선택하세요</option>
                                <option value="혼합" ${mode == 'update' && process.prType == '혼합' ? 'selected' : ''}>혼합</option>
                                <option value="성형" ${mode == 'update' && process.prType == '성형' ? 'selected' : ''}>성형</option>
                                <option value="건조" ${mode == 'update' && process.prType == '건조' ? 'selected' : ''}>건조</option>
                                <option value="포장" ${mode == 'update' && process.prType == '포장' ? 'selected' : ''}>포장</option>
                            </select>
                            <div class="error-message" id="prTypeError"></div>
                        </div>

                        <!-- 공정 순서 -->
                        <div class="form-group">
                            <label for="prOrder">공정 순서 <span class="required">*</span></label>
                            <input type="number" 
                                   id="prOrder" 
                                   name="prOrder" 
                                   value="${mode == 'update' ? process.prOrder : ''}"
                                   min="1" 
                                   max="8" 
                                   placeholder="1-8 사이의 숫자를 입력하세요"
                                   required>
                            <div class="order-info">
                                <h4>공정 순서 안내</h4>
                                <ul>
                                    <li><strong>1-4:</strong> 반제품 과정 (오이 비누 베이스 제조)</li>
                                    <li><strong>5-8:</strong> 완제품 과정 (베이스 + 성분 = 완제품)</li>
                                </ul>
                            </div>
                            <div class="error-message" id="prOrderError"></div>
                        </div>

                        <!-- 공정 이미지 -->
                        <div class="form-group">
                            <label for="prImage">공정 이미지</label>
                            <input type="text" 
                                   id="prImage" 
                                   name="prImage" 
                                   value="${mode == 'update' ? process.prImage : ''}"
                                   placeholder="이미지 파일명을 입력하세요 (예: extract.png)">
                            <div class="help-text">
                                이미지 파일명만 입력하세요. 실제 파일은 별도로 업로드됩니다.
                            </div>
                            <div class="error-message" id="prImageError"></div>
                        </div>

                        <!-- 버튼 섹션 -->
                        <div class="button-section">
                            <button type="submit" class="btn btn-primary">${mode == 'update' ? '수정' : '등록'}</button>
                            <a href="${pageContext.request.contextPath}/process" class="btn btn-secondary">취소</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/src/js/process_form.js"></script>
</body>
</html>

