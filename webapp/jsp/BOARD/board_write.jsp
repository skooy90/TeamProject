<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시글 작성 - MES 시스템</title>
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
        .esse {
        	font-size: 12px;
        	color: #808080;        }
    </style>
</head>
<body>
    <jsp:include page="../../Header_Side/header.jsp" />
    <div class="main-container">
        <jsp:include page="../../Header_Side/sidebar.jsp" />
        <div class="content-area">
            <div class="board-container">
                <!-- 페이지 헤더 -->
                <div class="board-header">
                    <h1 class="board-title">게시글 작성</h1>
                    <div class="board-actions">
                        <a href="<%=ctx%>/boardList" class="btn btn-secondary">목록</a>
                    </div>
                </div>

                <!-- 작성 폼 -->
                <div class="write-form-container">
                    <form method="POST" action="<%=ctx%>/boardAction" class="write-form">
                        <input type="hidden" name="action" value="write">
                        <input type="hidden" name="postNo" value="${nextPostNo}">
                        <input type="hidden" name="employeeNo" value="${sessionScope.empNo}">
                        <input type="hidden" name="boWriter" value="${sessionScope.empName}">
						<p class="esse"> * 필수 입력 사항입니다. </p>  
						<br>
                        <div class="form-group">
                            <label class="form-label" for="boCategory">카테고리 *</label>
                            <select class="form-select" id="boCategory" name="boCategory" required>
                                <option value="">카테고리를 선택하세요</option>
                                <option value="공지사항">공지사항</option>
                                <option value="일반게시판">일반게시판</option>
                                <option value="Q&A">Q&A</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="boTitle">제목 *</label>
                            <input type="text" class="form-input" id="boTitle" name="boTitle" 
                                   placeholder="제목을 입력하세요" required>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="boContent">내용 *</label>
                            <textarea class="form-textarea" id="boContent" name="boContent" rows="15" 
                                      placeholder="내용을 입력하세요" required></textarea>
                        </div>

                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">등록</button>
                            <a href="<%=ctx%>/boardList" class="btn btn-secondary">취소</a>
                        </div>
                    </form>
                </div>
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

        // 폼 유효성 검사
        document.querySelector('.write-form').addEventListener('submit', function(e) {
            const requiredFields = ['boCategory', 'boTitle', 'boContent'];
            let isValid = true;
            
            requiredFields.forEach(function(fieldName) {
                const field = document.getElementById(fieldName);
                if (!field.value.trim()) {
                    field.style.borderColor = '#e74c3c';
                    isValid = false;
                } else {
                    field.style.borderColor = '#ddd';
                }
            });
            
            if (!isValid) {
                e.preventDefault();
                alert('필수 항목을 모두 입력해주세요.');
            }
        });

        // 입력 필드 포커스 시 테두리 색상 변경
        document.querySelectorAll('.form-input, .form-select, .form-textarea').forEach(function(field) {
            field.addEventListener('focus', function() {
                this.style.borderColor = '#3498db';
            });
            
            field.addEventListener('blur', function() {
                if (this.value.trim()) {
                    this.style.borderColor = '#27ae60';
                } else {
                    this.style.borderColor = '#ddd';
                }
            });
        });

        // 카테고리 선택 시 색상 변경
        document.getElementById('boCategory').addEventListener('change', function() {
            if (this.value) {
                this.style.borderColor = '#27ae60';
            } else {
                this.style.borderColor = '#ddd';
            }
        });
    </script>
</body>
</html>
