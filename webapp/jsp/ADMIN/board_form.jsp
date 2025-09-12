<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시글 등록 - MES 시스템</title>
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
            <h1 class="page-title">게시글 등록</h1>

            <!-- 등록 폼 -->
            <div class="form-container">
                <form method="POST" action="${ctx}/admin/boards/action" class="board-form">
                    <input type="hidden" name="action" value="create">
                    
                    <div class="form-group">
                        <label class="form-label" for="postNo">게시글번호</label>
                        <input type="text" class="form-input" id="postNo" name="postNo" 
                               value="${nextPostNo}" readonly>
                        <small class="form-help">자동 생성됩니다</small>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="employeeNo">작성자 사원번호 *</label>
                        <input type="text" class="form-input" id="employeeNo" name="employeeNo" 
                               placeholder="작성자 사원번호를 입력하세요" required>
                        <small class="form-help">게시글을 작성하는 사원의 사원번호입니다.</small>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="boWriter">작성자명 *</label>
                        <input type="text" class="form-input" id="boWriter" name="boWriter" 
                               placeholder="작성자명을 입력하세요" required>
                    </div>

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
                        <textarea class="form-input" id="boContent" name="boContent" rows="10" 
                                  placeholder="내용을 입력하세요" required></textarea>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">등록</button>
                        <a href="${ctx}/admin/boards" class="btn btn-secondary">취소</a>
                    </div>
                </form>
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
        document.querySelector('.board-form').addEventListener('submit', function(e) {
            const requiredFields = ['employeeNo', 'boWriter', 'boCategory', 'boTitle', 'boContent'];
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
        document.querySelectorAll('.form-input, .form-select').forEach(function(field) {
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
    </script>
</body>
</html>
