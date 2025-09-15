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
    <link rel="stylesheet" href="<%=ctx%>/css/admin/admin_common.css">
    <c:set var="ctx" value="${pageContext.request.contextPath}" />
    
</head>
<body>
    <jsp:include page="/Header_Side/header.jsp" />
    <jsp:include page="/Header_Side/sidebar.jsp" />

    <div class="admin-page-container">
        <div class="admin-content">
            <div class="page-header">
                <h1 class="page-title">게시판 관리</h1>
            </div>

            <!-- 탭 컨테이너 -->
            <div class="tab-container">
                <div class="tab-buttons">
                    <button class="tab-button active" onclick="showTab('all-posts')">전체 게시글</button>
                    <button class="tab-button" onclick="showTab('reported-posts')">신고 접수 현황</button>
                </div>

                <!-- 전체 게시글 탭 -->
                <div id="all-posts" class="tab-content active">
                    <!-- 검색 및 액션 영역 -->
                    <div class="search-section">
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
                        <a href="${ctx}/admin/boards/form" class="btn btn-success">게시글 등록</a>
                        </form>
                    </div>

                    <!-- 필터 영역 -->
                    <div class="search-section">
                        <form method="GET" action="${ctx}/admin/boards" class="search-form">
                            <select name="category" class="search-select" onchange="this.form.submit()">
                                <option value="">전체 카테고리</option>
                                <option value="공지사항" ${category == '공지사항' ? 'selected' : ''}>공지사항</option>
                                <option value="일반게시판" ${category == '일반게시판' ? 'selected' : ''}>일반게시판</option>
                                <option value="Q&A" ${category == 'Q&A' ? 'selected' : ''}>Q&A</option>
                            </select>
                            <select name="writer" class="search-select" onchange="this.form.submit()">
                                <option value="">전체 작성자</option>
                                <c:forEach var="board" items="${boardList}">
                                    <option value="${board.boWriter}" ${writer == board.boWriter ? 'selected' : ''}>${board.boWriter}</option>
                                </c:forEach>
                            </select>
                            <a href="${ctx}/admin/boards" class="btn btn-secondary">초기화</a>
                        </form>
                    </div>

                    <!-- 통계 정보 -->
                    <c:if test="${not empty stats}">
                        <div class="stats-section">
                            <div class="stat-item">
                                <span class="stat-label">총 게시글</span>
                                <span class="stat-value">${stats.totalBoards}</span>
                            </div>
                            <div class="stat-item">
                                <span class="stat-label">오늘 작성</span>
                                <span class="stat-value">${stats.todayBoards}</span>
                            </div>
                            <c:forEach var="entry" items="${stats.categoryCounts}">
                                <div class="stat-item">
                                    <span class="stat-label">${entry.key}</span>
                                    <span class="stat-value">${entry.value}</span>
                                </div>
                            </c:forEach>
                        </div>
                    </c:if>

                    <!-- 게시글 목록 테이블 -->
                    <div class="table-container">
                        <table class="admin-table">
                            <thead>
                                <tr>
                                    <th>게시글번호</th>
                                    <th>제목</th>
                                    <th>작성자</th>
                                    <th>카테고리</th>
                                    <th>상태</th>
                                    <th>신고수</th>
                                    <th>작성일</th>
                                    <th>관리</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty boardList}">
                                        <c:forEach var="board" items="${boardList}">
                                            <tr>
                                                <td>${board.postNo}</td>
                                                <td>
                                                    <a href="${ctx}/admin/boards/detail${board.postNo}" class="title-link">
                                                        ${board.boTitle}
                                                    </a>
                                                </td>
                                                <td>${board.boWriter}</td>
                                                <td>
                                                    <span class="category-badge category-${board.boCategory == '공지사항' ? 'notice' : board.boCategory == '일반게시판' ? 'general' : 'qna'}">
                                                        ${board.boCategory}
                                                    </span>
                                                </td>
                                                <td><span class="status-badge status-normal">정상</span></td>
                                                <td>0</td>
                                                <td>
                                                    <fmt:formatDate value="${board.boCreationDate}" pattern="yyyy-MM-dd"/>
                                                </td>
                                                <td>
                                                    <div class="action-buttons">
                                                        <button class="btn btn-danger btn-sm" onclick="deletePost('${board.postNo}')">삭제</button>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="8" style="text-align: center; padding: 40px;">
                                                등록된 게시글이 없습니다.
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>

                <!-- 신고 접수 현황 탭 -->
                <div id="reported-posts" class="tab-content">
                    <div class="table-container">
                        <table class="admin-table">
                            <thead>
                                <tr>
                                    <th>게시글번호</th>
                                    <th>제목</th>
                                    <th>작성자</th>
                                    <th>신고자</th>
                                    <th>신고사유</th>
                                    <th>신고일</th>
                                    <th>처리상태</th>
                                    <th>관리</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td colspan="8" style="text-align: center; padding: 40px;">
                                        신고된 게시글이 없습니다.
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        // 페이지 로드 시 메시지 표시
//         window.onload = function() {
//             <c:if test="${not empty success}">
//                 alert('${success}');
//             </c:if>
//             <c:if test="${not empty error}">
//                 alert('${error}');
//             </c:if>
//         };

        function showTab(tabName) {
            // 모든 탭 콘텐츠 숨기기
            const tabContents = document.querySelectorAll('.tab-content');
            tabContents.forEach(content => {
                content.classList.remove('active');
            });
            
            // 모든 탭 버튼 비활성화
            const tabButtons = document.querySelectorAll('.tab-button');
            tabButtons.forEach(button => {
                button.classList.remove('active');
            });
            
            // 선택된 탭 콘텐츠 보이기
            document.getElementById(tabName).classList.add('active');
            
            // 선택된 탭 버튼 활성화
            event.target.classList.add('active');
        }


        function deletePost(postNo) {
            if (confirm('게시글을 삭제하시겠습니까?')) {
                // 폼 생성하여 삭제 요청
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = '${ctx}/joinproject/boardAction';
                
                // action 파라미터 추가
                const actionInput = document.createElement('input');
                actionInput.type = 'hidden';
                actionInput.name = 'action';
                actionInput.value = 'delete';
                form.appendChild(actionInput);
                
                // postNo 파라미터 추가
                const postNoInput = document.createElement('input');
                postNoInput.type = 'hidden';
                postNoInput.name = 'postNo';
                postNoInput.value = postNo;
                form.appendChild(postNoInput);
                
                // 폼을 body에 추가하고 제출
                document.body.appendChild(form);
                form.submit();
            }
        }
    </script>
    <!-- 메시지 표시 후 세션에서 제거 -->

    <style>
        /* 탭 스타일 */
        .tab-container {
            margin-bottom: 20px;
        }

        .tab-buttons {
            display: flex;
            border-bottom: 2px solid #dee2e6;
            margin-bottom: 20px;
        }

        .tab-button {
            padding: 10px 20px;
            background: none;
            border: none;
            cursor: pointer;
            font-size: 14px;
            font-weight: 500;
            color: #6c757d;
            border-bottom: 2px solid transparent;
            transition: all 0.3s ease;
        }

        .tab-button:hover {
            color: #3498db;
        }

        .tab-button.active {
            color: #3498db;
            border-bottom-color: #3498db;
        }

        .tab-content {
            display: none;
        }

        .tab-content.active {
            display: block;
        }

        /* 카테고리 배지 스타일 */
        .category-badge {
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: bold;
            color: white;
        }

        .category-notice {
            background-color: #e74c3c;
        }

        .category-general {
            background-color: #3498db;
        }

        .category-qna {
            background-color: #f39c12;
        }

        .status-badge {
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: bold;
            color: white;
        }

        .status-normal {
            background-color: #27ae60;
        }

        .title-link {
            color: #2c3e50;
            text-decoration: none;
        }

        .title-link:hover {
            color: #3498db;
            text-decoration: underline;
        }
    </style>
</body>
</html>