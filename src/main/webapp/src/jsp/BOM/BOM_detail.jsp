<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BOM 상세 - MES 시스템</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/src/Header_Sied/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/src/css/bom_detail.css">
</head>
<body>
    <jsp:include page="../../Header_Sied/header.jsp" />
    
    <div class="main-container">
        <jsp:include page="../../Header_Sied/sidebar.jsp" />
        
        <div class="content-area">
            <div class="container">
                <!-- 페이지 헤더 -->
                <div class="page-header">
                    <div class="header-info">
                        <h1 class="page-title">${bom.stName} BOM</h1>
                        <div class="product-info">
                            <span>제품코드: ${bom.standardCode}</span>
                            <span class="product-type type-${bom.stType.toLowerCase()}">${bom.stType}</span>
                            <span>BOM번호: ${bom.bomNo}</span>
                            <span>BOM 순서: ${bom.bomOrder}</span>
                        </div>
                    </div>
                    <div class="action-buttons">
                        <a href="${pageContext.request.contextPath}/bom/form?code=${bom.bomNo}" class="btn btn-warning">수정</a>
                        <a href="${pageContext.request.contextPath}/bom/delete?code=${bom.bomNo}" class="btn btn-danger" onclick="return confirm('정말 삭제하시겠습니까?')">삭제</a>
                        <a href="${pageContext.request.contextPath}/bom" class="btn btn-secondary">목록으로</a>
                    </div>
                </div>

                <!-- 기본 정보 카드 -->
                <div class="cards-container">
                    <div class="card">
                        <h3 class="card-title">BOM 기본 정보</h3>
                        <div class="info-row">
                            <span class="info-label">BOM번호:</span>
                            <span class="info-value">${bom.bomNo}</span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">제품명:</span>
                            <span class="info-value">${bom.stName}</span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">BOM 설명:</span>
                            <span class="info-value">${bom.bomDescription}</span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">BOM 유형:</span>
                            <span class="info-value">${bom.bomType}</span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">BOM 순서:</span>
                            <span class="info-value">${bom.bomOrder}</span>
                        </div>
                    </div>

                    <div class="card">
                        <h3 class="card-title">등록 정보</h3>
                        <div class="info-row">
                            <span class="info-label">등록일:</span>
                            <span class="info-value"><fmt:formatDate value="${bom.createDate}" pattern="yyyy-MM-dd"/></span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">수정일:</span>
                            <span class="info-value"><fmt:formatDate value="${bom.updateDate}" pattern="yyyy-MM-dd"/></span>
                        </div>
                    </div>

                    <div class="card">
                        <h3 class="card-title">재고 현황 요약</h3>
                        <div class="stats-grid">
                            <div class="stat-card">
                                <h4>전체 자재</h4>
                                <p class="number">${materials.size()}</p>
                            </div>
                            <div class="stat-card">
                                <h4>충분한 재고</h4>
                                <p class="number" id="sufficientCount">0</p>
                            </div>
                            <div class="stat-card">
                                <h4>부족한 재고</h4>
                                <p class="number" id="insufficientCount">0</p>
                            </div>
                            <div class="stat-card">
                                <h4>재고 충족률</h4>
                                <p class="number" id="fulfillmentRate">0%</p>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- BOM 자재 표 -->
                <div class="bom-section">
                    <h2 class="section-title">BOM 자재 목록</h2>
                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>자재코드</th>
                                    <th>자재명</th>
                                    <th>자재 유형</th>
                                    <th>단위</th>
                                    <th>BOM 수량</th>
                                    <th>현재 재고</th>
                                    <th>재고 상태</th>
                                    <th>비고</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="material" items="${materials}">
                                <tr>
                                    <td>${material.materialCode}</td>
                                    <td>${material.materialName}</td>
                                    <td>${material.materialType}</td>
                                    <td>${material.unit}</td>
                                    <td>${material.bomQuantity}</td>
                                    <td>${material.currentStock}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${material.currentStock >= material.bomQuantity}">
                                                <span class="stock-status status-sufficient">충분</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="stock-status status-low">부족</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${material.remark}</td>
                                </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>

                <!-- 수량 계산기 -->
                <div class="calculator-section">
                    <div class="calculator-header">
                        <h2 class="section-title" style="margin: 0;">수량 계산기</h2>
                        <div class="quantity-input">
                            <label for="productionQuantity">생산 수량:</label>
                            <input type="number" id="productionQuantity" value="10" min="1" onchange="calculateMaterials()">
                            <span>개</span>
                            <button class="calc-btn" onclick="calculateMaterials()">계산</button>
                        </div>
                    </div>

                    <div class="calc-results">
                        <table class="calc-table">
                            <thead>
                                <tr>
                                    <th>자재명</th>
                                    <th>BOM 수량</th>
                                    <th>필요 수량</th>
                                    <th>현재 재고</th>
                                    <th>부족 수량</th>
                                    <th>상태</th>
                                </tr>
                            </thead>
                            <tbody id="calcResults">
                                <!-- JavaScript로 동적 생성 -->
                            </tbody>
                        </table>
                    </div>
                </div>

                <!-- 부족 재고 목록 -->
                <div class="insufficient-materials">
                    <h4>⚠️ 부족한 재고 목록</h4>
                    <ul class="insufficient-list">
                        <li>• 천연 보습제: 25ml 부족 (10개 생산시)</li>
                        <li>• 권장사항: 재고 보충 또는 발주 필요</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <script>
        // 서버에서 전달받은 자재 데이터 사용
        const bomMaterials = [
            <c:forEach var="material" items="${materials}" varStatus="status">
            {
                code: '${material.materialCode}',
                name: '${material.materialName}',
                type: '${material.materialType}',
                unit: '${material.unit}',
                bomQty: ${material.bomQuantity},
                currentStock: ${material.currentStock},
                remark: '${material.remark}'
            }<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        ];

        // 수량 계산 함수
        function calculateMaterials() {
            const productionQty = parseInt(document.getElementById('productionQuantity').value) || 0;
            const resultsTable = document.getElementById('calcResults');
            
            if (productionQty <= 0) {
                resultsTable.innerHTML = '<tr><td colspan="6" style="text-align: center; color: #666;">생산 수량을 입력하세요.</td></tr>';
                return;
            }

            let html = '';
            let insufficientCount = 0;
            let sufficientCount = 0;

            bomMaterials.forEach(material => {
                const requiredQty = material.bomQty * productionQty;
                const shortage = Math.max(0, requiredQty - material.currentStock);
                const isInsufficient = shortage > 0;
                
                if (isInsufficient) {
                    insufficientCount++;
                } else {
                    sufficientCount++;
                }

                html += `
                    <tr ${isInsufficient ? 'class="insufficient"' : ''}>
                        <td>${material.name}</td>
                        <td>${material.bomQty} ${material.unit}</td>
                        <td>${requiredQty} ${material.unit}</td>
                        <td>${material.currentStock} ${material.unit}</td>
                        <td>${shortage > 0 ? shortage : 0} ${material.unit}</td>
                        <td>
                            <span class="stock-status ${isInsufficient ? 'status-low' : 'status-sufficient'}">
                                ${isInsufficient ? '부족' : '충분'}
                            </span>
                        </td>
                    </tr>
                `;
            });

            resultsTable.innerHTML = html;

            // 통계 업데이트
            document.getElementById('sufficientCount').textContent = sufficientCount;
            document.getElementById('insufficientCount').textContent = insufficientCount;
            const fulfillmentRate = bomMaterials.length > 0 ? Math.round((sufficientCount / bomMaterials.length) * 100) : 0;
            document.getElementById('fulfillmentRate').textContent = fulfillmentRate + '%';

            // 부족 재고 목록 업데이트
            updateInsufficientList(productionQty, insufficientCount);
        }

        // 부족 재고 목록 업데이트
        function updateInsufficientList(productionQty, insufficientCount) {
            const insufficientSection = document.querySelector('.insufficient-materials');
            
            if (insufficientCount === 0) {
                insufficientSection.innerHTML = `
                    <h4>✅ 재고 충족</h4>
                    <ul class="insufficient-list">
                        <li>• 모든 자재가 충분합니다.</li>
                        <li>• ${productionQty}개 생산 가능합니다.</li>
                    </ul>
                `;
                insufficientSection.style.background = '#d4edda';
                insufficientSection.style.borderColor = '#c3e6cb';
            } else {
                let html = '<h4>⚠️ 부족한 재고 목록</h4><ul class="insufficient-list">';
                
                bomMaterials.forEach(material => {
                    const requiredQty = material.bomQty * productionQty;
                    const shortage = Math.max(0, requiredQty - material.currentStock);
                    
                    if (shortage > 0) {
                        html += `<li>• ${material.name}: ${shortage}${material.unit} 부족 (${productionQty}개 생산시)</li>`;
                    }
                });
                
                html += '<li>• 권장사항: 재고 보충 또는 발주 필요</li></ul>';
                insufficientSection.innerHTML = html;
                insufficientSection.style.background = '#fff3cd';
                insufficientSection.style.borderColor = '#ffeaa7';
            }
        }

        // BOM 삭제는 이제 서버사이드에서 처리됨 (링크 클릭으로)

        // 페이지 로드시 초기 계산
        document.addEventListener('DOMContentLoaded', function() {
            calculateMaterials();
        });
    </script>
</body>
</html>
