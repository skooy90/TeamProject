<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>사용자 등록 - MES 시스템</title>
    <c:set var="ctx" value="${pageContext.request.contextPath}" />
    
    <link rel="stylesheet" href="<%=request.getContextPath()%>/Header_Side/style.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/admin/user_management.css">
    <style>
        /* 인라인 스타일로 기본 레이아웃 강제 적용 */
        .main-container {
            display: flex !important;
            margin-top: 60px !important;
            height: calc(100vh - 60px) !important;
        }
        .content-area {
            flex-grow: 1 !important;
            padding: 20px !important;
            margin-left: 200px !important;
            overflow-y: auto !important;
            height: calc(100vh - 60px) !important;
            background-color: #f5f5f5 !important;
        }
        .container {
            max-width: 1200px !important;
            margin: 0 auto !important;
            padding: 20px !important;
        }
        .page-title {
            font-size: 28px !important;
            color: #2c3e50 !important;
            margin-bottom: 30px !important;
            font-weight: 600 !important;
            border-bottom: 3px solid #3498db !important;
            padding-bottom: 10px !important;
        }
    </style>
</head>
<body>
    <jsp:include page="../../Header_Side/header.jsp" />
    <div class="main-container">
        <jsp:include page="../../Header_Side/sidebar.jsp" />
        <div class="content-area">
            <div class="container">
                <h1 class="page-title">사용자 등록</h1>

                <!-- 성공/에러 메시지 -->
                <c:if test="${not empty success}">
                    <div class="alert alert-success">
                        ${success}
                    </div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-error">
                        ${error}
                    </div>
                </c:if>

                <!-- 사용자 등록 폼 -->
                <div class="form-container">
                    <form action="${ctx}/admin/users/action" method="post" class="user-form">
                        <input type="hidden" name="action" value="create">
                        
                        <div class="form-section">
                            <h3 class="form-section-title">기본 정보</h3>
                            
                            <div class="form-group">
                                <label class="form-label" for="employeeNo">사원번호 *</label>
                                <input type="text" class="form-input" id="employeeNo" name="employeeNo" 
                                       value="${nextEmployeeNo}" readonly>
                                <small class="form-help">자동 생성됩니다</small>
                            </div>
                            
                            <div class="form-group">
                                <label class="form-label" for="usName">이름 *</label>
                                <input type="text" class="form-input" id="usName" name="usName" 
                                       placeholder="이름을 입력하세요" required>
                            </div>
                            
                            <div class="form-group">
                                <label class="form-label" for="usPosition">직급 *</label>
                                <select class="form-select" id="usPosition" name="usPosition" required>
                                    <option value="">직급을 선택하세요</option>
                                    <option value="공장장">공장장</option>
                                    <option value="팀장">팀장</option>
                                    <option value="주임">주임</option>
                                    <option value="사원">사원</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-section">
                            <h3 class="form-section-title">권한 및 보안</h3>
                            
                            <div class="form-group">
                                <label class="form-label" for="usAuthority">권한 *</label>
                                <select class="form-select" id="usAuthority" name="usAuthority" required>
                                    <option value="">권한을 선택하세요</option>
                                    <option value="ADMIN">관리자</option>
                                    <option value="EMPLOYEE">일반사원</option>
                                </select>
                            </div>
                            
                            <div class="form-group">
                                <label class="form-label" for="usPassword">초기 비밀번호</label>
                                <input type="password" class="form-input" id="usPassword" name="usPassword" 
                                       placeholder="기본: 사원번호" value="">
                                <small class="form-help">기본 비밀번호는 사원번호입니다. 사용자가 첫 로그인 시 변경해야 합니다.</small>
                            </div>
                        </div>

                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">사용자 등록</button>
                            <a href="${ctx}/admin/users" class="btn btn-secondary">취소</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script>
        // 폼 유효성 검사
        document.querySelector('.user-form').addEventListener('submit', function(e) {
            const name = document.getElementById('usName').value.trim();
            const position = document.getElementById('usPosition').value;
            const authority = document.getElementById('usAuthority').value;
            
            if (!name) {
                alert('이름을 입력해주세요.');
                e.preventDefault();
                return;
            }
            
            if (!position) {
                alert('직급을 선택해주세요.');
                e.preventDefault();
                return;
            }
            
            if (!authority) {
                alert('권한을 선택해주세요.');
                e.preventDefault();
                return;
            }
            
            if (confirm('사용자를 등록하시겠습니까?')) {
                return true;
            } else {
                e.preventDefault();
            }
        });

        // 권한에 따른 직급 제한
        document.getElementById('usAuthority').addEventListener('change', function() {
            const authority = this.value;
            const positionSelect = document.getElementById('usPosition');
            
            if (authority === 'ADMIN') {
                // 관리자는 공장장, 팀장만 가능
                Array.from(positionSelect.options).forEach(option => {
                    if (option.value === '주임' || option.value === '사원') {
                        option.style.display = 'none';
                    } else {
                        option.style.display = 'block';
                    }
                });
            } else {
                // 일반사원은 모든 직급 가능
                Array.from(positionSelect.options).forEach(option => {
                    option.style.display = 'block';
                });
            }
        });
    </script>
</body>
</html>
