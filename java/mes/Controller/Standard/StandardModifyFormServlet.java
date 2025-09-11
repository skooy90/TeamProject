// src/main/java/com/mes/Controller/StandardModifyFormServlet.java
package mes.Controller.Standard;

import mes.DAO.StandardDAO;
import mes.DTO.StandardDTO;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/modifyStandard")
public class StandardModifyFormServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        if (code == null || code.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/standardList");
            return;
        }
        StandardDTO item = StandardDAO.getInstance().findByCode(code);
        if (item == null) {
            resp.sendRedirect(req.getContextPath() + "/standardList");
            return;
        }
        req.setAttribute("standard", item);
        // 기존에 쓰시던 폼 JSP로 포워딩 (파일명/경로에 맞게)
        req.getRequestDispatcher("/jsp/Standard/Standard.jsp").forward(req, resp);
    }
}

