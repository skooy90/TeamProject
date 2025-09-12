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
    <title>게시글 상세보기 - MES 시스템</title>
    <link rel="stylesheet" href="<%=ctx%>/Header_Side/style.css">
    <link rel="stylesheet" href="<%=ctx%>/css/board.css">
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
            background-color: #f4f7f9 !important;
        }
    </style>
</head>
<body>
    <jsp:include page="../../Header_Side/header.jsp" />
    <div class="main-container">
        <jsp:include page="../../Header_Side/sidebar.jsp" />
        <div class="content-area">
            <div class="board-container">
                <c:choose>
                    <c:when test="${empty board}">
                        <div class="error-message">
                            <h2>게시글을 찾을 수 없습니다.</h2>
                            <p>요청하신 게시글이 존재하지 않거나 삭제되었습니다.</p>
                            <a href="<%=ctx%>/boardList" class="btn btn-primary">목록으로 돌아가기</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <!-- 게시글 헤더 -->
                        <div class="board-detail-header">
                            <div class="board-detail-title">
                                <h1>${board.boTitle}</h1>
                                <div class="board-detail-meta">
                                    <span class="category-badge category-${board.boCategory == '공지사항' ? 'notice' : board.boCategory == '일반게시판' ? 'general' : 'qna'}">
                                        ${board.boCategory}
                                    </span>
                                    <span class="post-no">${board.postNo}</span>
                                </div>
                            </div>
                            <div class="board-detail-info">
                                <div class="writer-info">
                                    <span class="label">작성자:</span>
                                    <span class="value">${board.boWriter}</span>
                                </div>
                                <div class="date-info">
                                    <span class="label">작성일:</span>
                                    <span class="value">
                                        <fmt:formatDate value="${board.boCreationDate}" pattern="yyyy-MM-dd HH:mm"/>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <!-- 게시글 내용 -->
                        <div class="board-detail-content">
                            <div class="content-text">
                                ${board.boContent}
                            </div>
                        </div>

                        <!-- 게시글 액션 -->
                        <div class="board-detail-actions">
                            <div class="action-left">
                                <a href="<%=ctx%>/boardList" class="btn btn-secondary">목록</a>
                            </div>
                            <div class="action-right">
                                <button onclick="editBoard()" class="btn btn-warning">수정</button>
                                <button onclick="deleteBoard()" class="btn btn-danger">삭제</button>
                            </div>
                        </div>

                        <!-- 수정 폼 (숨김) -->
                        <div id="editForm" class="edit-form" style="display: none;">
                            <form method="POST" action="<%=ctx%>/boardAction">
                                <input type="hidden" name="action" value="update">
                                <input type="hidden" name="postNo" value="${board.postNo}">
                                <input type="hidden" name="boWriter" value="${board.boWriter}">
                                
                                <div class="form-group">
                                    <label class="form-label">카테고리</label>
                                    <select name="boCategory" class="form-select" required>
                                        <option value="공지사항" ${board.boCategory == '공지사항' ? 'selected' : ''}>공지사항</option>
                                        <option value="일반게시판" ${board.boCategory == '일반게시판' ? 'selected' : ''}>일반게시판</option>
                                        <option value="Q&A" ${board.boCategory == 'Q&A' ? 'selected' : ''}>Q&A</option>
                                    </select>
                                </div>
                                
                                <div class="form-group">
                                    <label class="form-label">제목</label>
                                    <input type="text" name="boTitle" class="form-input" value="${board.boTitle}" required>
                                </div>
                                
                                <div class="form-group">
                                    <label class="form-label">내용</label>
                                    <textarea name="boContent" class="form-textarea" rows="10" required>${board.boContent}</textarea>
                                </div>
                                
                                <div class="form-actions">
                                    <button type="submit" class="btn btn-primary">수정 완료</button>
                                    <button type="button" onclick="cancelEdit()" class="btn btn-secondary">취소</button>
                                </div>
                            </form>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <script>
        // 메시지 표시
        <c:if test="${not empty success}">
            alert('${success}');
        </c:if>
        <c:if test="${not empty error}">
            alert('${error}');
        </c:if>

        // 수정 모드 토글
        function editBoard() {
            document.querySelector('.board-detail-content').style.display = 'none';
            document.querySelector('.board-detail-actions').style.display = 'none';
            document.getElementById('editForm').style.display = 'block';
        }

        function cancelEdit() {
            document.querySelector('.board-detail-content').style.display = 'block';
            document.querySelector('.board-detail-actions').style.display = 'flex';
            document.getElementById('editForm').style.display = 'none';
        }

        // 게시글 삭제
        function deleteBoard() {
            if (confirm('게시글을 삭제하시겠습니까?')) {
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = '<%=ctx%>/boardAction';
                
                const actionInput = document.createElement('input');
                actionInput.type = 'hidden';
                actionInput.name = 'action';
                actionInput.value = 'delete';
                form.appendChild(actionInput);
                
                const postNoInput = document.createElement('input');
                postNoInput.type = 'hidden';
                postNoInput.name = 'postNo';
                postNoInput.value = '${board.postNo}';
                form.appendChild(postNoInput);
                
                document.body.appendChild(form);
                form.submit();
            }
        }
    </script>
</body>
</html>
