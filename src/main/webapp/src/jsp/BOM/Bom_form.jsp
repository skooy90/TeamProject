<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BOM 등록/수정 - MES 시스템</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/src/Header_Sied/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/src/css/bom_form.css">
</head>
<body>
    <jsp:include page="../../Header_Sied/header.jsp" />
    
    <div class="main-container">
        <jsp:include page="../../Header_Sied/sidebar.jsp" />
        
        <div class="content-area">
            <div class="container">
                <!-- 페이지 헤더 -->
                <div class="page-header">
                    <h1 class="page-title">${mode == 'update' ? 'BOM 수정' : 'BOM 등록'}</h1>
                    <a href="${pageContext.request.contextPath}/bom" class="back-btn">목록으로</a>
                </div>

                <form id="bomForm" method="post" action="${pageContext.request.contextPath}/bom/${mode == 'update' ? 'update' : 'insert'}">
                    <!-- 기본 정보 섹션 -->
                    <div class="form-section">
                        <h2 class="section-title">기본 정보</h2>
                        
                        <div class="form-row">
                            <div class="form-group">
                                <label for="bomNo">BOM번호 *</label>
                                <input type="text" id="bomNo" name="bomNo" 
                                       value="${mode == 'update' ? bom.bomNo : '자동 생성'}"
                                       readonly required>
                                <div class="error-message" id="bomNoError"></div>
                            </div>
                            
                            <div class="form-group">
                                <label for="standardCode">제품코드 *</label>
                                <c:choose>
                                    <c:when test="${mode == 'update'}">
                                        <input type="text" id="standardCode" name="standardCode" value="${bom.standardCode} - ${bom.stName}" readonly>
                                    </c:when>
                                    <c:otherwise>
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
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="bomDescription">BOM 설명 *</label>
                                <input type="text" id="bomDescription" name="bomDescription" 
                                       value="${mode == 'update' ? bom.bomDescription : ''}"
                                       placeholder="BOM 설명을 입력하세요" required>
                                <div class="error-message" id="bomDescriptionError"></div>
                            </div>
                            
                            <div class="form-group">
                                <label for="bomType">BOM 유형 *</label>
                                <select id="bomType" name="bomType" required>
                                    <option value="">유형을 선택하세요</option>
                                    <option value="원자재" ${mode == 'update' && bom.bomType == '원자재' ? 'selected' : ''}>원자재</option>
                                    <option value="반제품" ${mode == 'update' && bom.bomType == '반제품' ? 'selected' : ''}>반제품</option>
                                    <option value="완제품" ${mode == 'update' && bom.bomType == '완제품' ? 'selected' : ''}>완제품</option>
                                </select>
                                <div class="error-message" id="bomTypeError"></div>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="bomOrder">BOM 순서 *</label>
                                <input type="number" id="bomOrder" name="bomOrder" 
                                       value="${mode == 'update' ? bom.bomOrder : ''}"
                                       min="1" placeholder="1" required>
                                <div class="error-message" id="bomOrderError"></div>
                            </div>
                            
                            <div class="form-group">
                                <label for="bomImage">BOM 이미지</label>
                                <input type="file" id="bomImage" name="bomImage" accept="image/*">
                                <div class="error-message" id="bomImageError"></div>
                            </div>
                        </div>
                    </div>

                    <!-- 자재 정보 섹션 -->
                    <div class="materials-section">
                        <div class="materials-header">
                            <h2 class="section-title">자재 정보</h2>
                            <button type="button" class="add-material-btn" onclick="addMaterialRow()">자재 추가</button>
                        </div>

                        <table class="materials-table">
                            <thead>
                                <tr>
                                    <th>자재코드</th>
                                    <th>자재명</th>
                                    <th>필요수량</th>
                                    <th>단위</th>
                                    <th>비고</th>
                                    <th>관리</th>
                                </tr>
                            </thead>
                            <tbody id="materialsTableBody">
                                <c:choose>
                                    <c:when test="${mode == 'update'}">
                                        <!-- 수정 모드: 기존 자재 데이터 표시 -->
                                        <c:forEach var="material" items="${materials}" varStatus="status">
                                        <tr class="material-row">
                                            <td>
                                                <select name="materialCode" required>
                                                    <option value="">자재를 선택하세요</option>
                                                    <c:forEach var="allMaterial" items="${allMaterials}">
                                                        <option value="${allMaterial.materialCode}" 
                                                                ${material.materialCode == allMaterial.materialCode ? 'selected' : ''}>
                                                            ${allMaterial.materialCode} - ${allMaterial.materialName}
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </td>
                                            <td><input type="text" value="${material.materialName}" readonly></td>
                                            <td><input type="number" name="quantity" value="${material.bomQuantity}" step="0.1" min="0.1" required></td>
                                            <td><input type="text" value="${material.unit}" readonly></td>
                                            <td><input type="text" name="remark" value="${material.remark}" placeholder="비고"></td>
                                            <td><button type="button" class="remove-material-btn" onclick="removeMaterialRow(this)">삭제</button></td>
                                        </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <!-- 등록 모드: 빈 자재 행 -->
                                        <tr class="material-row">
                                            <td>
                                                <select name="materialCode" required>
                                                    <option value="">자재를 선택하세요</option>
                                                    <c:forEach var="material" items="${allMaterials}">
                                                        <option value="${material.materialCode}">${material.materialCode} - ${material.materialName}</option>
                                                    </c:forEach>
                                                </select>
                                            </td>
                                            <td><input type="text" readonly></td>
                                            <td><input type="number" name="quantity" step="0.1" min="0.1" required></td>
                                            <td><input type="text" readonly></td>
                                            <td><input type="text" name="remark" placeholder="비고"></td>
                                            <td><button type="button" class="remove-material-btn" onclick="removeMaterialRow(this)">삭제</button></td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
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
        // 서버에서 전달받은 자재 데이터 사용
        const materialData = {
            <c:forEach var="material" items="${allMaterials}" varStatus="status">
            '${material.materialCode}': { name: '${material.materialName}', unit: '${material.unit}' }<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        };

        // 자재 행 추가
        function addMaterialRow() {
            const tbody = document.getElementById('materialsTableBody');
            const newRow = document.createElement('tr');
            newRow.className = 'material-row';
            
            // 서버에서 전달받은 자재 목록으로 옵션 생성
            let optionsHtml = '<option value="">자재를 선택하세요</option>';
            for (const [code, data] of Object.entries(materialData)) {
                optionsHtml += `<option value="${code}">${code} - ${data.name}</option>`;
            }
            
            newRow.innerHTML = `
                <td>
                    <select name="materialCode" required onchange="updateMaterialInfo(this)">
                        ${optionsHtml}
                    </select>
                </td>
                <td><input type="text" readonly></td>
                <td><input type="number" name="quantity" step="0.1" min="0.1" required></td>
                <td><input type="text" readonly></td>
                <td><input type="text" name="remark" placeholder="비고"></td>
                <td><button type="button" class="remove-material-btn" onclick="removeMaterialRow(this)">삭제</button></td>
            `;
            
            tbody.appendChild(newRow);
        }

        // 자재 행 삭제
        function removeMaterialRow(button) {
            const tbody = document.getElementById('materialsTableBody');
            const rows = tbody.querySelectorAll('.material-row');
            
            if (rows.length > 1) {
                button.closest('tr').remove();
            } else {
                alert('최소 1개의 자재는 필요합니다.');
            }
        }

        // 자재 정보 업데이트
        function updateMaterialInfo(selectElement) {
            const row = selectElement.closest('tr');
            const materialCode = selectElement.value;
            const nameInput = row.querySelector('td:nth-child(2) input');
            const unitInput = row.querySelector('td:nth-child(4) input');
            
            if (materialCode && materialData[materialCode]) {
                nameInput.value = materialData[materialCode].name;
                unitInput.value = materialData[materialCode].unit;
            } else {
                nameInput.value = '';
                unitInput.value = '';
            }
        }

        // 기존 자재 선택 이벤트 리스너 추가
        document.addEventListener('DOMContentLoaded', function() {
            const materialSelects = document.querySelectorAll('select[name="materialCode"]');
            materialSelects.forEach(select => {
                select.addEventListener('change', function() {
                    updateMaterialInfo(this);
                });
            });
        });

        // 폼 유효성 검사
        document.getElementById('bomForm').addEventListener('submit', function(e) {
            let isValid = true;
            
            // 필수 필드 검사
            const requiredFields = ['standardCode', 'bomDescription', 'bomType', 'bomOrder'];
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
            
            // BOM 순서 검사
            const bomOrder = document.getElementById('bomOrder');
            const bomOrderError = document.getElementById('bomOrderError');
            if (bomOrder.value && (isNaN(bomOrder.value) || parseInt(bomOrder.value) < 1)) {
                bomOrderError.textContent = '1 이상의 숫자를 입력하세요.';
                bomOrderError.style.display = 'block';
                isValid = false;
            } else {
                bomOrderError.style.display = 'none';
            }
            
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
