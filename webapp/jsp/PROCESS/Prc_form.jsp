<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${mode == 'update' ? '공정 수정' : '공정 등록'}- MES 시스템</title>
<c:set var="ctx" value="${pageContext.request.contextPath}" />


  <link rel="stylesheet" href="${ctx}/css/process_form.css">
  <link rel="stylesheet" href="${ctx}/src/Header_Side/style.css">
</head>
<body>
	<jsp:include page="../../Header_Side/header.jsp" />

	<div class="main-container">
		<jsp:include page="../../Header_Side/sidebar.jsp" />

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

                        <!-- 라우팅 타입 -->
                        <div class="form-group">
                            <label for="routingType">라우팅 타입 <span class="required">*</span></label>
                            <select id="routingType" name="routingType" required>
                                <option value="">라우팅 타입을 선택하세요</option>
                                <option value="SEMI" ${mode == 'update' && process.stType == 'SEMI' ? 'selected' : ''}>반제품 라우팅 (4단계)</option>
                                <option value="FINISH" ${mode == 'update' && process.stType == 'FINISH' ? 'selected' : ''}>완제품 라우팅 (8단계)</option>
                            </select>
                            <div class="help-text">
                                <strong>반제품 라우팅:</strong> 원자재 → 반제품 (4단계: 혼합, 성형, 건조, 포장)<br>
                                <strong>완제품 라우팅:</strong> 반제품 → 완제품 (8단계: 반제품 4단계 + 완제품 4단계)
                            </div>
                            <div class="error-message" id="routingTypeError"></div>
                        </div>

                        <!-- 라우팅 설명 -->
                        <div class="form-group">
                            <label for="routingDescription">라우팅 설명</label>
                            <textarea id="routingDescription" 
                                      name="routingDescription" 
                                      placeholder="라우팅에 대한 상세한 설명을 입력하세요"
                                      readonly>${mode == 'update' ? process.prDescription : '라우팅 타입을 선택하면 자동으로 생성됩니다.'}</textarea>
                            <div class="help-text">
                                라우팅 설명은 선택한 제품과 타입에 따라 자동으로 생성됩니다.
                            </div>
                        </div>

                        <!-- 공정 단계 미리보기 -->
                        <div class="form-group">
                            <label>공정 단계 미리보기</label>
                            <div id="processStepsPreview" class="process-steps-preview">
                                <p>라우팅 타입을 선택하면 공정 단계가 표시됩니다.</p>
                            </div>
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

    <script>
        // 라우팅 타입 선택 시 공정 단계 미리보기
        document.getElementById('routingType').addEventListener('change', function() {
            const routingType = this.value;
            const preview = document.getElementById('processStepsPreview');
            const description = document.getElementById('routingDescription');
            
            if (routingType === 'SEMI') {
                preview.innerHTML = `
                    <div class="step-preview">
                        <h4>반제품 라우팅 (4단계)</h4>
                        <ol>
                            <li>원료 혼합 - 신선 오이, 가성소다, 팜오일, 올리브오일</li>
                            <li>성분 배합 - 살리실산, 과일산, 아연 PCA, 티트리 오일</li>
                            <li>건조 - 25°C에서 4주간 건조</li>
                            <li>베이스 포장 - 개별 포장 및 라벨링</li>
                        </ol>
                    </div>
                `;
                description.value = '반제품';
            } else if (routingType === 'FINISH') {
                preview.innerHTML = `
                    <div class="step-preview">
                        <h4>완제품 라우팅 (8단계)</h4>
                        <ol>
                            <li>원료 혼합 - 신선 오이, 가성소다, 팜오일, 올리브오일</li>
                            <li>성분 배합 - 살리실산, 과일산, 아연 PCA, 티트리 오일</li>
                            <li>건조 - 25°C에서 4주간 건조</li>
                            <li>베이스 포장 - 개별 포장 및 라벨링</li>
                            <li>성분 추가 - 오이 추출물, 특수 성분</li>
                            <li>최종 혼합 - 올리브오일, 티트리 오일 추가</li>
                            <li>완제품 성형 - 30°C에서 2주간 건조</li>
                            <li>완제품 포장 - 최종 포장 및 품질 검사</li>
                        </ol>
                    </div>
                `;
                description.value = '완제품';
            } else {
                preview.innerHTML = '<p>라우팅 타입을 선택하면 공정 단계가 표시됩니다.</p>';
                description.value = '';
            }
        });
        
        // 페이지 로드 시 기존 값이 있으면 미리보기 표시
        document.addEventListener('DOMContentLoaded', function() {
            const routingType = document.getElementById('routingType').value;
            if (routingType) {
                document.getElementById('routingType').dispatchEvent(new Event('change'));
            }
        });
    </script>
    <script src="${pageContext.request.contextPath}/src/js/process_form.js"></script>
</body>
</html>

