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
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    req.setCharacterEncoding("UTF-8");
    String no = req.getParameter("no"); // 검사번호

    if (no != null && !no.isBlank()) {
      // ▶ 수정모드: 검사 단건 조회해서 JSP에 quality 라는 이름으로 전달
      QualityDTO dto = QualityDAO.getInstance().findByNo(no);
      if (dto == null) {
        resp.sendRedirect(req.getContextPath()+"/qualityList?err=not_found");
        return;
      }
      req.setAttribute("quality", dto);
      // 수정 모드에는 works 드롭다운 필요 없음
    } else {
      // ▶ 등록모드: 작업번호 선택 드롭다운 데이터 제공
      // (workNo, stName 정도만 담긴 리스트)
      req.setAttribute("works",
          WorkDAO.getInstance().findForQualityForm());
    }

    req.getRequestDispatcher("/jsp/Quality/quality_form.jsp")
       .forward(req, resp);
  }
}