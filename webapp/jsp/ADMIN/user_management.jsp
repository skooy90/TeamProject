<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>사용자 관리 - MES 시스템</title>
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
                <h1 class="page-title">사용자 관리</h1>

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

                <!-- 검색 및 액션 영역 -->
                <div class="search-action-section">
                    <form action="${ctx}/admin/users" method="get" class="search-form">
                        <input type="hidden" name="action" value="search">
                        <select name="searchType" class="search-select">
                            <option value="all" ${searchType == 'all' ? 'selected' : ''}>전체 검색</option>
                            <option value="name" ${searchType == 'name' ? 'selected' : ''}>이름</option>
                            <option value="employeeNo" ${searchType == 'employeeNo' ? 'selected' : ''}>사원번호</option>
                        </select>
                        <input type="text" name="keyword" value="${keyword}" placeholder="검색어를 입력하세요" class="search-input">
                        <button type="submit" class="btn btn-primary">검색</button>
                        <a href="${ctx}/admin/users" class="btn btn-secondary">초기화</a>
                    </form>
                    <a href="${ctx}/admin/users/form" class="btn btn-success">사용자 등록</a>
                </div>

                <!-- 필터 영역 -->
                <div class="filter-section">
                    <form action="${ctx}/admin/users" method="get" class="filter-form">
                        <input type="hidden" name="action" value="filter">
                        <select name="authority" class="filter-select">
                            <option value="">전체 권한</option>
                            <option value="ADMIN" ${filterAuthority == 'ADMIN' ? 'selected' : ''}>관리자</option>
                            <option value="EMPLOYEE" ${filterAuthority == 'EMPLOYEE' ? 'selected' : ''}>일반사원</option>
                        </select>
                        <select name="position" class="filter-select">
                            <option value="">전체 직급</option>
                            <option value="공장장" ${filterPosition == '공장장' ? 'selected' : ''}>공장장</option>
                            <option value="팀장" ${filterPosition == '팀장' ? 'selected' : ''}>팀장</option>
                            <option value="주임" ${filterPosition == '주임' ? 'selected' : ''}>주임</option>
                            <option value="사원" ${filterPosition == '사원' ? 'selected' : ''}>사원</option>
                        </select>
                        <button type="submit" class="btn btn-primary">필터 적용</button>
                    </form>
                </div>

                <!-- 사용자 목록 테이블 -->
                <div class="table-container">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>사원번호</th>
                                <th>이름</th>
                                <th>직급</th>
                                <th>권한</th>
                                <th>비밀번호 상태</th>
                                <th>가입일</th>
                                <th>관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty userList}">
                                    <c:forEach var="user" items="${userList}">
                                        <tr>
                                            <td>${user.employeeNo}</td>
                                            <td>${user.usName}</td>
                                            <td>${user.usPosition}</td>
                                            <td>
                                                <span class="status-badge ${user.usAuthority == 'ADMIN' ? 'authority-admin' : 'authority-employee'}">
                                                    ${user.usAuthority == 'ADMIN' ? '관리자' : '일반사원'}
                                                </span>
                                            </td>
                                            <td>
                                                <span class="status-badge ${user.usPsUpStatus == 1 ? 'status-completed' : 'status-pending'}">
                                                    ${user.usPsUpStatus == 1 ? '완료' : '변경필요'}
                                                </span>
                                            </td>
                                            <td><fmt:formatDate value="${user.createDate}" pattern="yyyy-MM-dd"/></td>
                                            <td>
                                                <div class="action-buttons">
                                                    <button class="btn btn-warning btn-sm" onclick="openEditModal('${user.employeeNo}', '${user.usName}', '${user.usPosition}', '${user.usAuthority}')">수정</button>
                                                    <button class="btn btn-primary btn-sm" onclick="resetPassword('${user.employeeNo}')">비밀번호 초기화</button>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="7" class="empty-state">
                                            <div class="empty-content">
                                                <div class="empty-icon">👥</div>
                                                <div class="empty-message">등록된 사용자가 없습니다.</div>
                                            </div>
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>

                <!-- 통계 영역 -->
                <c:if test="${not empty statistics}">
                    <div class="stats-section">
                        <div class="stat-card">
                            <div class="stat-title">사용자 현황</div>
                            <div class="stat-content">
                                <div>
                                    <div class="stat-number">${statistics.totalUsers}</div>
                                    <div class="stat-label">전체 사용자</div>
                                </div>
                                <div>
                                    <div class="stat-number">${statistics.adminUsers}</div>
                                    <div class="stat-label">관리자</div>
                                </div>
                                <div>
                                    <div class="stat-number">${statistics.employeeUsers}</div>
                                    <div class="stat-label">일반사원</div>
                                </div>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-title">비밀번호 상태</div>
                            <div class="stat-content">
                                <div>
                                    <div class="stat-number">${statistics.passwordChanged}</div>
                                    <div class="stat-label">변경 완료</div>
                                </div>
                                <div>
                                    <div class="stat-number">${statistics.passwordPending}</div>
                                    <div class="stat-label">변경 필요</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <!-- 사용자 수정 모달 -->
    <div id="editModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h2 class="modal-title">사용자 정보 수정</h2>
                <span class="close" onclick="closeEditModal()">&times;</span>
            </div>
            <form action="${ctx}/admin/users/action" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="employeeNo" id="editEmployeeNo">
                <div class="form-group">
                    <label class="form-label">사원번호</label>
                    <input type="text" class="form-input" id="editEmployeeNoDisplay" readonly>
                </div>
                <div class="form-group">
                    <label class="form-label">이름 *</label>
                    <input type="text" class="form-input" name="usName" id="editName" required>
                </div>
                <div class="form-group">
                    <label class="form-label">직급 *</label>
                    <select class="form-select" name="usPosition" id="editPosition" required>
                        <option value="공장장">공장장</option>
                        <option value="팀장">팀장</option>
                        <option value="주임">주임</option>
                        <option value="사원">사원</option>
                    </select>
                </div>
                <div class="form-group">
                    <label class="form-label">권한 *</label>
                    <select class="form-select" name="usAuthority" id="editAuthority" required>
                        <option value="ADMIN">관리자</option>
                        <option value="EMPLOYEE">일반사원</option>
                    </select>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">수정</button>
                    <button type="button" class="btn btn-secondary" onclick="closeEditModal()">취소</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        // 사용자 수정 모달 열기
        function openEditModal(employeeNo, name, position, authority) {
            document.getElementById('editEmployeeNo').value = employeeNo;
            document.getElementById('editEmployeeNoDisplay').value = employeeNo;
            document.getElementById('editName').value = name;
            document.getElementById('editPosition').value = position;
            document.getElementById('editAuthority').value = authority;
            document.getElementById('editModal').style.display = 'block';
        }

        // 사용자 수정 모달 닫기
        function closeEditModal() {
            document.getElementById('editModal').style.display = 'none';
        }

        // 비밀번호 초기화 (AJAX)
        function resetPassword(employeeNo) {
            if (confirm('비밀번호를 초기화하시겠습니까?')) {
                // AJAX 요청으로 비밀번호 초기화
                fetch('${ctx}/admin/users/action', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: 'action=resetPassword&employeeNo=' + employeeNo
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        // 성공 알림창 표시
                        alert(data.message);
                        // 페이지 새로고침
                        location.reload();
                    } else {
                        // 실패 알림창 표시
                        alert(data.message);
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('비밀번호 초기화 중 오류가 발생했습니다.');
                });
            }
        }

        // 모달 외부 클릭 시 닫기
        window.onclick = function(event) {
            const editModal = document.getElementById('editModal');
            if (event.target == editModal) {
                closeEditModal();
            }
        }
    </script>
</body>
</html>
