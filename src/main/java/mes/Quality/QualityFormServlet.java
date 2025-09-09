// 폼 진입(신규/수정 공용)
// src/main/java/com/mes/Controller/Quality/QualityFormServlet.java
package com.mes.Controller.Quality;

import com.mes.DAO.QualityDAO;
import com.mes.DTO.QualityDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/quality/form")
public class QualityFormServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String no = req.getParameter("no");
    if (no != null && !no.isBlank()) {
      // 수정
      QualityDTO dto = QualityDAO.getInstance().findByNo(no);
      req.setAttribute("quality", dto);
    }
    req.getRequestDispatcher("/quality_form.jsp").forward(req, resp);
    // 또는 req.getRequestDispatcher("/quality/form.jsp")
  }
}
