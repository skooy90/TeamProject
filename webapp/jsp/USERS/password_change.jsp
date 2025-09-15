<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>비밀번호 변경</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Header_Side/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .content-wrapper {
            margin-left: 250px;
            padding: 20px;
            min-height: calc(100vh - 60px);
        }

        .password-container {
            max-width: 500px;
            margin: 0 auto;
            padding: 30px;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .password-header {
            text-align: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 2px solid #e9ecef;
        }

        .password-header h2 {
            color: #2c3e50;
            margin-bottom: 10px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #555;
        }

        .form-group input {
            width: 100%;
            padding: 12px;
            border: 2px solid #e9ecef;
            border-radius: 6px;
            font-size: 14px;
            transition: border-color 0.3s ease;
        }

        .form-group input:focus {
            outline: none;
            border-color: #3498db;
        }

        .btn {
            width: 100%;
            padding: 12px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 16px;
            font-weight: bold;
            text-decoration: none;
            display: inline-block;
            text-align: center;
            transition: all 0.3s ease;
            margin-bottom: 10px;
        }

        .btn-primary {
            background: #3498db;
            color: white;
        }

        .btn-primary:hover {
            background: #2980b9;
        }

        .btn-secondary {
            background: #95a5a6;
            color: white;
        }

        .btn-secondary:hover {
            background: #7f8c8d;
        }

        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 6px;
            border-left: 4px solid;
        }

        .alert-success {
            background: #d4edda;
            border-color: #28a745;
            color: #155724;
        }

        .alert-error {
            background: #f8d7da;
            border-color: #dc3545;
            color: #721c24;
        }

        .password-strength {
            margin-top: 5px;
            font-size: 12px;
        }

        .strength-weak { color: #e74c3c; }
        .strength-medium { color: #f39c12; }
        .strength-strong { color: #27ae60; }

        .back-link {
            text-align: center;
            margin-top: 20px;
        }

        .back-link a {
            color: #3498db;
            text-decoration: none;
            font-size: 14px;
        }

        .back-link a:hover {
            text-decoration: underline;
        }

        @media (max-width: 768px) {
            .content-wrapper {
                margin-left: 0;
                padding: 10px;
            }
        }
        .esse {
        	font-size: 12px;
        	color: #808080;        }
    </style>
</head>
<body>
    <c:set var="ctx" value="${pageContext.request.contextPath}" />
    
    <!-- 헤더 포함 -->
    <jsp:include page="/Header_Side/header.jsp" />
    
    <!-- 사이드바 포함 -->
    <jsp:include page="/Header_Side/sidebar.jsp" />
    
    <div class="content-wrapper">
        <div class="password-container">
        <div class="password-header">
            <h2><i class="fas fa-key"></i> 비밀번호 변경</h2>
            <p>보안을 위해 정기적으로 비밀번호를 변경해주세요</p>
        </div>

        <!-- 알림 메시지 -->
        <c:if test="${not empty success}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i> ${success}
            </div>
        </c:if>
        
        <c:if test="${not empty error}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-circle"></i> ${error}
            </div>
        </c:if>

        <form action="${ctx}/mypage/password/update" method="post" id="passwordForm">
            <div class="form-group">
                <label for="currentPassword">현재 비밀번호</label>
                <input type="password" id="currentPassword" name="currentPassword" 
                       placeholder="현재 비밀번호를 입력하세요" required>
            </div>

            <div class="form-group">
                <label for="newPassword">새 비밀번호</label>
                <input type="password" id="newPassword" name="newPassword" 
                       placeholder="새 비밀번호를 입력하세요" required>
                <div id="passwordStrength" class="password-strength"></div>
                <p class="esse"> * 비밀번호 8글자 이상으로 입력 하세요. </p> 
            </div>

            <div class="form-group">
                <label for="confirmPassword">새 비밀번호 확인</label>
                <input type="password" id="confirmPassword" name="confirmPassword" 
                       placeholder="새 비밀번호를 다시 입력하세요" required>
            </div>

            <button type="submit" class="btn btn-primary">
            
                <i class="fas fa-save"></i> 비밀번호 변경
            </button>
        </form>

        <div class="back-link">
            <a href="${ctx}/mypage">
                <i class="fas fa-arrow-left"></i> 마이페이지로 돌아가기
            </a>
        </div>
        </div>
    </div>

    <script>
        // 비밀번호 강도 체크
        document.getElementById('newPassword').addEventListener('input', function() {
            const password = this.value;
            const strengthDiv = document.getElementById('passwordStrength');
            
            if (password.length === 0) {
                strengthDiv.textContent = '';
                return;
            }
            
            let strength = 0;
            let strengthText = '';
            
            if (password.length >= 8) strength++;
            if (/[a-z]/.test(password)) strength++;
            if (/[A-Z]/.test(password)) strength++;
            if (/[0-9]/.test(password)) strength++;
            if (/[^A-Za-z0-9]/.test(password)) strength++;
            
            if (strength < 3) {
                strengthText = '약함';
                strengthDiv.className = 'password-strength strength-weak';
            } else if (strength < 5) {
                strengthText = '보통';
                strengthDiv.className = 'password-strength strength-medium';
            } else {
                strengthText = '강함';
                strengthDiv.className = 'password-strength strength-strong';
            }
            
            strengthDiv.textContent = `비밀번호 강도: ${strengthText}`;
        });

        // 폼 유효성 검사
        document.getElementById('passwordForm').addEventListener('submit', function(e) {
            const newPassword = document.getElementById('newPassword').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            
            if (newPassword.length < 8) {
                e.preventDefault();
                alert('비밀번호는 최소 8자 이상이어야 합니다.');
                return;
            }
            
            if (newPassword !== confirmPassword) {
                e.preventDefault();
                alert('새 비밀번호가 일치하지 않습니다.');
                return;
            }
            
            if (newPassword.length > 20) {
                e.preventDefault();
                alert('비밀번호는 최대 20자까지 가능합니다.');
                return;
            }
        });
    </script>
</body>
</html>
