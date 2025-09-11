// 폼 진입(신규/수정 공용)
// src/main/java/com/mes/Controller/Quality/QualityFormServlet.java
package mes.Controller.Quality;

import mes.DAO.QualityDAO;
import mes.DAO.WorkDAO;
import mes.DTO.QualityDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/quality/form")
public class QualityFormServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        req.setAttribute("workList", WorkDAO.getInstance().findForQuality());
        req.getRequestDispatcher("/jsp/Quality/quality_form.jsp").forward(req, resp);
    }
}
