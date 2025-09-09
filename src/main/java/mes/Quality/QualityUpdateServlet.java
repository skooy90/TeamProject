// src/main/java/com/mes/Controller/Quality/QualityUpdateServlet.java
package com.mes.Controller.Quality;

import com.mes.DAO.QualityDAO;
import com.mes.DTO.QualityDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;

@WebServlet("/quality/update")
public class QualityUpdateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        QualityDTO d = new QualityDTO();
        d.setQualityNo(req.getParameter("qualityNo"));
        d.setWorkNo(req.getParameter("workNo"));
        d.setStandardCode(req.getParameter("standardCode"));
        d.setEmployeeNo(req.getParameter("employeeNo"));
        d.setQuResult(req.getParameter("quResult"));
        d.setQuQuantity(Integer.parseInt(req.getParameter("quQuantity")));
        d.setDefectQuantity(Integer.parseInt(req.getParameter("defectQuantity")));

        // Date (yyyy-MM-dd)
        d.setQuManufactureDate(Date.valueOf(req.getParameter("quManufactureDate")));

        // Timestamp (accepts "yyyy-MM-ddTHH:mm" or "yyyy-MM-dd HH:mm" or with seconds)
        String ins = req.getParameter("inspectionDate"); // e.g. 2025-01-17T16:30
        Timestamp inspectionTs = null;
        if (ins != null && !ins.isBlank()) {
            ins = ins.trim().replace('T', ' '); // 2025-01-17 16:30
            if (ins.length() == 16) {           // yyyy-MM-dd HH:mm
                ins += ":00";                   // -> yyyy-MM-dd HH:mm:00
            }
            inspectionTs = Timestamp.valueOf(ins);
        }
        d.setInspectionDate(inspectionTs);

        QualityDAO.getInstance().update(d);
        resp.sendRedirect(req.getContextPath() + "/qualityList");
    }
}
