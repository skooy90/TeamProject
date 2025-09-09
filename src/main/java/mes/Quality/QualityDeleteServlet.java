// 삭제
// src/main/java/com/mes/Controller/Quality/QualityDeleteServlet.java
package com.mes.Controller.Quality;

import com.mes.DAO.QualityDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/quality/delete")
public class QualityDeleteServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String no = req.getParameter("no");
        if (no != null && !no.isEmpty()) {
            QualityDAO.getInstance().delete(no);
        }
        resp.sendRedirect(req.getContextPath() + "/qualityList");
    }
}
