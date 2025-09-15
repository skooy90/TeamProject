<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BOM 등록/수정 - MES 시스템</title>
       <c:set var="ctx" value="${pageContext.request.contextPath}" />
    
  <link rel="stylesheet" href="${ctx}/css/bom_form.css?v=1.1" type="text/css">
  <link rel="stylesheet" href="${ctx}/Header_Side/style.css" type="text/css">
</head>
<body>
    <jsp:include page="../../Header_Side/header.jsp" />
    
    <div class="main-container">
        <jsp:include page="../../Header_Side/sidebar.jsp" />
        
        <div class="content-area">
            <div class="container">
                <!-- 페이지 헤더 -->
                <div class="detail-header">
                    <div class="header-info">
                        <h1>${mode == 'update' ? 'BOM 수정' : 'BOM 등록'}</h1>
                        <span class="routing-type">${mode == 'update' ? (bom.bomType == '원자재' ? '원자재 BOM' : bom.bomType == '반제품' ? '반제품 BOM' : '완제품 BOM') : 'BOM 등록'}</span>
                    </div>
                    <div class="header-actions">
                        <a href="${pageContext.request.contextPath}/bom" class="btn btn-secondary">목록으로</a>
                    </div>
                </div>

                <!-- 기본 정보 카드 -->
                <div class="grid-container">
                    <div class="info-card">
                        <h3>BOM 기본 정보</h3>
                        <div class="info-item">
                            <span class="info-label">BOM 번호</span>
                            <span class="info-value">${mode == 'update' ? bom.bomNo : '자동 생성'}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">제품 코드</span>
                            <span class="info-value">${mode == 'update' ? bom.standardCode : '선택 필요'}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">제품명</span>
                            <span class="info-value">${mode == 'update' ? bom.stName : '선택 필요'}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">BOM 유형</span>
                            <span class="info-value">${mode == 'update' ? bom.bomType : '선택 필요'}</span>
                        </div>
                    </div>
                    
                    <div class="chart-card">
                        <h3>자재 사용량 미리보기</h3>
                        <div id="materialsPreview" class="chart-container">
                            <p>제품을 선택하면 자재 사용량이 표시됩니다.</p>
                        </div>
                    </div>
                </div>

                <form id="bomForm" method="post" action="${pageContext.request.contextPath}/bom/${mode == 'update' ? 'update' : 'insert'}">
                    <!-- 기본 정보 섹션 -->
                    <div class="form-header">
                        <h2>BOM 기본 정보</h2>
                        <c:if test="${mode != 'update'}">
                    <p class="esse"> * 필수 선택사항입니다.. </p>
                        </c:if>
                    </div>
                        
                    <div class="form-container">
                            <div class="form-group">
                                <label for="bomNo">BOM번호 </label>
                                <input type="text" id="bomNo" name="bomNo" 
                                       value="${mode == 'update' ? bom.bomNo : '자동 생성'}"
                                       readonly required>
                                <div class="error-message" id="bomNoError"></div>
                            </div>
                            
                            <div class="form-group">
                                <c:choose>
                                    <c:when test="${mode == 'update'}">
                                <label for="standardCode">제품코드 </label>
                                        <input type="text" id="standardCode" name="standardCode" value="${bom.standardCode} - ${bom.stName}" readonly>
                                    </c:when>
                                    <c:otherwise>
                                <label for="standardCode">제품코드 * </label>
                                        <select id="standardCode" name="standardCode" required>
                                            <option value="">제품을 선택하세요</option>
                                            <c:forEach var="standard" items="${standards}">
                                                <option value="${standard.standardCode}">${standard.standardCode} - ${standard.stName}</option>
                                            </c:forEach>
                                        </select>
                                    </c:otherwise>
                                </c:choose>
                                <div class="error-message" id="standardCodeError"></div>
                        </div>

                            <div class="form-group">
                            <c:choose>
                                <c:when test="${mode == 'update'}">
                                <label for="bomType">BOM 유형 </label>
                                    <input type="text" id="bomType" name="bomType" value="${bom.bomType}" readonly>
                                </c:when>
                                <c:otherwise>
                                <select id="bomType" name="bomType" required>
                                <label for="bomType">BOM 유형 *</label>
                                    <option value="">유형을 선택하세요</option>
                                        <option value="원자재">원자재</option>
                                        <option value="반제품">반제품</option>
                                        <option value="완제품">완제품</option>
                                </select>
                                </c:otherwise>
                            </c:choose>
                                <div class="error-message" id="bomTypeError"></div>
                        </div>
                            
                            <div class="form-group">
                                <label for="bomImage">BOM 이미지</label>
                                <input type="file" id="bomImage" name="bomImage" accept="image/*">
                                <div class="error-message" id="bomImageError"></div>
                        </div>
                    </div>

                    <!-- 자재 정보 섹션 -->
                    <div class="materials-section">
                        <div class="materials-header">
                            <div>
                                <h2>제품별 자재 사용량 ${mode == 'update' ? '(수정)' : ''}</h2>
                                <c:if test="${mode == 'update'}">
                                    <p style="margin: 5px 0 0 0; color: #666; font-size: 0.9em;">
                                        기존 자재를 수정하거나 새로운 자재를 추가할 수 있습니다.
                                    </p>
                                </c:if>
                            </div>
                            <div class="materials-controls">
                                <button type="button" class="btn btn-primary" onclick="addMaterialRow()">
                                    <i class="fas fa-plus"></i> 자재 추가
                                </button>
                                <c:if test="${mode != 'update'}">
                                    <button type="button" class="btn btn-secondary" onclick="resetMaterials()">
                                        <i class="fas fa-undo"></i> 기본값으로 초기화
                                    </button>
                                </c:if>
                            </div>
                        </div>

                        <div id="materialsDetail" class="materials-detail">
                                <c:choose>
                                <c:when test="${mode == 'update'}">
                                    <!-- 수정 모드: 기존 자재 데이터 표시 -->
                                    <div id="updateMaterials">
                                        <div class="loading-message">
                                            <p>기존 자재 데이터를 불러오는 중...</p>
                                        </div>
                                    </div>
                                </c:when>
                                    <c:otherwise>
                                    <!-- 등록 모드: 기본 메시지 -->
                                    <p>제품을 선택하면 자재 사용량을 설정할 수 있습니다.</p>
                                    </c:otherwise>
                                </c:choose>
                        </div>
                    </div>

                    <!-- 버튼 영역 -->
                    <div class="button-section">
                        <button type="submit" class="btn btn-primary">${mode == 'update' ? '수정' : '등록'}</button>
                        <a href="${pageContext.request.contextPath}/bom" class="btn btn-secondary">취소</a>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        // 모드 확인
        const isUpdateMode = ${mode == 'update'};
        
        // 서버에서 전달받은 자재 데이터 사용
        const materialData = {
            <c:forEach var="material" items="${allMaterials}" varStatus="status">
            '${material.materialCode}': { 
                name: '${material.materialName}', 
                unit: '${material.unit}' 
            }<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        };
        
        // 기본 정보 업데이트 함수
        function updateBasicInfo() {
            const standardCode = document.getElementById('standardCode');
            const bomType = document.getElementById('bomType');
            
            if (standardCode && standardCode.value) {
                const selectedOption = standardCode.options[standardCode.selectedIndex];
                const productName = selectedOption.text.split(' - ')[1] || '선택 필요';
                document.querySelector('.info-card .info-item:nth-child(3) .info-value').textContent = productName;
                document.querySelector('.info-card .info-item:nth-child(2) .info-value').textContent = standardCode.value;
            }
            
            if (bomType && bomType.value) {
                document.querySelector('.info-card .info-item:nth-child(4) .info-value').textContent = bomType.value;
            }
            
            // 자재 미리보기 업데이트
            updateMaterialsPreview();
            
            // 수정 모드에서도 자재 섹션 업데이트
            if (isUpdateMode) {
                const detail = document.getElementById('materialsDetail');
                if (detail && standardCode && standardCode.value && bomType && bomType.value) {
                    // 기본 자재 데이터로 초기화
                    generateDefaultMaterials();
                }
            }
        }
        
        // 자재 미리보기 업데이트
        function updateMaterialsPreview() {
            const preview = document.getElementById('materialsPreview');
            const standardCode = document.getElementById('standardCode');
            const bomType = document.getElementById('bomType');
            
            if (standardCode && standardCode.value && bomType && bomType.value) {
                const materials = getDefaultMaterials(standardCode.value, bomType.value);
                preview.innerHTML = materials;
            } else {
                preview.innerHTML = '<p>제품을 선택하면 자재 사용량이 표시됩니다.</p>';
            }
        }
        
        // 기본 자재 데이터 반환
        function getDefaultMaterials(standardCode, bomType) {
            if (bomType === '원자재') {
                return '<div class="material-preview">' +
                    '<h4>원자재 BOM</h4>' +
                    '<ul>' +
                        '<li>신선 오이 - 2kg</li>' +
                        '<li>가성소다 - 200g</li>' +
                        '<li>팜 오일 - 500ml</li>' +
                        '<li>올리브 오일 - 300ml</li>' +
                    '</ul>' +
                '</div>';
            } else if (bomType === '반제품') {
                return '<div class="material-preview">' +
                    '<h4>반제품 BOM</h4>' +
                    '<ul>' +
                        '<li>신선 오이 - 2kg</li>' +
                        '<li>가성소다 - 200g</li>' +
                        '<li>팜 오일 - 500ml</li>' +
                        '<li>올리브 오일 - 300ml</li>' +
                        '<li>살리실산 - 50g</li>' +
                        '<li>과일산 - 40g</li>' +
                    '</ul>' +
                '</div>';
            } else if (bomType === '완제품') {
                return '<div class="material-preview">' +
                    '<h4>완제품 BOM</h4>' +
                    '<ul>' +
                        '<li>신선 오이 - 2kg</li>' +
                        '<li>가성소다 - 200g</li>' +
                        '<li>팜 오일 - 500ml</li>' +
                        '<li>올리브 오일 - 300ml</li>' +
                        '<li>살리실산 - 50g</li>' +
                        '<li>과일산 - 40g</li>' +
                        '<li>아연 PCA - 60g</li>' +
                        '<li>티트리 오일 - 30ml</li>' +
                    '</ul>' +
                '</div>';
            }
            return '<p>제품을 선택하면 자재 사용량이 표시됩니다.</p>';
        }

        // 자재 행 추가
        function addMaterialRow() {
            const detail = document.getElementById('materialsDetail');
            if (!detail) return;
            
            // 수정 모드에서는 기존 자재 섹션에 새 자재 추가
            if (isUpdateMode) {
                const updateDiv = document.getElementById('updateMaterials');
                if (updateDiv) {
                    // 기존 자재 섹션이 없다면 생성
                    if (!updateDiv.querySelector('.existing-materials')) {
                        updateDiv.innerHTML = '<div class="existing-materials"><h4>현재 등록된 자재 사용량</h4></div>';
                    }
                    
                    // 새 자재 카드 추가
                    const newMaterialCard = document.createElement('div');
                    newMaterialCard.className = 'material-card new-material';
                    newMaterialCard.innerHTML = generateMaterialCard();
                    
                    updateDiv.appendChild(newMaterialCard);
                }
            } else {
                // 등록 모드에서는 기존 방식대로 추가
                const materialCard = document.createElement('div');
                materialCard.className = 'material-card';
                materialCard.innerHTML = generateMaterialCard();
                
                detail.appendChild(materialCard);
            }
        }
        
        // 자재 카드 생성
        function generateMaterialCard() {
            let materialOptions = '';
            for (const code in materialData) {
                if (materialData.hasOwnProperty(code)) {
                    materialOptions += '<option value="' + code + '">' + code + ' - ' + materialData[code].name + '</option>';
                }
            }
            
            return '<div class="material-header">' +
                '<div class="material-info">' +
                    '<select name="materialCode" required onchange="updateMaterialInfo(this)">' +
                        '<option value="">자재를 선택하세요</option>' +
                        materialOptions +
                    '</select>' +
                    '<input type="text" name="materialName" readonly placeholder="자재명">' +
                '</div>' +
                '<button type="button" class="btn-remove-material" onclick="removeMaterialCard(this)">×</button>' +
            '</div>' +
            '<div class="material-content">' +
                '<div class="material-details">' +
                    '<div class="detail-item">' +
                        '<label>필요수량</label>' +
                        '<input type="number" name="quantity" step="0.1" min="0.1" required placeholder="수량">' +
                    '</div>' +
                    '<div class="detail-item">' +
                        '<label>단위</label>' +
                        '<input type="text" name="unit" readonly placeholder="단위">' +
                    '</div>' +
                    '<div class="detail-item">' +
                        '<label>비고</label>' +
                        '<input type="text" name="remark" placeholder="비고">' +
                    '</div>' +
                '</div>' +
            '</div>';
        }

        // 자재 카드 삭제
        function removeMaterialCard(button) {
            const detail = document.getElementById('materialsDetail');
            const cards = detail.querySelectorAll('.material-card');
            
            if (cards.length > 1) {
                button.closest('.material-card').remove();
            } else {
                alert('최소 1개의 자재는 필요합니다.');
            }
        }

        // 자재 정보 업데이트
        function updateMaterialInfo(selectElement) {
            const card = selectElement.closest('.material-card');
            const materialCode = selectElement.value;
            const nameInput = card.querySelector('input[name="materialName"]');
            const unitInput = card.querySelector('input[name="unit"]');
            
            if (materialCode && materialData[materialCode]) {
                nameInput.value = materialData[materialCode].name;
                unitInput.value = materialData[materialCode].unit;
            } else {
                nameInput.value = '';
                unitInput.value = '';
            }
        }

        // 자재 초기화
        function resetMaterials() {
            const detail = document.getElementById('materialsDetail');
            const bomType = document.getElementById('bomType');
            
            if (bomType && bomType.value) {
                detail.innerHTML = generateDefaultMaterials(bomType.value);
            } else {
                detail.innerHTML = '<p>BOM 유형을 선택하면 기본 자재가 표시됩니다.</p>';
            }
            
            // 수정 모드에서도 미리보기 업데이트
            updateMaterialsPreview();
        }
        
        // 기본 자재 생성
        function generateDefaultMaterials(bomType) {
            let materials = '';
            
            if (bomType === '원자재') {
                const defaultMaterials = [
                    { code: 'RA001', name: '신선 오이', unit: 'kg', quantity: 2 },
                    { code: 'RA002', name: '가성소다', unit: 'g', quantity: 200 },
                    { code: 'RA003', name: '팜 오일', unit: 'ml', quantity: 500 },
                    { code: 'RA004', name: '올리브 오일', unit: 'ml', quantity: 300 }
                ];
                
                materials = defaultMaterials.map(mat => generateMaterialCardWithData(mat)).join('');
            } else if (bomType === '반제품') {
                const defaultMaterials = [
                    { code: 'RA001', name: '신선 오이', unit: 'kg', quantity: 2 },
                    { code: 'RA002', name: '가성소다', unit: 'g', quantity: 200 },
                    { code: 'RA003', name: '팜 오일', unit: 'ml', quantity: 500 },
                    { code: 'RA004', name: '올리브 오일', unit: 'ml', quantity: 300 },
                    { code: 'RA005', name: '살리실산', unit: 'g', quantity: 50 },
                    { code: 'RA006', name: '과일산', unit: 'g', quantity: 40 }
                ];
                
                materials = defaultMaterials.map(mat => generateMaterialCardWithData(mat)).join('');
            } else if (bomType === '완제품') {
                const defaultMaterials = [
                    { code: 'RA001', name: '신선 오이', unit: 'kg', quantity: 2 },
                    { code: 'RA002', name: '가성소다', unit: 'g', quantity: 200 },
                    { code: 'RA003', name: '팜 오일', unit: 'ml', quantity: 500 },
                    { code: 'RA004', name: '올리브 오일', unit: 'ml', quantity: 300 },
                    { code: 'RA005', name: '살리실산', unit: 'g', quantity: 50 },
                    { code: 'RA006', name: '과일산', unit: 'g', quantity: 40 },
                    { code: 'RA007', name: '아연 PCA', unit: 'g', quantity: 60 },
                    { code: 'RA008', name: '티트리 오일', unit: 'ml', quantity: 30 }
                ];
                
                materials = defaultMaterials.map(mat => generateMaterialCardWithData(mat)).join('');
            }
            
            return materials;
        }
        
        // 데이터가 포함된 자재 카드 생성
        function generateMaterialCardWithData(material) {
            let materialOptions = '';
            for (const code in materialData) {
                if (materialData.hasOwnProperty(code)) {
                    const selected = code === material.code ? ' selected' : '';
                    materialOptions += '<option value="' + code + '"' + selected + '>' + code + ' - ' + materialData[code].name + '</option>';
                }
            }
            
            return '<div class="material-card">' +
                '<div class="material-header">' +
                    '<div class="material-info">' +
                        '<select name="materialCode" required onchange="updateMaterialInfo(this)">' +
                            '<option value="">자재를 선택하세요</option>' +
                            materialOptions +
                        '</select>' +
                        '<input type="text" name="materialName" readonly value="' + material.name + '">' +
                    '</div>' +
                    '<button type="button" class="btn-remove-material" onclick="removeMaterialCard(this)">×</button>' +
                '</div>' +
                '<div class="material-content">' +
                    '<div class="material-details">' +
                        '<div class="detail-item">' +
                            '<label>필요수량</label>' +
                            '<input type="number" name="quantity" step="0.1" min="0.1" required value="' + material.quantity + '">' +
                        '</div>' +
                        '<div class="detail-item">' +
                            '<label>단위</label>' +
                            '<input type="text" name="unit" readonly value="' + material.unit + '">' +
                        '</div>' +
                        '<div class="detail-item">' +
                            '<label>비고</label>' +
                            '<input type="text" name="remark" placeholder="비고" value="' + (material.remark || '') + '">' +
                        '</div>' +
                    '</div>' +
                '</div>' +
            '</div>';
        }

        // 이벤트 리스너 추가
        document.addEventListener('DOMContentLoaded', function() {
            // 제품 선택 이벤트
            const standardCodeSelect = document.getElementById('standardCode');
            if (standardCodeSelect) {
                standardCodeSelect.addEventListener('change', updateBasicInfo);
            }
            
            // BOM 유형 선택 이벤트
            const bomTypeSelect = document.getElementById('bomType');
            if (bomTypeSelect) {
                bomTypeSelect.addEventListener('change', function() {
                    updateBasicInfo();
                    resetMaterials();
                });
            }
            
            // 수정 모드 초기화
            if (isUpdateMode) {
                // 수정 모드에서는 기존 자재 데이터를 표시
                generateUpdateMaterials();
            }
        });
        
        // 수정 모드에서 기존 자재 데이터 생성
        function generateUpdateMaterials() {
            const detail = document.getElementById('materialsDetail');
            if (!detail) return;
            
            // 수정 모드에서는 기존 자재 데이터를 표시
            const updateDiv = document.getElementById('updateMaterials');
            if (updateDiv) {
                // 기존 자재 데이터가 있다면 표시, 없다면 안내 메시지
                const existingMaterials = getExistingMaterials();
                if (existingMaterials.length > 0) {
                    updateDiv.innerHTML = generateExistingMaterialsHTML(existingMaterials);
                } else {
                    updateDiv.innerHTML = '<p>현재 등록된 자재가 없습니다. "자재 추가" 버튼을 클릭하여 자재를 추가하세요.</p>';
                }
            }
        }
        
        // 기존 자재 데이터 가져오기 (실제로는 서버에서 받아와야 함)
        function getExistingMaterials() {
            // 실제 구현에서는 서버에서 기존 자재 데이터를 받아와야 함
            // 현재는 예시 데이터를 반환
            return [
                { code: 'RA001', name: '신선 오이', unit: 'kg', quantity: 2, remark: '신선한 오이' },
                { code: 'RA002', name: '가성소다', unit: 'g', quantity: 200, remark: '화학 원료' },
                { code: 'RA003', name: '팜 오일', unit: 'ml', quantity: 500, remark: '식용유' }
            ];
        }
        
        // 기존 자재 HTML 생성
        function generateExistingMaterialsHTML(materials) {
            let html = '<div class="existing-materials">';
            html += '<h4>현재 등록된 자재 사용량</h4>';
            
            materials.forEach((material, index) => {
                html += '<div class="material-card existing-material">';
                html += '<div class="material-header">';
                html += '<div class="material-info">';
                html += '<select name="materialCode" required onchange="updateMaterialInfo(this)">';
                html += '<option value="">자재를 선택하세요</option>';
                
                // 자재 옵션 생성
                for (const code in materialData) {
                    if (materialData.hasOwnProperty(code)) {
                        const selected = material.code === code ? 'selected' : '';
                        html += '<option value="' + code + '" ' + selected + '>' + code + ' - ' + materialData[code].name + '</option>';
                    }
                }
                
                html += '</select>';
                html += '<input type="text" name="materialName" readonly value="' + material.name + '">';
                html += '</div>';
                html += '<button type="button" class="btn-remove-material" onclick="removeMaterialCard(this)">×</button>';
                html += '</div>';
                html += '<div class="material-content">';
                html += '<div class="material-details">';
                html += '<div class="detail-item">';
                html += '<label>필요수량</label>';
                html += '<input type="number" name="quantity" step="0.1" min="0.1" required value="' + material.quantity + '">';
                html += '</div>';
                html += '<div class="detail-item">';
                html += '<label>단위</label>';
                html += '<input type="text" name="unit" readonly value="' + material.unit + '">';
                html += '</div>';
                html += '<div class="detail-item">';
                html += '<label>비고</label>';
                html += '<input type="text" name="remark" placeholder="비고" value="' + (material.remark || '') + '">';
                html += '</div>';
                html += '</div>';
                html += '</div>';
                html += '</div>';
            });
            
            html += '</div>';
            return html;
        }

        // 폼 유효성 검사
        document.getElementById('bomForm').addEventListener('submit', function(e) {
            let isValid = true;
            
            // 필수 필드 검사
            const requiredFields = ['standardCode', 'bomType'];
            requiredFields.forEach(fieldId => {
                const field = document.getElementById(fieldId);
                const errorDiv = document.getElementById(fieldId + 'Error');
                
                if (!field.value.trim()) {
                    errorDiv.textContent = '필수 입력 항목입니다.';
                    errorDiv.style.display = 'block';
                    isValid = false;
                } else {
                    errorDiv.style.display = 'none';
                }
            });
            
            // 자재 검사
            const materialRows = document.querySelectorAll('.material-row');
            if (materialRows.length === 0) {
                alert('최소 1개의 자재를 추가해주세요.');
                isValid = false;
            }
            
            if (!isValid) {
                e.preventDefault();
            }
        });
    </script>
</body>
</html>