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
  <link rel="stylesheet" href="${ctx}/Header_Side/style.css">
</head>
<body>
	<jsp:include page="../../Header_Side/header.jsp" />

	<div class="main-container">
		<jsp:include page="../../Header_Side/sidebar.jsp" />

		<div class="content-area">
            <div class="container">
                <!-- 헤더 섹션 -->
                <div class="detail-header">
                    <div class="header-info">
                        <h1>${mode == 'update' ? '공정 수정' : '공정 등록'}</h1>
                        <span class="routing-type">${mode == 'update' ? (process.stType == 'SEMI' ? '반제품 라우팅' : '완제품 라우팅') : '라우팅 등록'}</span>
                    </div>
                    <div class="header-actions">
                        <a href="${pageContext.request.contextPath}/process" class="btn btn-secondary">목록으로</a>
                    </div>
                </div>

                <!-- 그리드 레이아웃 -->
                <div class="grid-container">
                    <!-- 기본 정보 카드 -->
                    <div class="info-card">
                        <h3>라우팅 기본 정보</h3>
                        <div class="info-item">
                            <span class="info-label">제품코드</span>
                            <span class="info-value">${mode == 'update' ? process.standardCode : '선택 필요'}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">제품명</span>
                            <span class="info-value">${mode == 'update' ? process.stName : '선택 필요'}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">라우팅 타입</span>
                            <span class="info-value">${mode == 'update' ? (process.stType == 'SEMI' ? '반제품' : '완제품') : '선택 필요'}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">총 공정 단계</span>
                            <span class="info-value">${mode == 'update' ? (process.stType == 'SEMI' ? '4단계' : '8단계') : '선택 필요'}</span>
                        </div>
                    </div>

                    <!-- 공정 단계 미리보기 -->
                    <div class="chart-card">
                        <h3>공정 단계 미리보기</h3>
                        <div class="chart-container">
                            <div id="processStepsPreview" class="process-steps-preview">
                                <p>라우팅 타입을 선택하면 공정 단계가 표시됩니다.</p>
                            </div>
                        </div>
                    </div>
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

                        <!-- 버튼 섹션 -->
                        <div class="button-section">
                            <button type="submit" class="btn btn-primary">${mode == 'update' ? '수정' : '등록'}</button>
                            <a href="${pageContext.request.contextPath}/process" class="btn btn-secondary">취소</a>
                        </div>
                    </form>
                </div>

                <!-- 공정 단계별 상세 정보 -->
                <div class="process-steps">
                    <h3>공정 단계별 상세 정보</h3>
                    <div class="step-controls">
                        <button type="button" id="addStepBtn" class="btn btn-primary" style="display: none;">
                            <i class="fas fa-plus"></i> 단계 추가
                        </button>
                        <button type="button" id="resetStepsBtn" class="btn btn-secondary" style="display: none;">
                            <i class="fas fa-undo"></i> 기본값으로 초기화
                        </button>
                    </div>
                    <div id="processStepsDetail" class="process-steps-detail">
                        <c:choose>
                            <c:when test="${mode == 'update'}">
                                <!-- 수정 모드: JavaScript로 기존 공정 단계 표시 -->
                                <div id="updateProcessSteps"></div>
                            </c:when>
                            <c:otherwise>
                                <!-- 등록 모드: 기본 메시지 -->
                                <p>라우팅 타입을 선택하면 공정 단계별 상세 정보가 표시됩니다.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        // 라우팅 타입 선택 시 공정 단계 미리보기 및 상세 정보 업데이트
        document.getElementById('routingType').addEventListener('change', function() {
            const routingType = this.value;
            const preview = document.getElementById('processStepsPreview');
            const detail = document.getElementById('processStepsDetail');
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
                
                if (detail) {
                    detail.innerHTML = generateSemiProcessSteps();
                }
                
                // 컨트롤 버튼 표시 (등록 모드에서만)
                const addStepBtn = document.getElementById('addStepBtn');
                const resetStepsBtn = document.getElementById('resetStepsBtn');
                if (addStepBtn) addStepBtn.style.display = 'inline-block';
                if (resetStepsBtn) resetStepsBtn.style.display = 'inline-block';
                
                // 기본 정보 업데이트
                updateBasicInfo();
                
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
                
                if (detail) {
                    detail.innerHTML = generateFinishProcessSteps();
                }
                
                // 컨트롤 버튼 표시 (등록 모드에서만)
                const addStepBtn = document.getElementById('addStepBtn');
                const resetStepsBtn = document.getElementById('resetStepsBtn');
                if (addStepBtn) addStepBtn.style.display = 'inline-block';
                if (resetStepsBtn) resetStepsBtn.style.display = 'inline-block';
                
                // 기본 정보 업데이트
                updateBasicInfo();
                
                description.value = '완제품';
            } else {
                preview.innerHTML = '<p>라우팅 타입을 선택하면 공정 단계가 표시됩니다.</p>';
                if (detail) {
                    detail.innerHTML = '<p>라우팅 타입을 선택하면 공정 단계별 상세 정보가 표시됩니다.</p>';
                }
                
                // 컨트롤 버튼 숨기기 (등록 모드에서만)
                const addStepBtn = document.getElementById('addStepBtn');
                const resetStepsBtn = document.getElementById('resetStepsBtn');
                if (addStepBtn) addStepBtn.style.display = 'none';
                if (resetStepsBtn) resetStepsBtn.style.display = 'none';
                
                // 기본 정보 업데이트
                updateBasicInfo();
                
                description.value = '';
            }
        });
        
        // 반제품 공정 단계 생성 (편집 가능한 폼)
        function generateSemiProcessSteps() {
            let html = '';
            
            // 1단계
            html += '<div class="step-card step-semi" data-step="1">';
            html += '<div class="step-header">';
            html += '<div class="step-number">1</div>';
            html += '<div class="step-info">';
            html += '<input type="text" name="stepName_1" value="원료 혼합" class="step-name-input" placeholder="단계명">';
            html += '<select name="stepType_1" class="step-type-select">';
            html += '<option value="혼합" selected>혼합</option>';
            html += '<option value="성형">성형</option>';
            html += '<option value="건조">건조</option>';
            html += '<option value="포장">포장</option>';
            html += '</select>';
            html += '</div>';
            html += '<button type="button" class="btn-remove-step" onclick="removeStep(1)">×</button>';
            html += '</div>';
            html += '<div class="step-content">';
            html += '<div class="materials-section">';
            html += '<h5>필요 자재</h5>';
            html += '<div class="materials-input">';
            html += '<textarea name="materials_1" class="materials-textarea" placeholder="자재를 입력하세요 (한 줄에 하나씩)">신선 오이 2kg (RA001)\n가성소다 200g (RA002)\n팜 오일 500ml (RA003)\n올리브 오일 300ml (RA004)\n믹서기, 교반기</textarea>';
            html += '</div>';
            html += '</div>';
            html += '<div class="work-instruction">';
            html += '<h5>작업 지침</h5>';
            html += '<div class="instruction-input">';
            html += '<textarea name="instructions_1" class="instruction-textarea" placeholder="작업 지침을 입력하세요 (한 줄에 하나씩)">1. 신선 오이 2kg (RA001) 깨끗이 씻기\n2. 슬라이스하여 믹서기에 넣기\n3. 가성소다 200g (RA002)과 함께 3분간 갈기\n4. 팜 오일 500ml (RA003) + 올리브 오일 300ml (RA004) 추가\n5. 40°C에서 5분간 교반</textarea>';
            html += '</div>';
            html += '</div>';
            html += '</div>';
            html += '</div>';
            
            // 2단계
            html += '<div class="step-card step-semi" data-step="2">';
            html += '<div class="step-header">';
            html += '<div class="step-number">2</div>';
            html += '<div class="step-info">';
            html += '<input type="text" name="stepName_2" value="성분 배합" class="step-name-input" placeholder="단계명">';
            html += '<select name="stepType_2" class="step-type-select">';
            html += '<option value="혼합">혼합</option>';
            html += '<option value="성형" selected>성형</option>';
            html += '<option value="건조">건조</option>';
            html += '<option value="포장">포장</option>';
            html += '</select>';
            html += '</div>';
            html += '<button type="button" class="btn-remove-step" onclick="removeStep(2)">×</button>';
            html += '</div>';
            html += '<div class="step-content">';
            html += '<div class="materials-section">';
            html += '<h5>필요 자재</h5>';
            html += '<div class="materials-input">';
            html += '<textarea name="materials_2" class="materials-textarea" placeholder="자재를 입력하세요 (한 줄에 하나씩)">살리실산 50g (RA005)\n과일산 40g (RA006)\n아연 PCA 60g (RA007)\n티트리 오일 30ml (RA008)\n성형틀, 교반기</textarea>';
            html += '</div>';
            html += '</div>';
            html += '<div class="work-instruction">';
            html += '<h5>작업 지침</h5>';
            html += '<div class="instruction-input">';
            html += '<textarea name="instructions_2" class="instruction-textarea" placeholder="작업 지침을 입력하세요 (한 줄에 하나씩)">1. 살리실산 50g (RA005) + 과일산 40g (RA006) 혼합\n2. 아연 PCA 60g (RA007) + 티트리 오일 30ml (RA008) 추가\n3. 교반기로 3분간 균일하게 혼합\n4. 성형틀에 부어넣기</textarea>';
            html += '</div>';
            html += '</div>';
            html += '</div>';
            html += '</div>';
            
            // 3단계
            html += '<div class="step-card step-semi" data-step="3">';
            html += '<div class="step-header">';
            html += '<div class="step-number">3</div>';
            html += '<div class="step-info">';
            html += '<input type="text" name="stepName_3" value="건조" class="step-name-input" placeholder="단계명">';
            html += '<select name="stepType_3" class="step-type-select">';
            html += '<option value="혼합">혼합</option>';
            html += '<option value="성형">성형</option>';
            html += '<option value="건조" selected>건조</option>';
            html += '<option value="포장">포장</option>';
            html += '</select>';
            html += '</div>';
            html += '<button type="button" class="btn-remove-step" onclick="removeStep(3)">×</button>';
            html += '</div>';
            html += '<div class="step-content">';
            html += '<div class="materials-section">';
            html += '<h5>필요 자재</h5>';
            html += '<div class="materials-input">';
            html += '<textarea name="materials_3" class="materials-textarea" placeholder="자재를 입력하세요 (한 줄에 하나씩)">건조실 (온도: 25°C)\n온도계, 습도계\n시간 측정기\n경화 확인 도구</textarea>';
            html += '</div>';
            html += '</div>';
            html += '<div class="work-instruction">';
            html += '<h5>작업 지침</h5>';
            html += '<div class="instruction-input">';
            html += '<textarea name="instructions_3" class="instruction-textarea" placeholder="작업 지침을 입력하세요 (한 줄에 하나씩)">1. 성형틀을 건조실에 배치 (온도: 25°C)\n2. 24시간 경화 대기\n3. 4주간 건조 완료 (습도 60% 유지)\n4. 경화 상태 확인 후 다음 단계 진행</textarea>';
            html += '</div>';
            html += '</div>';
            html += '</div>';
            html += '</div>';
            
            // 4단계
            html += '<div class="step-card step-semi" data-step="4">';
            html += '<div class="step-header">';
            html += '<div class="step-number">4</div>';
            html += '<div class="step-info">';
            html += '<input type="text" name="stepName_4" value="베이스 포장" class="step-name-input" placeholder="단계명">';
            html += '<select name="stepType_4" class="step-type-select">';
            html += '<option value="혼합">혼합</option>';
            html += '<option value="성형">성형</option>';
            html += '<option value="건조">건조</option>';
            html += '<option value="포장" selected>포장</option>';
            html += '</select>';
            html += '</div>';
            html += '<button type="button" class="btn-remove-step" onclick="removeStep(4)">×</button>';
            html += '</div>';
            html += '<div class="step-content">';
            html += '<div class="materials-section">';
            html += '<h5>필요 자재</h5>';
            html += '<div class="materials-input">';
            html += '<textarea name="materials_4" class="materials-textarea" placeholder="자재를 입력하세요 (한 줄에 하나씩)">비누 베이스 포장재\n베이스 라벨\n보관 박스\n품질 검사 도구</textarea>';
            html += '</div>';
            html += '</div>';
            html += '<div class="work-instruction">';
            html += '<h5>작업 지침</h5>';
            html += '<div class="instruction-input">';
            html += '<textarea name="instructions_4" class="instruction-textarea" placeholder="작업 지침을 입력하세요 (한 줄에 하나씩)">1. 개별 포장재로 비누 베이스 포장\n2. 베이스 라벨 부착 (제조일자, 유통기한 표시)\n3. 품질 검사 후 박스에 정리하여 보관\n4. 보관 조건: 서늘하고 건조한 곳</textarea>';
            html += '</div>';
            html += '</div>';
            html += '</div>';
            html += '</div>';
            
            return html;
        }
        
        // 완제품 공정 단계 생성 (편집 가능한 폼)
        function generateFinishProcessSteps() {
            let html = generateSemiProcessSteps();
            
            // 5단계
            html += '<div class="step-card step-finish" data-step="5">';
            html += '<div class="step-header">';
            html += '<div class="step-number">5</div>';
            html += '<div class="step-info">';
            html += '<input type="text" name="stepName_5" value="성분 추가" class="step-name-input" placeholder="단계명">';
            html += '<select name="stepType_5" class="step-type-select">';
            html += '<option value="혼합" selected>혼합</option>';
            html += '<option value="성형">성형</option>';
            html += '<option value="건조">건조</option>';
            html += '<option value="포장">포장</option>';
            html += '</select>';
            html += '</div>';
            html += '<button type="button" class="btn-remove-step" onclick="removeStep(5)">×</button>';
            html += '</div>';
            html += '<div class="step-content">';
            html += '<div class="materials-section">';
            html += '<h5>필요 자재</h5>';
            html += '<div class="materials-input">';
            html += '<textarea name="materials_5" class="materials-textarea" placeholder="자재를 입력하세요 (한 줄에 하나씩)">오이 비누 베이스 2kg (SE001)\n오이 추출물 100ml (SE002)\n각질제거 성분 150g (SE003)\n믹서기, 교반기</textarea>';
            html += '</div>';
            html += '</div>';
            html += '<div class="work-instruction">';
            html += '<h5>작업 지침</h5>';
            html += '<div class="instruction-input">';
            html += '<textarea name="instructions_5" class="instruction-textarea" placeholder="작업 지침을 입력하세요 (한 줄에 하나씩)">1. 오이 비누 베이스 2kg (SE001) 35°C로 가열\n2. 오이 추출물 100ml (SE002) 추가\n3. 각질제거 성분 150g (SE003) 혼합\n4. 35°C에서 3분간 교반</textarea>';
            html += '</div>';
            html += '</div>';
            html += '</div>';
            html += '</div>';
            
            // 6단계
            html += '<div class="step-card step-finish" data-step="6">';
            html += '<div class="step-header">';
            html += '<div class="step-number">6</div>';
            html += '<div class="step-info">';
            html += '<input type="text" name="stepName_6" value="최종 혼합" class="step-name-input" placeholder="단계명">';
            html += '<select name="stepType_6" class="step-type-select">';
            html += '<option value="혼합" selected>혼합</option>';
            html += '<option value="성형">성형</option>';
            html += '<option value="건조">건조</option>';
            html += '<option value="포장">포장</option>';
            html += '</select>';
            html += '</div>';
            html += '<button type="button" class="btn-remove-step" onclick="removeStep(6)">×</button>';
            html += '</div>';
            html += '<div class="step-content">';
            html += '<div class="materials-section">';
            html += '<h5>필요 자재</h5>';
            html += '<div class="materials-input">';
            html += '<textarea name="materials_6" class="materials-textarea" placeholder="자재를 입력하세요 (한 줄에 하나씩)">올리브 오일 200ml (RA004)\n티트리 오일 20ml (RA008)\n살리실산 25g (RA005)\n교반기, 온도계</textarea>';
            html += '</div>';
            html += '</div>';
            html += '<div class="work-instruction">';
            html += '<h5>작업 지침</h5>';
            html += '<div class="instruction-input">';
            html += '<textarea name="instructions_6" class="instruction-textarea" placeholder="작업 지침을 입력하세요 (한 줄에 하나씩)">1. 올리브 오일 200ml (RA004) 추가\n2. 티트리 오일 20ml (RA008) 혼합\n3. 살리실산 25g (RA005) 추가로 각질제거 효과 강화\n4. 30°C에서 2분간 최종 교반</textarea>';
            html += '</div>';
            html += '</div>';
            html += '</div>';
            html += '</div>';
            
            // 7단계
            html += '<div class="step-card step-finish" data-step="7">';
            html += '<div class="step-header">';
            html += '<div class="step-number">7</div>';
            html += '<div class="step-info">';
            html += '<input type="text" name="stepName_7" value="완제품 성형" class="step-name-input" placeholder="단계명">';
            html += '<select name="stepType_7" class="step-type-select">';
            html += '<option value="혼합">혼합</option>';
            html += '<option value="성형" selected>성형</option>';
            html += '<option value="건조">건조</option>';
            html += '<option value="포장">포장</option>';
            html += '</select>';
            html += '</div>';
            html += '<button type="button" class="btn-remove-step" onclick="removeStep(7)">×</button>';
            html += '</div>';
            html += '<div class="step-content">';
            html += '<div class="materials-section">';
            html += '<h5>필요 자재</h5>';
            html += '<div class="materials-input">';
            html += '<textarea name="materials_7" class="materials-textarea" placeholder="자재를 입력하세요 (한 줄에 하나씩)">완제품 성형틀\n건조실 (온도: 30°C)\n온도계, 습도계\n경화 확인 도구</textarea>';
            html += '</div>';
            html += '</div>';
            html += '<div class="work-instruction">';
            html += '<h5>작업 지침</h5>';
            html += '<div class="instruction-input">';
            html += '<textarea name="instructions_7" class="instruction-textarea" placeholder="작업 지침을 입력하세요 (한 줄에 하나씩)">1. 완제품 성형틀에 부어넣기\n2. 건조실에 배치 (온도: 30°C, 습도: 50%)\n3. 12시간 경화 대기\n4. 2주간 건조 완료 후 경화 상태 확인</textarea>';
            html += '</div>';
            html += '</div>';
            html += '</div>';
            html += '</div>';
            
            // 8단계
            html += '<div class="step-card step-finish" data-step="8">';
            html += '<div class="step-header">';
            html += '<div class="step-number">8</div>';
            html += '<div class="step-info">';
            html += '<input type="text" name="stepName_8" value="완제품 포장" class="step-name-input" placeholder="단계명">';
            html += '<select name="stepType_8" class="step-type-select">';
            html += '<option value="혼합">혼합</option>';
            html += '<option value="성형">성형</option>';
            html += '<option value="건조">건조</option>';
            html += '<option value="포장" selected>포장</option>';
            html += '</select>';
            html += '</div>';
            html += '<button type="button" class="btn-remove-step" onclick="removeStep(8)">×</button>';
            html += '</div>';
            html += '<div class="step-content">';
            html += '<div class="materials-section">';
            html += '<h5>필요 자재</h5>';
            html += '<div class="materials-input">';
            html += '<textarea name="materials_8" class="materials-textarea" placeholder="자재를 입력하세요 (한 줄에 하나씩)">완제품 포장재\n제품 라벨\n품질 검사 도구\n최종 포장 박스</textarea>';
            html += '</div>';
            html += '</div>';
            html += '<div class="work-instruction">';
            html += '<h5>작업 지침</h5>';
            html += '<div class="instruction-input">';
            html += '<textarea name="instructions_8" class="instruction-textarea" placeholder="작업 지침을 입력하세요 (한 줄에 하나씩)">1. 완제품 포장재로 개별 포장\n2. 제품 라벨 부착 (제품명, 성분, 유통기한)\n3. 품질 검사 (외관, 무게, 포장 상태)\n4. 최종 포장 박스에 정리하여 출고</textarea>';
            html += '</div>';
            html += '</div>';
            html += '</div>';
            html += '</div>';
            
            return html;
        }
        
        // 단계 제거 함수
        function removeStep(stepNumber) {
            const stepCard = document.querySelector(`[data-step="${stepNumber}"]`);
            if (stepCard) {
                stepCard.remove();
                updateStepNumbers();
            }
        }
        
        // 단계 번호 업데이트
        function updateStepNumbers() {
            const stepCards = document.querySelectorAll('.step-card');
            stepCards.forEach((card, index) => {
                const stepNumber = card.querySelector('.step-number');
                const stepNameInput = card.querySelector('.step-name-input');
                const stepTypeSelect = card.querySelector('.step-type-select');
                const materialsTextarea = card.querySelector('.materials-textarea');
                const instructionsTextarea = card.querySelector('.instruction-textarea');
                
                const newStepNumber = index + 1;
                stepNumber.textContent = newStepNumber;
                card.setAttribute('data-step', newStepNumber);
                
                // name 속성 업데이트
                if (stepNameInput) stepNameInput.name = `stepName_${newStepNumber}`;
                if (stepTypeSelect) stepTypeSelect.name = `stepType_${newStepNumber}`;
                if (materialsTextarea) materialsTextarea.name = `materials_${newStepNumber}`;
                if (instructionsTextarea) instructionsTextarea.name = `instructions_${newStepNumber}`;
                
                // 삭제 버튼 onclick 업데이트
                const removeBtn = card.querySelector('.btn-remove-step');
                if (removeBtn) {
                    removeBtn.setAttribute('onclick', `removeStep(${newStepNumber})`);
                }
            });
        }
        
        // 단계 추가 함수
        function addStep() {
            const stepCards = document.querySelectorAll('.step-card');
            const newStepNumber = stepCards.length + 1;
            const routingType = document.getElementById('routingType').value;
            const isFinish = routingType === 'FINISH';
            
            const newStepCard = document.createElement('div');
            newStepCard.className = `step-card ${isFinish ? 'step-finish' : 'step-semi'}`;
            newStepCard.setAttribute('data-step', newStepNumber);
            
            newStepCard.innerHTML = `
                <div class="step-header">
                    <div class="step-number">${newStepNumber}</div>
                    <div class="step-info">
                        <input type="text" name="stepName_${newStepNumber}" value="새 단계" class="step-name-input" placeholder="단계명">
                        <select name="stepType_${newStepNumber}" class="step-type-select">
                            <option value="혼합">혼합</option>
                            <option value="성형">성형</option>
                            <option value="건조">건조</option>
                            <option value="포장">포장</option>
                        </select>
                    </div>
                    <button type="button" class="btn-remove-step" onclick="removeStep(${newStepNumber})">×</button>
                </div>
                <div class="step-content">
                    <div class="materials-section">
                        <h5>필요 자재</h5>
                        <div class="materials-input">
                            <textarea name="materials_${newStepNumber}" class="materials-textarea" placeholder="자재를 입력하세요 (한 줄에 하나씩)"></textarea>
                        </div>
                    </div>
                    <div class="work-instruction">
                        <h5>작업 지침</h5>
                        <div class="instruction-input">
                            <textarea name="instructions_${newStepNumber}" class="instruction-textarea" placeholder="작업 지침을 입력하세요 (한 줄에 하나씩)"></textarea>
                        </div>
                    </div>
                </div>
            `;
            
            document.getElementById('processStepsDetail').appendChild(newStepCard);
        }
        
        // 기본값으로 초기화
        function resetSteps() {
            const routingType = document.getElementById('routingType').value;
            if (routingType) {
                document.getElementById('routingType').dispatchEvent(new Event('change'));
            }
        }
        
        // 수정 모드에서 라우팅 타입 변경 시 공정 단계 업데이트
        function updateProcessStepsForType(routingType) {
            const detail = document.getElementById('processStepsDetail');
            if (!detail) return;
            
            // 기존 데이터 수집
            const existingData = collectExistingStepData();
            
            if (routingType === 'SEMI') {
                // 반제품으로 변경 (4단계만 유지)
                const semiSteps = generateSemiProcessStepsWithData(existingData);
                detail.innerHTML = semiSteps;
            } else if (routingType === 'FINISH') {
                // 완제품으로 변경 (8단계)
                const finishSteps = generateFinishProcessStepsWithData(existingData);
                detail.innerHTML = finishSteps;
            }
            
            // 단계 번호 재정렬
            updateStepNumbers();
        }
        
        // 기존 공정 단계 데이터 수집
        function collectExistingStepData() {
            const data = {};
            const stepCards = document.querySelectorAll('.step-card');
            
            stepCards.forEach(card => {
                const stepNumber = card.getAttribute('data-step');
                const stepName = card.querySelector('.step-name-input')?.value || '';
                const stepType = card.querySelector('.step-type-select')?.value || '';
                const materials = card.querySelector('.materials-textarea')?.value || '';
                const instructions = card.querySelector('.instruction-textarea')?.value || '';
                
                data[stepNumber] = {
                    name: stepName,
                    type: stepType,
                    materials: materials,
                    instructions: instructions
                };
            });
            
            return data;
        }
        
        // 기존 데이터를 포함한 반제품 단계 생성
        function generateSemiProcessStepsWithData(existingData) {
            let html = '';
            for (let i = 1; i <= 4; i++) {
                const data = existingData[i] || {};
                const stepName = data.name || getDefaultStepName(i, 'SEMI');
                const materials = data.materials || getDefaultMaterials(i, 'SEMI');
                const instructions = data.instructions || getDefaultInstructions(i, 'SEMI');
                
                // selected 속성 생성
                const getSelectedAttr = (type, defaultTypes) => {
                    if (data.type === type) return 'selected';
                    if (!data.type && defaultTypes.includes(i)) return 'selected';
                    return '';
                };
                
                html += '<div class="step-card step-semi" data-step="' + i + '">';
                html += '<div class="step-header">';
                html += '<div class="step-number">' + i + '</div>';
                html += '<div class="step-info">';
                html += '<input type="text" name="stepName_' + i + '" value="' + stepName + '" class="step-name-input" placeholder="단계명">';
                html += '<select name="stepType_' + i + '" class="step-type-select">';
                html += '<option value="혼합" ' + getSelectedAttr('혼합', [1]) + '>혼합</option>';
                html += '<option value="성형" ' + getSelectedAttr('성형', [2]) + '>성형</option>';
                html += '<option value="건조" ' + getSelectedAttr('건조', [3]) + '>건조</option>';
                html += '<option value="포장" ' + getSelectedAttr('포장', [4]) + '>포장</option>';
                html += '</select>';
                html += '</div>';
                html += '<button type="button" class="btn-remove-step" onclick="removeStep(' + i + ')">×</button>';
                html += '</div>';
                html += '<div class="step-content">';
                html += '<div class="materials-section">';
                html += '<h5>필요 자재</h5>';
                html += '<div class="materials-input">';
                html += '<textarea name="materials_' + i + '" class="materials-textarea" placeholder="자재를 입력하세요 (한 줄에 하나씩)">' + materials + '</textarea>';
                html += '</div>';
                html += '</div>';
                html += '<div class="work-instruction">';
                html += '<h5>작업 지침</h5>';
                html += '<div class="instruction-input">';
                html += '<textarea name="instructions_' + i + '" class="instruction-textarea" placeholder="작업 지침을 입력하세요 (한 줄에 하나씩)">' + instructions + '</textarea>';
                html += '</div>';
                html += '</div>';
                html += '</div>';
                html += '</div>';
            }
            return html;
        }
        
        // 기존 데이터를 포함한 완제품 단계 생성
        function generateFinishProcessStepsWithData(existingData) {
            let html = '';
            for (let i = 1; i <= 8; i++) {
                const data = existingData[i] || {};
                const stepName = data.name || getDefaultStepName(i, 'FINISH');
                const materials = data.materials || getDefaultMaterials(i, 'FINISH');
                const instructions = data.instructions || getDefaultInstructions(i, 'FINISH');
                
                // selected 속성 생성
                const getSelectedAttr = (type, defaultTypes) => {
                    if (data.type === type) return 'selected';
                    if (!data.type && defaultTypes.includes(i)) return 'selected';
                    return '';
                };
                
                const cardClass = i <= 4 ? 'step-semi' : 'step-finish';
                html += '<div class="step-card ' + cardClass + '" data-step="' + i + '">';
                html += '<div class="step-header">';
                html += '<div class="step-number">' + i + '</div>';
                html += '<div class="step-info">';
                html += '<input type="text" name="stepName_' + i + '" value="' + stepName + '" class="step-name-input" placeholder="단계명">';
                html += '<select name="stepType_' + i + '" class="step-type-select">';
                html += '<option value="혼합" ' + getSelectedAttr('혼합', [1, 5, 6]) + '>혼합</option>';
                html += '<option value="성형" ' + getSelectedAttr('성형', [2, 7]) + '>성형</option>';
                html += '<option value="건조" ' + getSelectedAttr('건조', [3]) + '>건조</option>';
                html += '<option value="포장" ' + getSelectedAttr('포장', [4, 8]) + '>포장</option>';
                html += '</select>';
                html += '</div>';
                html += '<button type="button" class="btn-remove-step" onclick="removeStep(' + i + ')">×</button>';
                html += '</div>';
                html += '<div class="step-content">';
                html += '<div class="materials-section">';
                html += '<h5>필요 자재</h5>';
                html += '<div class="materials-input">';
                html += '<textarea name="materials_' + i + '" class="materials-textarea" placeholder="자재를 입력하세요 (한 줄에 하나씩)">' + materials + '</textarea>';
                html += '</div>';
                html += '</div>';
                html += '<div class="work-instruction">';
                html += '<h5>작업 지침</h5>';
                html += '<div class="instruction-input">';
                html += '<textarea name="instructions_' + i + '" class="instruction-textarea" placeholder="작업 지침을 입력하세요 (한 줄에 하나씩)">' + instructions + '</textarea>';
                html += '</div>';
                html += '</div>';
                html += '</div>';
                html += '</div>';
            }
            return html;
        }
        
        // 기본 단계명 반환
        function getDefaultStepName(stepNumber, routingType) {
            if (routingType === 'SEMI') {
                const names = ['원료 혼합', '성분 배합', '건조', '베이스 포장'];
                return names[stepNumber - 1] || `단계 ${stepNumber}`;
            } else {
                const names = ['원료 혼합', '성분 배합', '건조', '베이스 포장', '성분 추가', '최종 혼합', '완제품 성형', '완제품 포장'];
                return names[stepNumber - 1] || `단계 ${stepNumber}`;
            }
        }
        
        // 기본 자재 반환
        function getDefaultMaterials(stepNumber, routingType) {
            if (routingType === 'SEMI') {
                const materials = [
                    '신선 오이 2kg (RA001)\n가성소다 200g (RA002)\n팜 오일 500ml (RA003)\n올리브 오일 300ml (RA004)\n믹서기, 교반기',
                    '살리실산 50g (RA005)\n과일산 40g (RA006)\n아연 PCA 60g (RA007)\n티트리 오일 30ml (RA008)\n성형틀, 교반기',
                    '건조실 (온도: 25°C)\n온도계, 습도계\n시간 측정기\n경화 확인 도구',
                    '비누 베이스 포장재\n베이스 라벨\n보관 박스\n품질 검사 도구'
                ];
                return materials[stepNumber - 1] || '';
            } else {
                const materials = [
                    '신선 오이 2kg (RA001)\n가성소다 200g (RA002)\n팜 오일 500ml (RA003)\n올리브 오일 300ml (RA004)\n믹서기, 교반기',
                    '살리실산 50g (RA005)\n과일산 40g (RA006)\n아연 PCA 60g (RA007)\n티트리 오일 30ml (RA008)\n성형틀, 교반기',
                    '건조실 (온도: 25°C)\n온도계, 습도계\n시간 측정기\n경화 확인 도구',
                    '비누 베이스 포장재\n베이스 라벨\n보관 박스\n품질 검사 도구',
                    '오이 비누 베이스 2kg (SE001)\n오이 추출물 100ml (SE002)\n각질제거 성분 150g (SE003)\n믹서기, 교반기',
                    '올리브 오일 200ml (RA004)\n티트리 오일 20ml (RA008)\n살리실산 25g (RA005)\n교반기, 온도계',
                    '완제품 성형틀\n건조실 (온도: 30°C)\n온도계, 습도계\n경화 확인 도구',
                    '완제품 포장재\n제품 라벨\n품질 검사 도구\n최종 포장 박스'
                ];
                return materials[stepNumber - 1] || '';
            }
        }
        
        // 기본 작업 지침 반환
        function getDefaultInstructions(stepNumber, routingType) {
            if (routingType === 'SEMI') {
                const instructions = [
                    '1. 신선 오이 2kg (RA001) 깨끗이 씻기\n2. 슬라이스하여 믹서기에 넣기\n3. 가성소다 200g (RA002)과 함께 3분간 갈기\n4. 팜 오일 500ml (RA003) + 올리브 오일 300ml (RA004) 추가\n5. 40°C에서 5분간 교반',
                    '1. 살리실산 50g (RA005) + 과일산 40g (RA006) 혼합\n2. 아연 PCA 60g (RA007) + 티트리 오일 30ml (RA008) 추가\n3. 교반기로 3분간 균일하게 혼합\n4. 성형틀에 부어넣기',
                    '1. 성형틀을 건조실에 배치 (온도: 25°C)\n2. 24시간 경화 대기\n3. 4주간 건조 완료 (습도 60% 유지)\n4. 경화 상태 확인 후 다음 단계 진행',
                    '1. 개별 포장재로 비누 베이스 포장\n2. 베이스 라벨 부착 (제조일자, 유통기한 표시)\n3. 품질 검사 후 박스에 정리하여 보관\n4. 보관 조건: 서늘하고 건조한 곳'
                ];
                return instructions[stepNumber - 1] || '';
            } else {
                const instructions = [
                    '1. 신선 오이 2kg (RA001) 깨끗이 씻기\n2. 슬라이스하여 믹서기에 넣기\n3. 가성소다 200g (RA002)과 함께 3분간 갈기\n4. 팜 오일 500ml (RA003) + 올리브 오일 300ml (RA004) 추가\n5. 40°C에서 5분간 교반',
                    '1. 살리실산 50g (RA005) + 과일산 40g (RA006) 혼합\n2. 아연 PCA 60g (RA007) + 티트리 오일 30ml (RA008) 추가\n3. 교반기로 3분간 균일하게 혼합\n4. 성형틀에 부어넣기',
                    '1. 성형틀을 건조실에 배치 (온도: 25°C)\n2. 24시간 경화 대기\n3. 4주간 건조 완료 (습도 60% 유지)\n4. 경화 상태 확인 후 다음 단계 진행',
                    '1. 개별 포장재로 비누 베이스 포장\n2. 베이스 라벨 부착 (제조일자, 유통기한 표시)\n3. 품질 검사 후 박스에 정리하여 보관\n4. 보관 조건: 서늘하고 건조한 곳',
                    '1. 오이 비누 베이스 2kg (SE001) 35°C로 가열\n2. 오이 추출물 100ml (SE002) 추가\n3. 각질제거 성분 150g (SE003) 혼합\n4. 35°C에서 3분간 교반',
                    '1. 올리브 오일 200ml (RA004) 추가\n2. 티트리 오일 20ml (RA008) 혼합\n3. 살리실산 25g (RA005) 추가로 각질제거 효과 강화\n4. 30°C에서 2분간 최종 교반',
                    '1. 완제품 성형틀에 부어넣기\n2. 건조실에 배치 (온도: 30°C, 습도: 50%)\n3. 12시간 경화 대기\n4. 2주간 건조 완료 후 경화 상태 확인',
                    '1. 완제품 포장재로 개별 포장\n2. 제품 라벨 부착 (제품명, 성분, 유통기한)\n3. 품질 검사 (외관, 무게, 포장 상태)\n4. 최종 포장 박스에 정리하여 출고'
                ];
                return instructions[stepNumber - 1] || '';
            }
        }
        
        // 제품 선택 시 기본 정보 업데이트
        function updateBasicInfo() {
            const standardCodeSelect = document.getElementById('standardCode');
            const routingTypeSelect = document.getElementById('routingType');
            
            if (standardCodeSelect && routingTypeSelect) {
                const selectedOption = standardCodeSelect.options[standardCodeSelect.selectedIndex];
                const routingType = routingTypeSelect.value;
                
                // 기본 정보 업데이트
                const infoValues = document.querySelectorAll('.info-value');
                if (infoValues.length >= 4) {
                    if (selectedOption.value && routingType) {
                        // 제품명 추출 (예: "PRD001 - 오이 각질제거비누"에서 "오이 각질제거비누" 추출)
                        const productName = selectedOption.text.split(' - ')[1] || selectedOption.text;
                        
                        infoValues[0].textContent = selectedOption.value; // 제품코드
                        infoValues[1].textContent = productName; // 제품명
                        infoValues[2].textContent = routingType === 'SEMI' ? '반제품' : '완제품'; // 라우팅 타입
                        infoValues[3].textContent = routingType === 'SEMI' ? '4단계' : '8단계'; // 총 공정 단계
                    } else if (routingType) {
                        // 라우팅 타입만 선택된 경우
                        infoValues[0].textContent = '선택 필요';
                        infoValues[1].textContent = '선택 필요';
                        infoValues[2].textContent = routingType === 'SEMI' ? '반제품' : '완제품';
                        infoValues[3].textContent = routingType === 'SEMI' ? '4단계' : '8단계';
                    } else {
                        // 아무것도 선택되지 않은 경우
                        infoValues[0].textContent = '선택 필요';
                        infoValues[1].textContent = '선택 필요';
                        infoValues[2].textContent = '선택 필요';
                        infoValues[3].textContent = '선택 필요';
                    }
                }
            }
        }
        
        // 페이지 로드 시 기존 값이 있으면 미리보기 표시
        document.addEventListener('DOMContentLoaded', function() {
            const routingType = document.getElementById('routingType').value;
            const standardCode = document.getElementById('standardCode').value;
            const isUpdateMode = document.querySelector('[data-step]') !== null; // 수정 모드 확인
            
            // 수정 모드인 경우 기본 정보 업데이트
            if (routingType || standardCode) {
                updateBasicInfo();
            }
            
            // 수정 모드이거나 라우팅 타입이 선택된 경우 컨트롤 버튼 표시
            if (isUpdateMode || routingType) {
                const addStepBtn = document.getElementById('addStepBtn');
                const resetStepsBtn = document.getElementById('resetStepsBtn');
                if (addStepBtn) addStepBtn.style.display = 'inline-block';
                if (resetStepsBtn) resetStepsBtn.style.display = 'inline-block';
            }
            
            if (routingType) {
                document.getElementById('routingType').dispatchEvent(new Event('change'));
            }
            
            // 제품 선택 이벤트 리스너 (수정 모드에서는 readonly이지만 change 이벤트는 발생할 수 있음)
            const standardCodeSelect = document.getElementById('standardCode');
            if (standardCodeSelect) {
                standardCodeSelect.addEventListener('change', updateBasicInfo);
            }
            
            // 라우팅 타입 변경 이벤트 리스너 (수정 모드에서도 적용)
            const routingTypeSelect = document.getElementById('routingType');
            if (routingTypeSelect) {
                routingTypeSelect.addEventListener('change', function() {
                    // 기본 정보 업데이트
                    updateBasicInfo();
                    
                    // 수정 모드에서도 공정 단계 업데이트
                    if (isUpdateMode) {
                        const detail = document.getElementById('processStepsDetail');
                        if (detail) {
                            const currentRoutingType = this.value;
                            if (currentRoutingType === 'SEMI') {
                                // 기존 데이터를 보존하면서 반제품 단계로 변경
                                updateProcessStepsForType('SEMI');
                            } else if (currentRoutingType === 'FINISH') {
                                // 기존 데이터를 보존하면서 완제품 단계로 변경
                                updateProcessStepsForType('FINISH');
                            } else {
                                detail.innerHTML = '<p>라우팅 타입을 선택하면 공정 단계별 상세 정보가 표시됩니다.</p>';
                            }
                        }
                    }
                });
            }
            
            // 컨트롤 버튼 이벤트 리스너
            const addStepBtn = document.getElementById('addStepBtn');
            const resetStepsBtn = document.getElementById('resetStepsBtn');
            if (addStepBtn) {
                addStepBtn.addEventListener('click', addStep);
            }
            if (resetStepsBtn) {
                resetStepsBtn.addEventListener('click', resetSteps);
            }
        });
        
        // 수정 모드에서 기존 데이터로 공정 단계 생성
        function generateUpdateProcessSteps() {
            const processSteps = [
                <c:forEach var="step" items="${processSteps}" varStatus="status">
                {
                    prOrder: ${step.prOrder},
                    prDescription: '${step.prDescription}',
                    prType: '${step.prType}',
                    materials: '',
                    instructions: ''
                }<c:if test="${!status.last}">,</c:if>
                </c:forEach>
            ];
            
            const detail = document.getElementById('processStepsDetail');
            if (detail) {
                detail.innerHTML = generateProcessStepsFromData(processSteps);
            }
        }
        
        // 기존 데이터를 사용하여 공정 단계 HTML 생성
        function generateProcessStepsFromData(processSteps) {
            let html = '';
            processSteps.forEach(step => {
                const cardClass = step.prOrder <= 4 ? 'step-semi' : 'step-finish';
                const materials = getDefaultMaterials(step.prOrder, step.prOrder <= 4 ? 'SEMI' : 'FINISH');
                const instructions = getDefaultInstructions(step.prOrder, step.prOrder <= 4 ? 'SEMI' : 'FINISH');
                
                html += '<div class="step-card ' + cardClass + '" data-step="' + step.prOrder + '">';
                html += '<div class="step-header">';
                html += '<div class="step-number">' + step.prOrder + '</div>';
                html += '<div class="step-info">';
                html += '<input type="text" name="stepName_' + step.prOrder + '" value="' + step.prDescription + '" class="step-name-input" placeholder="단계명">';
                html += '<select name="stepType_' + step.prOrder + '" class="step-type-select">';
                html += '<option value="혼합" ' + (step.prType === '혼합' ? 'selected' : '') + '>혼합</option>';
                html += '<option value="성형" ' + (step.prType === '성형' ? 'selected' : '') + '>성형</option>';
                html += '<option value="건조" ' + (step.prType === '건조' ? 'selected' : '') + '>건조</option>';
                html += '<option value="포장" ' + (step.prType === '포장' ? 'selected' : '') + '>포장</option>';
                html += '</select>';
                html += '</div>';
                html += '<button type="button" class="btn-remove-step" onclick="removeStep(' + step.prOrder + ')">×</button>';
                html += '</div>';
                html += '<div class="step-content">';
                html += '<div class="materials-section">';
                html += '<h5>필요 자재</h5>';
                html += '<div class="materials-input">';
                html += '<textarea name="materials_' + step.prOrder + '" class="materials-textarea" placeholder="자재를 입력하세요 (한 줄에 하나씩)">' + materials + '</textarea>';
                html += '</div>';
                html += '</div>';
                html += '<div class="work-instruction">';
                html += '<h5>작업 지침</h5>';
                html += '<div class="instruction-input">';
                html += '<textarea name="instructions_' + step.prOrder + '" class="instruction-textarea" placeholder="작업 지침을 입력하세요 (한 줄에 하나씩)">' + instructions + '</textarea>';
                html += '</div>';
                html += '</div>';
                html += '</div>';
                html += '</div>';
            });
            return html;
        }
        
        // 수정 모드 초기화
        if (isUpdateMode) {
            document.addEventListener('DOMContentLoaded', function() {
                generateUpdateProcessSteps();
            });
        }
    </script>
    <script src="${pageContext.request.contextPath}/src/js/process_form.js"></script>
</body>
</html>

