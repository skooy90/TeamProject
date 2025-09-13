// src/main/java/com/mes/Controller/Auth/LoginServlet.java
package mes.Controller.Auth;

import mes.DAO.UsersDAO;
import mes.DTO.UsersDTO;
import mes.security.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("userID");
        String pw = req.getParameter("userPW");

        UsersDAO dao = UsersDAO.getInstance();
        UsersDTO user = dao.findByEmployeeNo(id);

        if (user == null) {
            req.setAttribute("error", "사원번호 또는 비밀번호가 올바르지 않습니다.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        String stored = user.getUsPassword(); // 해시 또는 과거 평문

        // BCrypt 검증 (레거시 평문도 허용하도록 PasswordUtil.verify가 처리)
        if (!PasswordUtil.verify(pw, stored)) {
            req.setAttribute("error", "사원번호 또는 비밀번호가 올바르지 않습니다.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        // 성공: 레거시/낮은 cost면 즉시 재해시하여 DB 저장 (점진적 마이그레이션)
        if (PasswordUtil.needsRehash(stored)) {
            dao.updatePassword(user.getEmployeeNo(), pw); // 내부에서 hash()
        }

        // 세션 아이디 재발급(세션 고정 공격 방지)
        req.changeSessionId();

        HttpSession session = req.getSession();
        session.setAttribute("loginUser", user);
        session.setAttribute("empNo", user.getEmployeeNo());
        session.setAttribute("empName", user.getUsName());
        session.setAttribute("authority", user.getUsAuthority());
        session.setMaxInactiveInterval(60 * 30);

        resp.sendRedirect(req.getContextPath() + "/standardList");
    }
}
