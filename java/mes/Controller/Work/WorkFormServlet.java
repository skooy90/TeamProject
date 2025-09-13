package mes.Controller.Work;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mes.DAO.ProductionDAO;
import mes.DAO.WorkDAO;
import mes.DTO.WorkDTO;

//WorkFormServlet.java  (신규/수정 공용 폼 진입)
@WebServlet("/work/form")
public class WorkFormServlet extends HttpServlet {
  @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String no = req.getParameter("no");

    if (no == null || no.isBlank()) {
      // 등록 모드
      List<Map<String, Object>> list =
          ProductionDAO.getInstance().findForWorkFormWithTarget();
      req.setAttribute("productions", list);
    } else {
      // 수정 모드
      WorkDTO w = WorkDAO.getInstance().findByNo(no);
      if (w == null) { resp.sendRedirect(req.getContextPath()+"/workList?err=not_found"); return; }
      req.setAttribute("work", w);
    }

    req.getRequestDispatcher("/jsp/WORK/work_form.jsp").forward(req, resp);
  }
}
