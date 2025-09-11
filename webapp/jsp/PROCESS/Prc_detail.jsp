<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>공정 상세 - ${process.stName} - MES 시스템</title>
      <c:set var="ctx" value="${pageContext.request.contextPath}" />
    
  <link rel="stylesheet" href="${ctx}/css/process_detail.css">
  <link rel="stylesheet" href="${ctx}/src/Header_Side/style.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
                        <h1>${process.prDescription}</h1>
                        <span class="routing-type">${process.stType == 'SEMI' ? '반제품 라우팅' : '완제품 라우팅'}</span>
                    </div>
                    <div class="header-actions">
                        <a href="${pageContext.request.contextPath}/process/form?code=${process.standardCode}" class="btn btn-primary">라우팅 수정</a>
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
                            <span class="info-value">${process.standardCode}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">제품명</span>
                            <span class="info-value">${process.stName}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">라우팅 타입</span>
                            <span class="info-value">${process.stType == 'SEMI' ? '반제품' : '완제품'}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">총 공정 단계</span>
                            <span class="info-value">${processSteps.size()}단계</span>
                        </div>
                    </div>

                    <!-- 공정 순서도 -->
                    <div class="chart-card">
                        <h3>공정 순서도</h3>
                        <div class="chart-container">
                            <canvas id="processChart"></canvas>
                            </div>
                    </div>
                </div>

                <!-- 공정 단계별 상세 정보 -->
                <div class="process-steps">
                    <h3>공정 단계별 상세 정보</h3>
                    
                    <c:forEach var="step" items="${processSteps}" varStatus="status">
                        <div class="step-card ${step.prOrder <= 4 ? 'step-semi' : 'step-finish'}">
                            <div class="step-header">
                                <div class="step-number">${step.prOrder}</div>
                                <div class="step-info">
                                    <h4>${step.prDescription}</h4>
                                    <span class="step-type">${step.prType}</span>
                                </div>
                            </div>
                            <div class="step-content">
                                <div class="materials-section">
                                    <h5>필요 자재</h5>
                                    <div class="materials-list">
                                        <c:choose>                                            <c:when test="${step.prOrder == 1 && process.stType == 'SEMI'}">
                                                <div class="material-item">신선 오이 2kg (RA001)</div>
                                                <div class="material-item">가성소다 200g (RA002)</div>
                                                <div class="material-item">팜 오일 500ml (RA003)</div>
                                                <div class="material-item">올리브 오일 300ml (RA004)</div>
                                                <div class="material-item">믹서기, 교반기</div>
                                            </c:when>
                                            <c:when test="${step.prOrder == 2 && process.stType == 'SEMI'}">
                                                <div class="material-item">살리실산 50g (RA005)</div>
                                                <div class="material-item">과일산 40g (RA006)</div>
                                                <div class="material-item">아연 PCA 60g (RA007)</div>
                                                <div class="material-item">티트리 오일 30ml (RA008)</div>
                                                <div class="material-item">성형틀, 교반기</div>
                                            </c:when>
                                            <c:when test="${step.prOrder == 3 && process.stType == 'SEMI'}">
                                                <div class="material-item">건조실 (온도: 25°C)</div>
                                                <div class="material-item">온도계, 습도계</div>
                                                <div class="material-item">시간 측정기</div>
                                                <div class="material-item">경화 확인 도구</div>
                                            </c:when>
                                            <c:when test="${step.prOrder == 4 && process.stType == 'SEMI'}">
                                                <div class="material-item">비누 베이스 포장재</div>
                                                <div class="material-item">베이스 라벨</div>
                                                <div class="material-item">보관 박스</div>
                                                <div class="material-item">품질 검사 도구</div>
                                            </c:when>
                                            <c:when test="${step.prOrder == 1 && process.stType == 'FINISH'}">
                                                <div class="material-item">신선 오이 2kg (RA001)</div>
                                                <div class="material-item">가성소다 200g (RA002)</div>
                                                <div class="material-item">팜 오일 500ml (RA003)</div>
                                                <div class="material-item">올리브 오일 300ml (RA004)</div>
                                                <div class="material-item">믹서기, 교반기</div>
                                            </c:when>
                                            <c:when test="${step.prOrder == 2 && process.stType == 'FINISH'}">
                                                <div class="material-item">살리실산 50g (RA005)</div>
                                                <div class="material-item">과일산 40g (RA006)</div>
                                                <div class="material-item">아연 PCA 60g (RA007)</div>
                                                <div class="material-item">티트리 오일 30ml (RA008)</div>
                                                <div class="material-item">성형틀, 교반기</div>
                                            </c:when>
                                            <c:when test="${step.prOrder == 3 && process.stType == 'FINISH'}">
                                                <div class="material-item">건조실 (온도: 25°C)</div>
                                                <div class="material-item">온도계, 습도계</div>
                                                <div class="material-item">시간 측정기</div>
                                                <div class="material-item">경화 확인 도구</div>
                                            </c:when>
                                            <c:when test="${step.prOrder == 4 && process.stType == 'FINISH'}">
                                                <div class="material-item">비누 베이스 포장재</div>
                                                <div class="material-item">베이스 라벨</div>
                                                <div class="material-item">보관 박스</div>
                                                <div class="material-item">품질 검사 도구</div>
                                            </c:when>
                                            <c:when test="${step.prOrder == 5 && process.stType == 'FINISH'}">
                                                <div class="material-item">오이 비누 베이스 2kg (SE001)</div>
                                                <div class="material-item">오이 추출물 100ml (SE002)</div>
                                                <c:if test="${process.stName == '오이 각질제거비누'}">
                                                    <div class="material-item">각질제거 성분 150g (SE003)</div>
                                                </c:if>
                                                <c:if test="${process.stName == '오이 수축비누'}">
                                                    <div class="material-item">수축 성분 125g (SE004)</div>
                                                </c:if>
                                                <div class="material-item">믹서기, 교반기</div>
                                            </c:when>
                                            <c:when test="${step.prOrder == 6 && process.stType == 'FINISH'}">
                                                <div class="material-item">올리브 오일 200ml (RA004)</div>
                                                <div class="material-item">티트리 오일 20ml (RA008)</div>
                                                <c:if test="${process.stName == '오이 각질제거비누'}">
                                                    <div class="material-item">살리실산 25g (RA005)</div>
                                                </c:if>
                                                <c:if test="${process.stName == '오이 수축비누'}">
                                                    <div class="material-item">과일산 20g (RA006)</div>
                                                </c:if>
                                                <div class="material-item">교반기, 온도계</div>
                                            </c:when>
                                            <c:when test="${step.prOrder == 7 && process.stType == 'FINISH'}">
                                                <div class="material-item">완제품 성형틀</div>
                                                <div class="material-item">건조실 (온도: 30°C)</div>
                                                <div class="material-item">온도계, 습도계</div>
                                                <div class="material-item">경화 확인 도구</div>
                                            </c:when>
                                            <c:when test="${step.prOrder == 8 && process.stType == 'FINISH'}">
                                                <div class="material-item">완제품 포장재</div>
                                                <div class="material-item">제품 라벨</div>
                                                <div class="material-item">품질 검사 도구</div>
                                                <div class="material-item">최종 포장 박스</div>
                                            </c:when>
                                        </c:choose>
                            </div>
                                </div>
                                <div class="work-instruction">
                                    <h5>작업 지침</h5>
                                    <div class="instruction-text">
                                        <c:choose>                                            <c:when test="${step.prOrder == 1 && process.stType == 'SEMI'}">
                                                <p>1. 신선 오이 2kg (RA001) 깨끗이 씻기</p>
                                                <p>2. 슬라이스하여 믹서기에 넣기</p>
                                                <p>3. 가성소다 200g (RA002)과 함께 3분간 갈기</p>
                                                <p>4. 팜 오일 500ml (RA003) + 올리브 오일 300ml (RA004) 추가</p>
                                                <p>5. 40°C에서 5분간 교반</p>
                                            </c:when>
                                            <c:when test="${step.prOrder == 2 && process.stType == 'SEMI'}">
                                                <p>1. 살리실산 50g (RA005) + 과일산 40g (RA006) 혼합</p>
                                                <p>2. 아연 PCA 60g (RA007) + 티트리 오일 30ml (RA008) 추가</p>
                                                <p>3. 교반기로 3분간 균일하게 혼합</p>
                                                <p>4. 성형틀에 부어넣기</p>
                                            </c:when>
                                            <c:when test="${step.prOrder == 3 && process.stType == 'SEMI'}">
                                                <p>1. 성형틀을 건조실에 배치 (온도: 25°C)</p>
                                                <p>2. 24시간 경화 대기</p>
                                                <p>3. 4주간 건조 완료 (습도 60% 유지)</p>
                                                <p>4. 경화 상태 확인 후 다음 단계 진행</p>
                                            </c:when>
                                            <c:when test="${step.prOrder == 4 && process.stType == 'SEMI'}">
                                                <p>1. 개별 포장재로 비누 베이스 포장</p>
                                                <p>2. 베이스 라벨 부착 (제조일자, 유통기한 표시)</p>
                                                <p>3. 품질 검사 후 박스에 정리하여 보관</p>
                                                <p>4. 보관 조건: 서늘하고 건조한 곳</p>
                                            </c:when>
                                            <c:when test="${step.prOrder == 1 && process.stType == 'FINISH'}">
                                                <p>1. 신선 오이 2kg (RA001) 깨끗이 씻기</p>
                                                <p>2. 슬라이스하여 믹서기에 넣기</p>
                                                <p>3. 가성소다 200g (RA002)과 함께 3분간 갈기</p>
                                                <p>4. 팜 오일 500ml (RA003) + 올리브 오일 300ml (RA004) 추가</p>
                                                <p>5. 40°C에서 5분간 교반</p>
                                            </c:when>
                                            <c:when test="${step.prOrder == 2 && process.stType == 'FINISH'}">
                                                <p>1. 살리실산 50g (RA005) + 과일산 40g (RA006) 혼합</p>
                                                <p>2. 아연 PCA 60g (RA007) + 티트리 오일 30ml (RA008) 추가</p>
                                                <p>3. 교반기로 3분간 균일하게 혼합</p>
                                                <p>4. 성형틀에 부어넣기</p>
                                            </c:when>
                                            <c:when test="${step.prOrder == 3 && process.stType == 'FINISH'}">
                                                <p>1. 성형틀을 건조실에 배치 (온도: 25°C)</p>
                                                <p>2. 24시간 경화 대기</p>
                                                <p>3. 4주간 건조 완료 (습도 60% 유지)</p>
                                                <p>4. 경화 상태 확인 후 다음 단계 진행</p>
                                            </c:when>
                                            <c:when test="${step.prOrder == 4 && process.stType == 'FINISH'}">
                                                <p>1. 개별 포장재로 비누 베이스 포장</p>
                                                <p>2. 베이스 라벨 부착 (제조일자, 유통기한 표시)</p>
                                                <p>3. 품질 검사 후 박스에 정리하여 보관</p>
                                                <p>4. 보관 조건: 서늘하고 건조한 곳</p>
                                            </c:when>
                                            <c:when test="${step.prOrder == 5 && process.stType == 'FINISH'}">
                                                <p>1. 오이 비누 베이스 2kg (SE001) 35°C로 가열</p>
                                                <p>2. 오이 추출물 100ml (SE002) 추가</p>
                                                <c:if test="${process.stName == '오이 각질제거비누'}">
                                                    <p>3. 각질제거 성분 150g (SE003) 혼합</p>
                                                </c:if>
                                                <c:if test="${process.stName == '오이 수축비누'}">
                                                    <p>3. 수축 성분 125g (SE004) 혼합</p>
                                                </c:if>
                                                <p>4. 35°C에서 3분간 교반</p>
                                            </c:when>
                                            <c:when test="${step.prOrder == 6 && process.stType == 'FINISH'}">
                                                <p>1. 올리브 오일 200ml (RA004) 추가</p>
                                                <p>2. 티트리 오일 20ml (RA008) 혼합</p>
                                                <c:if test="${process.stName == '오이 각질제거비누'}">
                                                    <p>3. 살리실산 25g (RA005) 추가로 각질제거 효과 강화</p>
                                                </c:if>
                                                <c:if test="${process.stName == '오이 수축비누'}">
                                                    <p>3. 과일산 20g (RA006) 추가로 수축 효과 강화</p>
                                                </c:if>
                                                <p>4. 30°C에서 2분간 최종 교반</p>
                                            </c:when>
                                            <c:when test="${step.prOrder == 7 && process.stType == 'FINISH'}">
                                                <p>1. 완제품 성형틀에 부어넣기</p>
                                                <p>2. 건조실에 배치 (온도: 30°C, 습도: 50%)</p>
                                                <p>3. 12시간 경화 대기</p>
                                                <p>4. 2주간 건조 완료 후 경화 상태 확인</p>
                                            </c:when>
                                            <c:when test="${step.prOrder == 8 && process.stType == 'FINISH'}">
                                                <p>1. 완제품 포장재로 개별 포장</p>
                                                <p>2. 제품 라벨 부착 (제품명, 성분, 유통기한)</p>
                                                <p>3. 품질 검사 (외관, 무게, 포장 상태)</p>
                                                <p>4. 최종 포장 박스에 정리하여 출고</p>
                                            </c:when>
                                        </c:choose>
                                </div>
                            </div>
                        </div>
                </div>
                    </c:forEach>
                    </div>
                </div>
            </div>
        </div>

    <script>
        // 공정 순서도 차트
        const ctx = document.getElementById('processChart').getContext('2d');
        const processSteps = [
            <c:forEach var="step" items="${processSteps}" varStatus="status">
                '${step.prOrder}. ${step.prDescription}'<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        ];
        
        const chart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: processSteps,
                datasets: [{
                    label: '공정 순서',
                    data: [
                        <c:forEach var="step" items="${processSteps}" varStatus="status">
                            ${step.prOrder}<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    ],
                    borderColor: '#007bff',
                    backgroundColor: [
                        <c:forEach var="step" items="${processSteps}" varStatus="status">
                            '${step.prOrder <= 4 ? '#ffc107' : '#28a745'}'<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    ],
                    borderWidth: 3,
                    pointRadius: 8,
                    pointHoverRadius: 10
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true,
                        max: ${processSteps.size()},
                        ticks: {
                            stepSize: 1
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: false
                    },
                    title: {
                        display: true,
                        text: '공정 순서도 (노란색: 반제품 과정, 초록색: 완제품 과정)'
                    }
                }
            }
        });
    </script>
</body>
</html>
