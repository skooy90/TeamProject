// src/main/java/com/mes/Filter/AuthFilter.java
package com.mes.Filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

/** 로그인 필요 페이지 보호 */
@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req  = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String ctx = req.getContextPath();
        String uri = req.getRequestURI();

        // 허용 경로: 로그인 화면/처리, 정적파일, 헬스체크 등
        boolean allowed = uri.startsWith(ctx + "/login.jsp")
                       || uri.startsWith(ctx + "/login")
                       || uri.startsWith(ctx + "/logout")
                       || uri.startsWith(ctx + "/css/")
                       || uri.startsWith(ctx + "/js/")
                       || uri.startsWith(ctx + "/images/");

        if (allowed) {
            chain.doFilter(request, response);
            return;
        }

        // 로그인 체크
        HttpSession session = req.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("loginUser") != null);

        if (!loggedIn) {
            resp.sendRedirect(ctx + "/login.jsp");
            return;
        }

        chain.doFilter(request, response);
    }
}

