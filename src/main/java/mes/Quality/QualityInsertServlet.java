// 등록
// src/main/java/com/mes/Controller/Quality/QualityInsertServlet.java
package com.mes.Controller.Quality;

import com.mes.DAO.QualityDAO;
import com.mes.DTO.QualityDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;

@WebServlet("/quality/insert")
public class QualityInsertServlet extends HttpServlet {
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        QualityDTO d = new QualityDTO();
        d.setWorkNo(req.getParameter("workNo"));
        d.setStandardCode(req.getParameter("standardCode"));
        d.setEmployeeNo(req.getParameter("employeeNo"));
        d.setQuResult(req.getParameter("quResult"));
        d.setQuQuantity(Integer.parseInt(req.getParameter("quQuantity")));
        d.setQuManufactureDate(Date.valueOf(req.getParameter("quManufactureDate")));
        d.setDefectQuantity(Integer.parseInt(req.getParameter("defectQuantity")));
        d.setInspectionDate(Timestamp.valueOf(req.getParameter("inspectionDate") + ":00")); // yyyy-MM-ddTHH:mm → +":00"

        int r = QualityDAO.getInstance().insert(d);
        resp.sendRedirect(req.getContextPath() + "/qualityList");
    }
}