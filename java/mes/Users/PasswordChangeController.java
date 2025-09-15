package mes.Controller.Users;

import mes.DAO.UsersDAO;
import mes.DTO.UsersDTO;
import mes.security.PasswordUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/mypage/password")
public class PasswordChangeController extends HttpServlet {
    private UsersDAO usersDAO = UsersDAO.getInstance();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        
        try {
            // 세션에서 로그인한 사용자 정보 가져오기
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("loginUser") == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }
            
            UsersDTO loginUser = (UsersDTO) session.getAttribute("loginUser");
            request.setAttribute("loginUser", loginUser);
            
            // JSP로 포워드
            RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/USERS/password_change.jsp");
            dispatcher.forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "비밀번호 변경 페이지 로드 중 오류가 발생했습니다: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/USERS/password_change.jsp");
            dispatcher.forward(request, response);
        }
    }
}
