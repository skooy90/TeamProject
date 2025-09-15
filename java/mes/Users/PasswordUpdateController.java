package mes.Controller.Users;

import mes.DAO.UsersDAO;
import mes.DTO.UsersDTO;
import mes.security.PasswordUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/mypage/password/update")
public class PasswordUpdateController extends HttpServlet {
    private UsersDAO usersDAO = UsersDAO.getInstance();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
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
            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");
            
            // 유효성 검사
            if (currentPassword == null || currentPassword.trim().isEmpty()) {
                request.setAttribute("error", "현재 비밀번호를 입력해주세요.");
                request.setAttribute("loginUser", loginUser);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/USERS/password_change.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            if (newPassword == null || newPassword.trim().isEmpty()) {
                request.setAttribute("error", "새 비밀번호를 입력해주세요.");
                request.setAttribute("loginUser", loginUser);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/USERS/password_change.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
                request.setAttribute("error", "새 비밀번호 확인을 입력해주세요.");
                request.setAttribute("loginUser", loginUser);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/USERS/password_change.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                request.setAttribute("error", "새 비밀번호가 일치하지 않습니다.");
                request.setAttribute("loginUser", loginUser);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/USERS/password_change.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            if (newPassword.length() < 8) {
                request.setAttribute("error", "새 비밀번호는 최소 8자 이상이어야 합니다.");
                request.setAttribute("loginUser", loginUser);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/USERS/password_change.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            if (newPassword.length() > 20) {
                request.setAttribute("error", "새 비밀번호는 최대 20자까지 가능합니다.");
                request.setAttribute("loginUser", loginUser);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/USERS/password_change.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            // 현재 비밀번호 검증
            String storedPassword = loginUser.getUsPassword();
            if (!PasswordUtil.verify(currentPassword, storedPassword)) {
                request.setAttribute("error", "현재 비밀번호가 올바르지 않습니다.");
                request.setAttribute("loginUser", loginUser);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/USERS/password_change.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            // 새 비밀번호로 업데이트
            int result = usersDAO.updatePassword(loginUser.getEmployeeNo(), newPassword);
            
            if (result > 0) {
                // 세션 정보 업데이트
                UsersDTO updatedUser = usersDAO.findByEmployeeNo(loginUser.getEmployeeNo());
                if (updatedUser != null) {
                    session.setAttribute("loginUser", updatedUser);
                }
                
                request.setAttribute("success", "비밀번호가 성공적으로 변경되었습니다.");
                request.setAttribute("loginUser", updatedUser != null ? updatedUser : loginUser);
                
             // 성공 시 마이페이지로 리다이렉트
                response.sendRedirect(request.getContextPath() + "/mypage?success=password_changed");
                return;
                
            } else {
                request.setAttribute("error", "비밀번호 변경에 실패했습니다. 다시 시도해주세요.");
                request.setAttribute("loginUser", loginUser);
            }
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/USERS/password_change.jsp");
            dispatcher.forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "비밀번호 변경 중 오류가 발생했습니다: " + e.getMessage());
            request.setAttribute("loginUser", (UsersDTO) request.getSession().getAttribute("loginUser"));
            RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/USERS/password_change.jsp");
            dispatcher.forward(request, response);
        }
    }
}
