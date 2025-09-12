<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시판 관리 - MES 시스템</title>
    <link rel="stylesheet" href="<%=ctx%>/Header_Side/style.css">
    <link rel="stylesheet" href="<%=ctx%>/css/admin/board_management.css">
    <style>
        /* 인라인 스타일로 기본 레이아웃 강제 적용 */
        .main-content {
            margin-left: 200px !important;
            width: calc(100% - 200px) !important;
            min-height: 100vh !important;
            background-color: #f5f5f5 !important;
        }
        .content {
            padding: 30px !important;
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
    <jsp:include page="/Header_Side/header.jsp" />
    <jsp:include page="/Header_Side/sidebar.jsp" />

    <div class="main-content">
        <div class="content">
            <h1 class="page-title">게시판 관리</h1>

            <!-- 탭 컨테이너 -->
            <div class="tab-container">
                <div class="tab-buttons">
                    <button class="tab-button active" onclick="showTab('all-posts')">전체 게시글</button>
                    <button class="tab-button" onclick="showTab('reported-posts')">신고 접수 현황</button>
                </div>

                <!-- 전체 게시글 탭 -->
                <div id="all-posts" class="tab-content active">
                    <!-- 검색 및 액션 영역 -->
                    <div class="search-action-section">
                        <form method="GET" action="${ctx}/admin/boards" class="search-form">
                            <input type="text" name="keyword" class="search-input" 
                                   placeholder="제목, 내용, 작성자 검색" value="${keyword}">
                            <select name="searchType" class="search-select">
                                <option value="all" ${searchType == 'all' ? 'selected' : ''}>전체</option>
                                <option value="title" ${searchType == 'title' ? 'selected' : ''}>제목</option>
                                <option value="content" ${searchType == 'content' ? 'selected' : ''}>내용</option>
                                <option value="writer" ${searchType == 'writer' ? 'selected' : ''}>작성자</option>
                            </select>
                            <button type="submit" class="btn btn-primary">검색</button>
                        </form>
                        <a href="${ctx}/admin/boards/form" class="btn btn-success">게시글 등록</a>
                    </div>

                    <!-- 필터 영역 -->
                    <div class="filter-section">
                        <form method="GET" action="${ctx}/admin/boards" class="filter-form">
                            <select name="category" class="filter-select" onchange="this.form.submit()">
                                <option value="">전체 카테고리</option>
                                <option value="공지사항" ${category == '공지사항' ? 'selected' : ''}>공지사항</option>
                                <option value="일반게시판" ${category == '일반게시판' ? 'selected' : ''}>일반게시판</option>
                                <option value="Q&A" ${category == 'Q&A' ? 'selected' : ''}>Q&A</option>
                            </select>
                            <select name="writer" class="filter-select" onchange="this.form.submit()">
                                <option value="">전체 작성자</option>
                                <c:forEach var="board" items="${boardList}">
                                    <option value="${board.boWriter}" ${writer == board.boWriter ? 'selected' : ''}>${board.boWriter}</option>
                                </c:forEach>
                            </select>
                            <a href="${ctx}/admin/boards" class="btn btn-secondary">초기화</a>
                        </form>
                    </div>

                    <!-- 게시글 목록 테이블 -->
                    <div class="table-container">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>게시글번호</th>
                                    <th>제목</th>
                                    <th>작성자</th>
                                    <th>카테고리</th>
                                    <th>작성일</th>
                                    <th>관리</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty boardList}">
                                        <tr>
                                            <td colspan="6" class="text-center">등록된 게시글이 없습니다.</td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="board" items="${boardList}">
                                            <tr>
                                                <td>${board.postNo}</td>
                                                <td class="title-cell">
                                                    <span class="title-text">${board.boTitle}</span>
                                                </td>
                                                <td>${board.boWriter}</td>
                                                <td>
                                                    <span class="status-badge category-${board.boCategory == '공지사항' ? 'notice' : board.boCategory == '일반게시판' ? 'general' : 'qna'}">
                                                        ${board.boCategory}
                                                    </span>
                                                </td>
                                                <td>
                                                    <fmt:formatDate value="${board.boCreationDate}" pattern="yyyy-MM-dd"/>
                                                </td>
                                                <td>
                                                    <div class="action-buttons">
                                                        <button class="btn btn-warning btn-sm" onclick="openViewModal('${board.postNo}')">상세보기</button>
                                                        <button class="btn btn-danger btn-sm" onclick="deleteBoard('${board.postNo}')">삭제</button>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>

                <!-- 신고 접수 현황 탭 -->
                <div id="reported-posts" class="tab-content">
                    <!-- 신고 현황 통계 -->
                    <div class="stats-section">
                        <div class="stat-card">
                            <div class="stat-title">게시판 현황</div>
                            <div class="stat-content">
                                <div>
                                    <div class="stat-number">${stats.totalBoards}</div>
                                    <div class="stat-label">총 게시글 수</div>
                                </div>
                                <div>
                                    <div class="stat-number">${stats.todayBoards}</div>
                                    <div class="stat-label">오늘 작성</div>
                                </div>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-title">카테고리별 현황</div>
                            <div class="stat-content">
                                <c:forEach var="entry" items="${stats.categoryCounts}">
                                    <div>
                                        <div class="stat-number">${entry.value}</div>
                                        <div class="stat-label">${entry.key}</div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-title">최근 활동</div>
                            <div class="stat-content">
                                <div>
                                    <div class="stat-number">${stats.todayBoards}</div>
                                    <div class="stat-label">오늘</div>
                                </div>
                                <div>
                                    <div class="stat-number">${stats.totalBoards}</div>
                                    <div class="stat-label">전체</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 게시글 상세보기 모달 -->
    <div id="viewModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h2 class="modal-title">게시글 상세보기</h2>
                <span class="close" onclick="closeViewModal()">&times;</span>
            </div>
            <div class="post-meta">
                <span>게시글번호: <strong id="viewPostNo">-</strong></span>
                <span>작성자: <strong id="viewWriter">-</strong></span>
                <span>카테고리: <strong id="viewCategory">-</strong></span>
                <span>작성일: <strong id="viewDate">-</strong></span>
            </div>
            <div class="post-content">
                <h3 id="viewTitle">-</h3>
                <div id="viewContent" style="margin-top: 15px; line-height: 1.6;">
                    -
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-danger" onclick="deleteBoardFromModal()">게시글 삭제</button>
                <button class="btn" onclick="closeViewModal()" style="background-color: #6c757d; color: white;">닫기</button>
            </div>
        </div>
    </div>

    <script>
        // 탭 전환 함수
        function showTab(tabId) {
            // 모든 탭 버튼과 컨텐츠 비활성화
            document.querySelectorAll('.tab-button').forEach(btn => btn.classList.remove('active'));
            document.querySelectorAll('.tab-content').forEach(content => content.classList.remove('active'));
            
            // 선택된 탭 활성화
            event.target.classList.add('active');
            document.getElementById(tabId).classList.add('active');
        }

        // 모달 관련 함수들
        function openViewModal(postNo) {
            // AJAX로 게시글 상세 정보 가져오기
            fetch('${ctx}/admin/boards/detail?postNo=' + postNo)
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        document.getElementById('viewPostNo').textContent = data.board.postNo;
                        document.getElementById('viewWriter').textContent = data.board.boWriter;
                        document.getElementById('viewCategory').textContent = data.board.boCategory;
                        document.getElementById('viewDate').textContent = data.board.boCreationDate;
                        document.getElementById('viewTitle').textContent = data.board.boTitle;
                        document.getElementById('viewContent').textContent = data.board.boContent;
                        document.getElementById('viewModal').style.display = 'block';
                    } else {
                        alert('게시글 정보를 불러올 수 없습니다.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('게시글 정보를 불러오는 중 오류가 발생했습니다.');
                });
        }

        function closeViewModal() {
            document.getElementById('viewModal').style.display = 'none';
        }

        // 게시글 삭제 (AJAX)
        function deleteBoard(postNo) {
            if (confirm('게시글을 삭제하시겠습니까?')) {
                fetch('${ctx}/admin/boards/action', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: 'action=delete&postNo=' + postNo
                })
                .then(response => response.text())
                .then(data => {
                    alert('게시글이 삭제되었습니다.');
                    location.reload();
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('게시글 삭제 중 오류가 발생했습니다.');
                });
            }
        }

        function deleteBoardFromModal() {
            const postNo = document.getElementById('viewPostNo').textContent;
            if (confirm('게시글을 삭제하시겠습니까?')) {
                fetch('${ctx}/admin/boards/action', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: 'action=delete&postNo=' + postNo
                })
                .then(response => response.text())
                .then(data => {
                    alert('게시글이 삭제되었습니다.');
                    closeViewModal();
                    location.reload();
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('게시글 삭제 중 오류가 발생했습니다.');
                });
            }
        }

        // 모달 외부 클릭 시 닫기
        window.onclick = function(event) {
            const viewModal = document.getElementById('viewModal');
            
            if (event.target == viewModal) {
                closeViewModal();
            }
        }

        // 메시지 표시
        <c:if test="${not empty success}">
            alert('${success}');
        </c:if>
        <c:if test="${not empty error}">
            alert('${error}');
        </c:if>
    </script>
</body>
</html>
