<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>마이페이지</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Header_Side/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <style>
        .content-wrapper {
            margin-left: 250px;
            padding: 20px;
            min-height: calc(100vh - 60px);
        }

        .mypage-container {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .mypage-header {
            text-align: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 2px solid #e9ecef;
        }

        .mypage-header h1 {
            color: #2c3e50;
            margin-bottom: 10px;
        }

        .user-info {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
            margin-bottom: 30px;
        }

        .info-card {
            background: #f8f9fa;
            padding: 20px;
      border-radius: 8px;
            border-left: 4px solid #3498db;
        }

        .info-card h3 {
            color: #2c3e50;
            margin-bottom: 15px;
            font-size: 18px;
        }

        .info-item {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
            padding: 8px 0;
            border-bottom: 1px solid #e9ecef;
        }

        .info-item:last-child {
            border-bottom: none;
        }

        .info-label {
      font-weight: bold;
            color: #555;
        }

        .info-value {
            color: #2c3e50;
        }

        .action-buttons {
            display: flex;
            gap: 15px;
            justify-content: center;
            margin-top: 30px;
        }

        .btn {
            padding: 12px 24px;
      border: none;
            border-radius: 6px;
      cursor: pointer;
            font-size: 14px;
            font-weight: bold;
            text-decoration: none;
            display: inline-block;
            text-align: center;
            transition: all 0.3s ease;
        }

        .btn-primary {
            background: #3498db;
            color: white;
        }

        .btn-primary:hover {
      background: #2980b9;
    }

        .btn-warning {
            background: #f39c12;
            color: white;
        }

        .btn-warning:hover {
            background: #e67e22;
        }

        .btn-danger {
            background: #e74c3c;
            color: white;
        }

        .btn-danger:hover {
            background: #c0392b;
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

        @media (max-width: 768px) {
            .content-wrapper {
                margin-left: 0;
                padding: 10px;
            }
            
            .user-info {
                grid-template-columns: 1fr;
            }
            
            .action-buttons {
                flex-direction: column;
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
        <div class="mypage-container">
        <div class="mypage-header">
            <h1><i class="fas fa-user-circle"></i> 마이페이지</h1>
            <p>개인 정보를 확인하고 관리하세요</p>
        </div>

        <!-- 알림 메시지 -->
<%--         <c:if test="${not empty success}"> --%>
<!--             <div class="alert alert-success"> -->
<%--                 <i class="fas fa-check-circle"></i> ${success} --%>
<!--             </div> -->
<%--         </c:if> --%>
        
<%--         <c:if test="${not empty error}"> --%>
<!--             <div class="alert alert-error"> -->
<%--                 <i class="fas fa-exclamation-circle"></i> ${error} --%>
<!--             </div> -->
<%--         </c:if> --%>

        <div class="user-info">
            <!-- 기본 정보 -->
            <div class="info-card">
                <h3><i class="fas fa-id-card"></i> 기본 정보</h3>
                <div class="info-item">
                    <span class="info-label">사원번호</span>
                    <span class="info-value">${loginUser.employeeNo}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">이름</span>
                    <span class="info-value">${loginUser.usName}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">직급</span>
                    <span class="info-value">${loginUser.usPosition}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">권한</span>
                    <span class="info-value">
                        <c:choose>
                            <c:when test="${loginUser.usAuthority == 'ADMIN'}">
                                <span style="color: #e74c3c; font-weight: bold;">관리자</span>
                            </c:when>
                            <c:otherwise>
                                <span style="color: #27ae60; font-weight: bold;">사용자</span>
                            </c:otherwise>
                        </c:choose>
                    </span>
                </div>
            </div>

            <!-- 계정 정보 -->
            <div class="info-card">
                <h3><i class="fas fa-cog"></i> 계정 정보</h3>
                <div class="info-item">
                    <span class="info-label">비밀번호 상태</span>
                    <span class="info-value">
                        <c:choose>
                            <c:when test="${loginUser.usPsUpStatus == 1}">
                                <span style="color: #27ae60;"><i class="fas fa-check"></i> 최신</span>
                            </c:when>
                            <c:otherwise>
                                <span style="color: #f39c12;"><i class="fas fa-exclamation-triangle"></i> 변경 필요</span>
                            </c:otherwise>
                        </c:choose>
                    </span>
                </div>
                    <p class="esse"> * 첫 로그인시 초기 비밀번호 수정해야 합니다. </p> 
                <div class="info-item">
                    <span class="info-label">가입일</span>
                    <span class="info-value">${loginUser.createDate}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">최종 수정일</span>
                    <span class="info-value">${loginUser.updateDate}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">세션 상태</span>
                    <span class="info-value" style="color: #27ae60;">
                        <i class="fas fa-circle"></i> 활성
                    </span>
                </div>
            </div>
        </div>

        <div class="action-buttons">
            <a href="${ctx}/mypage/password" class="btn btn-warning">
                <i class="fas fa-key"></i> 비밀번호 변경
            </a>
        </div>
        </div>
    </div>
</body>
</html>