// 목록 + 검색
// src/main/java/com/mes/Controller/Quality/QualityListServlet.java
package com.mes.Controller.Quality;

import com.mes.DAO.QualityDAO;
import com.mes.DTO.QualityDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/qualityList")
public class QualityListServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String q = req.getParameter("q");
        QualityDAO dao = QualityDAO.getInstance();
        List<QualityDTO> list = (q != null && q.trim().length() >= 2)
                ? dao.searchByPrefix(q.trim())
                : dao.findAll();
        req.setAttribute("qualityList", list);
        req.setAttribute("q", q == null ? "" : q.trim());
        req.getRequestDispatcher("/quality.jsp").forward(req, resp);
    }
}
