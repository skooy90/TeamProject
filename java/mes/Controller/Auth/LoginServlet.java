// src/main/java/com/mes/Controller/Auth/LoginServlet.java
package mes.Controller.Auth;

import mes.DAO.UsersDAO;
import mes.DTO.UsersDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("userID");     // 로그인 폼 name과 일치
        String pw = req.getParameter("userPW");

        UsersDTO user = UsersDAO.getInstance().login(id, pw);

        if (user != null) {
            // 로그인 성공 → 세션 저장
            HttpSession session = req.getSession();
            session.setAttribute("loginUser", user);         // 전체 DTO
            session.setAttribute("empNo", user.getEmployeeNo());
            session.setAttribute("empName", user.getUsName());
            session.setAttribute("authority", user.getUsAuthority()); // ADMIN/EMPLOYEE 등
            session.setMaxInactiveInterval(60 * 30); // 30분
            
            // 기준관리로 이동 (프로젝트에서 사용 중인 URL로 맞춰주세요)
            resp.sendRedirect(req.getContextPath() + "/standardList");
        } else {
            // 실패 → 로그인 화면으로, 메시지
            req.setAttribute("error", "사원번호 또는 비밀번호가 올바르지 않습니다.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
