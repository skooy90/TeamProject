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
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/admin/admin_common.css">
</head>
<body>
    <jsp:include page="../../Header_Side/header.jsp" />
    <jsp:include page="../../Header_Side/sidebar.jsp" />
    
    <div class="admin-page-container">
        <div class="admin-content">
            <div class="page-header">
                <h1 class="page-title">사용자 관리</h1>
            </div>

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

            <!-- 검색 영역 -->
            <div class="search-section">
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
                    <a href="${ctx}/admin/users/form" class="btn btn-success">사용자 등록</a>
                </form>
            </div>

            <!-- 필터 영역 -->
            <div class="search-section">
                <form action="${ctx}/admin/users" method="get" class="search-form">
                    <input type="hidden" name="action" value="filter">
                    <select name="authority" class="search-select">
                        <option value="">전체 권한</option>
                        <option value="ADMIN" ${filterAuthority == 'ADMIN' ? 'selected' : ''}>관리자</option>
                        <option value="EMPLOYEE" ${filterAuthority == 'EMPLOYEE' ? 'selected' : ''}>일반사원</option>
                    </select>
                    <select name="position" class="search-select">
                        <option value="">전체 직급</option>
                        <option value="공장장" ${filterPosition == '공장장' ? 'selected' : ''}>공장장</option>
                        <option value="팀장" ${filterPosition == '팀장' ? 'selected' : ''}>팀장</option>
                        <option value="주임" ${filterPosition == '주임' ? 'selected' : ''}>주임</option>
                        <option value="사원" ${filterPosition == '사원' ? 'selected' : ''}>사원</option>
                    </select>
                    <button type="submit" class="btn btn-primary">필터 적용</button>
                </form>
            </div>

            <!-- 통계 정보 -->
            <c:if test="${not empty stats}">
                <div class="stats-section">
                    <div class="stat-item">
                        <span class="stat-label">총 사용자</span>
                        <span class="stat-value">${stats.totalUsers}</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-label">관리자</span>
                        <span class="stat-value">${stats.adminUsers}</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-label">일반사원</span>
                        <span class="stat-value">${stats.employeeUsers}</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-label">비밀번호 변경 완료</span>
                        <span class="stat-value">${stats.passwordChanged}</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-label">비밀번호 변경 필요</span>
                        <span class="stat-value">${stats.passwordPending}</span>
                    </div>
                </div>
            </c:if>

            <!-- 사용자 목록 테이블 -->
            <div class="table-container">
                <table class="admin-table">
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
                                            <c:choose>
                                                <c:when test="${user.usAuthority == 'ADMIN'}">
                                                    <span style="color: #e74c3c; font-weight: bold;">관리자</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span style="color: #27ae60; font-weight: bold;">사용자</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${user.usPsUpStatus == 1}">
                                                    <span style="color: #27ae60;">✓ 최신</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span style="color: #f39c12;">⚠ 변경 필요</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${user.createDate}" pattern="yyyy-MM-dd"/>
                                        </td>
                                        <td>
                                            <div class="action-buttons">
                                                <button onclick="editUser('${user.employeeNo}', '${user.usName}', '${user.usPosition}', '${user.usAuthority}')" 
                                                        class="btn btn-warning btn-sm">수정</button>
                                                <button onclick="resetPassword('${user.employeeNo}')" 
                                                        class="btn btn-danger btn-sm">비밀번호 초기화</button>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="7" style="text-align: center; padding: 40px;">
                                        등록된 사용자가 없습니다.
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- 사용자 수정 모달 -->
    <div id="editModal" class="modal" style="display: none;">
        <div class="modal-content">
            <div class="modal-header">
                <h3>사용자 정보 수정</h3>
                <span class="close" onclick="closeEditModal()">&times;</span>
            </div>
            <form id="editForm" action="${ctx}/admin/users/action" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="employeeNo" id="editEmployeeNo">
                
                <div class="form-group">
                    <label for="editUsName">이름</label>
                    <input type="text" id="editUsName" name="usName" required>
                </div>
                
                <div class="form-group">
                    <label for="editUsPosition">직급</label>
                    <select id="editUsPosition" name="usPosition" required>
                        <option value="공장장">공장장</option>
                        <option value="팀장">팀장</option>
                        <option value="주임">주임</option>
                        <option value="사원">사원</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="editUsAuthority">권한</label>
                    <select id="editUsAuthority" name="usAuthority" required>
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
        function editUser(employeeNo, usName, usPosition, usAuthority) {
            document.getElementById('editEmployeeNo').value = employeeNo;
            document.getElementById('editUsName').value = usName;
            document.getElementById('editUsPosition').value = usPosition;
            document.getElementById('editUsAuthority').value = usAuthority;
            document.getElementById('editModal').style.display = 'block';
        }

        function closeEditModal() {
            document.getElementById('editModal').style.display = 'none';
        }

        function resetPassword(employeeNo) {
            if (confirm('비밀번호를 초기화하시겠습니까?')) {
                const form = document.createElement('form');
                form.method = 'post';
                form.action = '${ctx}/admin/users/action';
                
                const actionInput = document.createElement('input');
                actionInput.type = 'hidden';
                actionInput.name = 'action';
                actionInput.value = 'resetPassword';
                
                const employeeNoInput = document.createElement('input');
                employeeNoInput.type = 'hidden';
                employeeNoInput.name = 'employeeNo';
                employeeNoInput.value = employeeNo;
                
                form.appendChild(actionInput);
                form.appendChild(employeeNoInput);
                document.body.appendChild(form);
                form.submit();
            }
        }

        // 모달 외부 클릭 시 닫기
        window.onclick = function(event) {
            const modal = document.getElementById('editModal');
            if (event.target == modal) {
                closeEditModal();
            }
        }
    </script>

    <style>
        /* 모달 스타일 */
        .modal {
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0,0,0,0.5);
        }

        .modal-content {
            background-color: #fefefe;
            margin: 15% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 80%;
            max-width: 500px;
            border-radius: 8px;
        }

        .modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 1px solid #dee2e6;
        }

        .close {
            color: #aaa;
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
        }

        .close:hover {
            color: #000;
        }

        .modal-footer {
            display: flex;
            justify-content: flex-end;
            gap: 10px;
            margin-top: 20px;
        }
    </style>
</body>
</html>